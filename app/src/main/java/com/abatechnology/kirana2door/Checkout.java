package com.abatechnology.kirana2door;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.abatechnology.kirana2door.Activity.AccountUpdate;
import com.abatechnology.kirana2door.Activity.BuyNow;
import com.abatechnology.kirana2door.Activity.DataVar;
import com.abatechnology.kirana2door.Activity.Global;
import com.abatechnology.kirana2door.Activity.MainActivity;
import com.abatechnology.kirana2door.Adapter.CartAdapter;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.support.design.widget.Snackbar.LENGTH_LONG;

public class Checkout extends AppCompatActivity {
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    RecyclerView recyclerView;
    String respone , total ;
    EditText edtpincode ;
    ProgressDialog dialog;
    RequestQueue queue;
    Button validpin , confirmorder;
    LinearLayout pinlayout ,confirmlayout;
    String pid="1" , strpin ,Scharge ,Stime,finalprce;
    int a;
    TextView shippinhtime , shpipintcharge ,Totalamount ,email ,pin ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        queue= Volley.newRequestQueue(this);
        dialog =new ProgressDialog(Checkout.this);

        respone = getIntent().getStringExtra("array");
        System.out.println(respone);
        a = getIntent().getIntExtra("total",0);
        total = String.valueOf(a);
        System.out.println(total);
        edtpincode = findViewById(R.id.chkpincode);
        validpin=findViewById(R.id.chkvalidatepin);
        pinlayout=findViewById(R.id.chkpinlayout);
        confirmlayout=findViewById(R.id.checkoutlayout);
        confirmorder=findViewById(R.id.chkorder);
        shippinhtime =findViewById(R.id.stime);
        shpipintcharge=findViewById(R.id.scharge);
        Totalamount=findViewById(R.id.chktotal);
        email=findViewById(R.id.chkemail);
        pin=findViewById(R.id.chkpin);
        new Checkout.UserInfo().execute(Global.email);
        validpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtpincode.getText().toString().isEmpty()){
                    edtpincode.setError("Please enter pincode");
                }else {
                    validdatepin();
                    //pin.setText(edtpincode.getText());
                    strpin=edtpincode.getText().toString();
                }
            }
        });

        confirmorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeOrder();
            }
        });


        recyclerViewadapter();

    }

    public class UserInfo extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(Checkout.this);
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

                // Enter URL address where your php file resides

                url = new URL(Connecttodb.path+"fetch_user_info");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("email_id", params[0]);
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
                return "exception";
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
                return "exception";
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {
            pdLoading.dismiss();
            //this method will be running on UI thread

            try {

                JSONArray jArray = new JSONArray(result);

                // Extract data from json and store into ArrayList as class objects
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    edtpincode.setText((json_data.getString("pincode")));
                }
            } catch (JSONException e) {

            }

        }

        @Override
        protected void onCancelled() {

        }
    }
    private void placeOrder() {
        dialog.setMessage("Loading....");
        dialog.show();
        dialog.setCancelable(false);

        final StringRequest request = new StringRequest(StringRequest.Method.POST, Connecttodb.path + "cartcheckout", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(Checkout.this);
                } else {
                    builder = new AlertDialog.Builder(Checkout.this);
                }
                builder.setTitle("Kirana2door")
                        .setMessage(response)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                //startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                intent.putExtra("category_id","1");
                                startActivity(intent);
                            }
                        })
                        .show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Snackbar snackbar = Snackbar.make(pinlayout, "Connection Problem! OR No Internet Connection !", LENGTH_LONG);
                snackbar.show();            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String ,String> map = new HashMap<>();
                map.put("email",Global.email);
                map.put("pincode",strpin);
                map.put("totalprice",total);
                map.put("shippingcost",Scharge);
                map.put("finalprice",finalprce);
                map.put("payment_type","cash on delivery");

                return map;
            }
        };
        queue.add(request);

    }

    private void recyclerViewadapter() {

        recyclerView = findViewById(R.id.checkoutRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        List<DataVar> data=new ArrayList<>();

        try {

            JSONArray jArray = new JSONArray(respone);

            // Extract data from json and store into ArrayList as class objects
            for(int i=0;i<jArray.length();i++){
                JSONObject json_data = jArray.getJSONObject(i);
                DataVar EData = new DataVar();
                EData.cart_id = json_data.getString("cart_id");
                EData.cartproduct_id = json_data.getString("product_id");
                EData.cartproduct_name = json_data.getString("product_name");
                EData.carthsn_code = json_data.getString("hsn_code");
                EData.cartproduct_discription_1 = json_data.getString("product_discription_1");
                EData.cartproduct_discription_2 = json_data.getString("product_discription_2");
                EData.cartproduct_image = json_data.getString("product_image");
                EData.cartgst_id = json_data.getString("gst_id");
                EData.cartpcategory_id = json_data.getString("category_id");
                EData.cartproduct_price = json_data.getString("product_price");



                EData.cartproduct_mrp = json_data.getString("product_mrp");
                EData.cartproduct_weight = json_data.getString("product_weight");
                EData.cartminimum_quantity = json_data.getString("minimum_quantity");
                EData.cartunits = json_data.getString("units");
                EData.cartstatus = json_data.getString("status");
                EData.cartqty = json_data.getString("product_qty");

                int price = Integer.parseInt(json_data.getString("product_price"));
                int wt = Integer.parseInt(json_data.getString("product_qty"));

                EData.cartcustomer_id = json_data.getString("customer_id");
                data.add(EData);
            }


            // Setup and Handover data to recyclerview
            recyclerView.setAdapter(new checkadapt(Checkout.this,data));


        } catch (JSONException e) {
            //Toast.makeText(context, "No Item In Cart Or Please Check Internet Connection", Toast.LENGTH_LONG).show();
        }


    }

    private void validdatepin() {
        dialog.setMessage("Loading....");
        dialog.show();
        dialog.setCancelable(false);

        final StringRequest request = new StringRequest(StringRequest.Method.POST, Connecttodb.path + "availibilitycheck", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                //Toast.makeText(BuyNow.this, response, Toast.LENGTH_SHORT).show();
                if (!response.equals("Not available in this area !") && !response.equals("error")){
                    pinlayout.setVisibility(View.GONE);
                    confirmlayout.setVisibility(View.VISIBLE);
                    confirmorder.setVisibility(View.VISIBLE);
                    email.setText(Global.email);
                    pin.setText(strpin);
                    try {
                        JSONArray array = new JSONArray(response);
                        JSONObject jsonObject =array.getJSONObject(0);
                        int t = Integer.parseInt(jsonObject.getString("delivery_time"));
                        int hours = t / 60; //since both are ints, you get an int
                        int minutes = t % 60;

                        Stime =String.valueOf(hours)+ " : " +String.valueOf(minutes)+" Hours";

                        int inttotal = Integer.parseInt(total);
                        int sch = Integer.parseInt(jsonObject.getString("shipping_charges"));
                        int maintotal;
                        if(inttotal >= 500){
                            maintotal = inttotal;
                            Scharge = "0";
                            shpipintcharge.setText("Shipping Charge "+"₹"+Scharge);
                        }else {
                            maintotal = inttotal + sch;
                            Scharge=jsonObject.getString("shipping_charges");
                            shpipintcharge.setText("Shipping Charge "+"₹"+Scharge);
                        }
                        shippinhtime.setText("Shipping Time "+Stime);


                        Totalamount.setText("Total =  ₹  "+String.valueOf(maintotal));


                        int b = Integer.parseInt(Scharge);
                        int c=a+b;
                        finalprce = String.valueOf(c);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else {
                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(Checkout.this);
                    } else {
                        builder = new AlertDialog.Builder(Checkout.this);
                    }
                    builder.setTitle("")
                            .setMessage(response)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    //   startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                }
                            })
                            .show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Snackbar snackbar = Snackbar.make(pinlayout, "Connection Problem! OR No Internet Connection !", LENGTH_LONG);
                snackbar.show();            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String ,String> map = new HashMap<>();
                map.put("pid",pid);
                map.put("pin",edtpincode.getText().toString());

                return map;
            }
        };
        queue.add(request);

    }

}
