package com.completewallet.grocery.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.completewallet.grocery.Adapter.CartAdapter;
import com.completewallet.grocery.Adapter.OrderHistoryAdapter;
import com.completewallet.grocery.Connecttodb;
import com.completewallet.grocery.R;
import com.completewallet.grocery.SessionManager;

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

import static android.support.design.widget.Snackbar.LENGTH_LONG;

public class OrderHistoryActivity extends AppCompatActivity {
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private String[] strArrData = {"No Suggestions"};
    public Context context;
    public String email;
    View parentLayout;
    SwipeRefreshLayout mSwipeRefreshLayout;
    public Credentials CData;
    public RecyclerView recyclerView;
    LinearLayout guestlayout ;
    SessionManager manager ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        parentLayout = findViewById(android.R.id.content);
        manager=new SessionManager(OrderHistoryActivity.this);
        guestlayout = findViewById(R.id.guesusererror);
        recyclerView=findViewById(R.id.rvHistory);

        if (manager.isSkip()){
            guestlayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(OrderHistoryActivity.this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swifeRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new OrderFetch().execute("qwerty@gmail.com");
            }
        });
        //Make call to AsyncTask
        new OrderFetch().execute("qwerty@gmail.com");
    }
    private class OrderFetch extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(OrderHistoryActivity.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your json file resides
                url = new URL(Connecttodb.path+"orderhistory.php");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            try {

                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("email",params[0]);

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

            pdLoading.dismiss();
            List<DataVar> data=new ArrayList<>();

            pdLoading.dismiss();
            try {

                JSONArray jArray = new JSONArray(result);

                // Extract data from json and store into ArrayList as class objects
                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);
                    dataList.add(json_data.getString("id"));
                    DataVar EData = new DataVar();
                    EData.id = json_data.getString("id");
                    EData.order_id = json_data.getString("order_id");
                    EData.customer_id = json_data.getString("customer_id");
                    EData.order_date = json_data.getString("order_date");
                    EData.total_ammount = json_data.getString("total_ammount");
                    EData.gst = json_data.getString("gst");
                    EData.gst_ammount = json_data.getString("gst_ammount");
                    EData.shipping_charges = json_data.getString("shipping_charges");
                    EData.final_ammount = json_data.getString("final_ammount");
                    EData.delivery_type = json_data.getString("delivery_type");
                    EData.payment_type = json_data.getString("payment_type");
                    EData.pincode = json_data.getString("pincode");
                    EData.order_status = json_data.getString("order_status");
                    EData.status_change_time = json_data.getString("status_change_time");
                    data.add(EData);
                }
                strArrData = dataList.toArray(new String[dataList.size()]);

                // Setup and Handover data to recyclerview
                recyclerView.setAdapter(new OrderHistoryAdapter(OrderHistoryActivity.this,data));


            } catch (JSONException e) {
                Snackbar snackbar = Snackbar.make(parentLayout, "Connection Problem! OR No Internet Connection !", LENGTH_LONG);

                snackbar.show();
                //Toast.makeText(context, "Please Check Internet Connection", Toast.LENGTH_LONG).show();
            }

        }

    }
}
