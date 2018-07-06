package com.abatechnology.kirana2door.Activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.abatechnology.kirana2door.Connecttodb;
import com.abatechnology.kirana2door.R;

import java.util.HashMap;
import java.util.Map;

import fr.ganfra.materialspinner.MaterialSpinner;

import static android.support.design.widget.Snackbar.LENGTH_LONG;

public class ForgotPassOne extends AppCompatActivity {

    PinEntryEditText otp;
    EditText email;
    Button btnsend,btnveri;
    LinearLayout emailView , verificationview;
    RequestQueue queue;
    View parentLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        emailView=findViewById(R.id.one);
        verificationview=findViewById(R.id.two);
        email=findViewById(R.id.remail);
        btnsend=findViewById(R.id.sdotp);
        btnveri=findViewById(R.id.varibtn);
        otp=findViewById(R.id.enotp);
        parentLayout = findViewById(android.R.id.content);
        queue= Volley.newRequestQueue(this);

        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest request =new StringRequest(Request.Method.POST, Connecttodb.path + "forgotpassemailverification", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("successfully sent")){
                            emailView.setVisibility(View.GONE);
                            verificationview.setVisibility(View.VISIBLE);
                        }else if(response.equals("User is not verified")){
                            Snackbar snackbar = Snackbar.make(parentLayout, "User is not verified !", LENGTH_LONG);
                            snackbar.show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Snackbar snackbar = Snackbar.make(parentLayout, "Connection Problem! OR No Internet Connection !", LENGTH_LONG);
                        snackbar.show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {

                        Map<String, String>  params = new HashMap<String, String>();

                        params.put("email",email.getText().toString().trim());

                        return params;
                    }

                };

                queue.add(request);
            }
        });

        btnveri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest request =new StringRequest(Request.Method.POST, Connecttodb.path + "forgotpassotpvalidation", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("successful")){
                            Intent intent = new Intent(ForgotPassOne.this, ForgotPassTwo.class);
                            intent.putExtra("email",email.getText().toString().trim());
                            startActivity(intent);
                        }else if(response.equals("OTP not match")){
                            Snackbar snackbar = Snackbar.make(parentLayout, "OTP not matched !", LENGTH_LONG);
                            snackbar.show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Snackbar snackbar = Snackbar.make(parentLayout, "Connection Problem! OR No Internet Connection !", LENGTH_LONG);
                        snackbar.show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {

                        Map<String, String>  params = new HashMap<String, String>();

                        params.put("otp",otp.getText().toString().trim());

                        return params;
                    }

                };

                queue.add(request);
            }
        });
    }
}
