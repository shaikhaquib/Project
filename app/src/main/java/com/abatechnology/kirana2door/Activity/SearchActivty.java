package com.abatechnology.kirana2door.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.abatechnology.kirana2door.Adapter.ProductAdapter;
import com.abatechnology.kirana2door.Connecttodb;
import com.abatechnology.kirana2door.R;
import com.abatechnology.kirana2door.SessionManager;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.support.design.widget.Snackbar.LENGTH_LONG;

public class SearchActivty extends AppCompatActivity {

    RequestQueue queue;
    List<DataVar> data=new ArrayList<>();
    ImageView Back,clear;
    EditText query;
    RecyclerView rvSearch;
    Context context;
    private Timer timer;
    SessionManager manager;
    boolean login =true ;
    ProgressBar progressBar;
 //   ProductAdapter mAdapter;

    private TextWatcher searchTextWatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(final Editable arg0) {
            // user typed: start the timer
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    // do your actual work here
                    SearchActivty.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            String searchquery=arg0.toString();
                           // mAdapter.getFilter().filter(arg0);
                            new ProductFetch().execute(searchquery);

                        }
                    });

                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                    // hide keyboard as well?
                    // InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    // in.hideSoftInputFromWindow(searchText.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }, 60); // 600ms delay before the timer executes the "run" method from TimerTask
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // nothing to do here
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // user is typing: reset already started timer (if existing)

            if(s.toString().trim().length()==0){
                clear.setVisibility(View.GONE);
            } else {
                clear.setVisibility(View.VISIBLE);
            }

            if (timer != null) {
                timer.cancel();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_activty);
        getSupportActionBar().hide();

        context = SearchActivty.this ;

        manager =new SessionManager(this);

        if (manager.isSkip()){
            login=false;
        }else {
            login=true;
        }

        rvSearch=findViewById(R.id.rvSearch);
        rvSearch.setLayoutManager(new LinearLayoutManager(SearchActivty.this));
        Back= (ImageView) findViewById(R.id.searchClose);
        clear= (ImageView) findViewById(R.id.srchClear);
        query= (EditText) findViewById(R.id.SearchBox);
        progressBar=  findViewById(R.id.searchProgress);

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query.setText("");
            }
        });
        query.addTextChangedListener(searchTextWatcher );

    }

    private class ProductFetch extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(context);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar.setVisibility(View.VISIBLE);
            //this method will be running on UI thread
         /*   pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();*/

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your json file resides
                url = new URL(Connecttodb.path+"androidsearchbar");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            try {

                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(Global.READ_TIMEOUT);
                conn.setConnectTimeout(Global.CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("querystr",params[0]);

                String query = builder.build().getEncodedQuery();

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {

                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {
            //this method will be running on UI thread
            ArrayList<String> dataList = new ArrayList<String>();
            progressBar.setVisibility(View.GONE);

            pdLoading.dismiss();
            List<DataVar> data=new ArrayList<>();

            pdLoading.dismiss();
            try {

                JSONArray jArray = new JSONArray(result);

                // Extract data from json and store into ArrayList as class objects
                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);
                    dataList.add(json_data.getString("product_id"));
                    DataVar EData = new DataVar();
                    EData.product_id = json_data.getString("product_id");
                    EData.product_name = json_data.getString("product_name");
                    EData.hsn_code = json_data.getString("hsn_code");
                    EData.product_discription_1 = json_data.getString("product_discription_1");
                    EData.product_discription_2 = json_data.getString("product_discription_2");
                    EData.product_image = json_data.getString("product_image");
                    EData.gst_id = json_data.getString("gst_id");
                    EData.pcategory_id = json_data.getString("category_id");
                    EData.product_price = json_data.getString("product_price");
                    EData.product_mrp = json_data.getString("product_mrp");
                    EData.product_weight = json_data.getString("product_weight");
                    EData.minimum_quantity = json_data.getString("minimum_quantity");
                    EData.minimum = Integer.parseInt(json_data.getString("minimum_quantity"));
                    EData.units = json_data.getString("units");
                    EData.status = json_data.getString("status");
                    data.add(EData);
                }


                // Setup and Handover data to recyclerview
                rvSearch.setAdapter(new ProductAdapter(context,data,login));


            } catch (JSONException e) {
                Snackbar snackbar = Snackbar.make(rvSearch, "No Product Available In This Category !", LENGTH_LONG);

                snackbar.show();
                //Toast.makeText(context, "No Product Available In This Category !", Toast.LENGTH_LONG).show();
            }

        }

    }

}
