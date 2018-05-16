package com.completewallet.grocery.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.completewallet.grocery.Adapter.OrderHistoryAdapter;
import com.completewallet.grocery.Adapter.SubOrderAdapter;
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

public class SubOrderHistory extends AppCompatActivity {
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private String[] strArrData = {"No Suggestions"};
    public Context context;
    public String email,oid,cid;
    View parentLayout;
    SwipeRefreshLayout mSwipeRefreshLayout;
    public Credentials CData;
    public RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_order_history);
        parentLayout = findViewById(android.R.id.content);
        oid = getIntent().getStringExtra("orderid");
        cid = getIntent().getStringExtra("custid");
        recyclerView=findViewById(R.id.rvsubHistory);
        LinearLayoutManager layoutManager = new LinearLayoutManager(SubOrderHistory.this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swifeRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new SubOrderHistory.SubOrderFetch().execute(cid.trim(),oid.trim());
            }
        });
        //Make call to AsyncTask
        new SubOrderHistory.SubOrderFetch().execute(cid.trim(),oid.trim());
    }
    private class SubOrderFetch extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(SubOrderHistory.this);
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
                url = new URL(Connecttodb.path+"suborderhistory.php");

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
                        .appendQueryParameter("cid",params[0])
                        .appendQueryParameter("oid",params[1]);

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
                    EData.quantity = json_data.getString("quantity");
                    EData.order_product_price = json_data.getString("order_product_price");
                    EData.orderproduct_image = json_data.getString("product_image");
                    EData.orderproduct_name = json_data.getString("product_name");
                    data.add(EData);
                }
                strArrData = dataList.toArray(new String[dataList.size()]);

                // Setup and Handover data to recyclerview
                recyclerView.setAdapter(new SubOrderAdapter(SubOrderHistory.this,data));


            } catch (JSONException e) {
                Snackbar snackbar = Snackbar.make(parentLayout, "Connection Problem! OR No Internet Connection !", LENGTH_LONG);
                snackbar.show();
            }

        }

    }
}
