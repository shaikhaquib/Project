package com.abatechnology.kirana2door.Activity;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

import static android.support.design.widget.Snackbar.LENGTH_LONG;

public class OtpVerification extends AppCompatActivity {

    public PinEntryEditText otp;
    public Button verify;
    RequestQueue queue;
    View parentLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        otp = findViewById(R.id.otp);
        verify = findViewById(R.id.vrotp);
        parentLayout = findViewById(android.R.id.content);
        queue= Volley.newRequestQueue(this);
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest request =new StringRequest(Request.Method.POST, Connecttodb.path + "verificationemail", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("customer verification successfully")){
                            Toast.makeText(OtpVerification.this,"Successfully Registered",Toast.LENGTH_SHORT).show();
                            //call welcome mail send api here
                            Intent intent = new Intent(OtpVerification.this, LoginActivity.class);
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
