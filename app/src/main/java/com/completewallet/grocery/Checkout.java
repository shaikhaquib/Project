package com.completewallet.grocery;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.completewallet.grocery.Activity.BuyNow;
import com.completewallet.grocery.Activity.DataVar;
import com.completewallet.grocery.Activity.Global;
import com.completewallet.grocery.Activity.MainActivity;
import com.completewallet.grocery.Adapter.CartAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.support.design.widget.Snackbar.LENGTH_LONG;

public class Checkout extends AppCompatActivity {

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

                        int maintotal =inttotal + sch;

                        shippinhtime.setText("Shipping Time "+Stime);
                        Scharge=jsonObject.getString("shipping_charges");
                        shpipintcharge.setText("Shipping Charge "+"₹"+Scharge);

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
