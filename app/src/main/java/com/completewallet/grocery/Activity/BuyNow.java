package com.completewallet.grocery.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.completewallet.grocery.Connecttodb;
import com.completewallet.grocery.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.support.design.widget.Snackbar.LENGTH_LONG;
public class BuyNow extends AppCompatActivity {

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
                       int inttotal = Integer.parseInt(getIntent().getStringExtra("price"));

                       price.setText(getIntent().getStringExtra("price"));
                        int sch = Integer.parseInt(jsonObject.getString("shipping_charges"));

                        int maintotal =inttotal + sch;


                        amount.setText(String.valueOf(maintotal));
                        charge.setText(String.valueOf(sch));

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
