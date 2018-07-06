package com.abatechnology.kirana2door.Activity;

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
import com.abatechnology.kirana2door.Checkout;
import com.abatechnology.kirana2door.Connecttodb;
import com.abatechnology.kirana2door.R;

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
import java.util.HashMap;
import java.util.Map;

import static android.support.design.widget.Snackbar.LENGTH_LONG;
public class BuyNow extends AppCompatActivity {
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    String pid , strpin;
    EditText edtpincode ;
    Button validpin , confirmorder;
    RequestQueue queue;
    ProgressDialog dialog;
    LinearLayout pinlayout ,confirmlayout;
    TextView title , qunt ,amount ,email ,pin ,time ,charge,price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_now);
        queue= Volley.newRequestQueue(this);
         dialog =new ProgressDialog(BuyNow.this);

        pid=getIntent().getStringExtra("product_id");
        edtpincode = findViewById(R.id.edtpincode);
        validpin=findViewById(R.id.validatepin);
        pinlayout=findViewById(R.id.pinlayout);
        confirmlayout=findViewById(R.id.confirmlayout);
        title=findViewById(R.id.cntitle);
        qunt=findViewById(R.id.cnquantity);
        amount=findViewById(R.id.cnmrp);
        email=findViewById(R.id.cnemai);
        pin=findViewById(R.id.cnpin);
        charge=findViewById(R.id.cnCharge);
        time=findViewById(R.id.cnTime);
        price=findViewById(R.id.cnprice);

        confirmorder=findViewById(R.id.confirmorder);

        confirmorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeOrder();
            }
        });

        title.setText(getIntent().getStringExtra("name"));
        qunt.setText(getIntent().getStringExtra("quantity"));
        email.setText(Global.email);

        new BuyNow.UserInfo().execute(Global.email);


        validpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtpincode.getText().toString().isEmpty()){
                    edtpincode.setError("Please enter pincode");
                }else {
                    validdatepin();
                    pin.setText(edtpincode.getText());
                    strpin=edtpincode.getText().toString();
                }
            }
        });


    }
    public class UserInfo extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(BuyNow.this);
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

        final StringRequest request = new StringRequest(StringRequest.Method.POST, Connecttodb.path + "buynow", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(BuyNow.this);
                } else {
                    builder = new AlertDialog.Builder(BuyNow.this);
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
                Snackbar snackbar = Snackbar.make(pinlayout, "Connection problem !", LENGTH_LONG);
                snackbar.show();            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String ,String> map = new HashMap<>();
                map.put("product_id",pid);
                map.put("pincode",strpin);
                map.put("email",Global.email);
                map.put("product_qty",getIntent().getStringExtra("quantity"));
                map.put("payment_type","Cash On Delivery");

                return map;
            }
        };
        queue.add(request);

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
                    try {
                        JSONArray array = new JSONArray(response);
                        JSONObject jsonObject =array.getJSONObject(0);
                        int t = Integer.parseInt(jsonObject.getString("delivery_time"));
                        int hours = t / 60; //since both are ints, you get an int
                        int minutes = t % 60;


                      String  Stime =String.valueOf(hours)+ " : " +String.valueOf(minutes)+" Hours";
                        time.setText(Stime);
                       int inttotal = Integer.parseInt(getIntent().getStringExtra("actprice"));

                       price.setText(getIntent().getStringExtra("price"));
                        int sch = Integer.parseInt(jsonObject.getString("shipping_charges"));
                        int maintotal;
                        if(inttotal >= 500){
                            maintotal = inttotal;
                            charge.setText("0");
                        }else {
                            maintotal = inttotal + sch;
                            charge.setText(String.valueOf(sch));
                        }
                        amount.setText(String.valueOf(maintotal));


                       /* int b = Integer.parseInt(Scharge);
                        int c=a+b;
                        finalprce = String.valueOf(c);*/
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(BuyNow.this);
                    } else {
                        builder = new AlertDialog.Builder(BuyNow.this);
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
