package com.completewallet.grocery.Activity;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.completewallet.grocery.Connecttodb;
import com.completewallet.grocery.R;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.support.design.widget.Snackbar.LENGTH_LONG;

public class ForgotPassTwo extends AppCompatActivity {
    public EditText cont,newpass,confpass;
    View parentLayout;
    public Button btn;
    public String intemail;
    RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass_two);
        cont = (EditText) findViewById(R.id.cont);
        newpass = (EditText) findViewById(R.id.newpass);
        confpass = (EditText) findViewById(R.id.confpass);
        parentLayout = findViewById(android.R.id.content);
        btn = (Button) findViewById(R.id.change);
        intemail = getIntent().getStringExtra("email");
        queue= Volley.newRequestQueue(this);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValidFormData()) {
                    StringRequest request =new StringRequest(Request.Method.POST, Connecttodb.path + "forgotpasschange", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(response.equals("password successfully changed")){
                                Toast.makeText(ForgotPassTwo.this,"Password Changed Successfully",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ForgotPassTwo.this, LoginActivity.class);
                                startActivity(intent);
                            }else if(response.equals("User not Exist")){
                                Snackbar snackbar = Snackbar.make(parentLayout, "Please provide correct contact number.", LENGTH_LONG);
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

                            params.put("email",intemail.trim());
                            params.put("contact",cont.getText().toString().trim());
                            params.put("pass",confpass.getText().toString().trim());

                            return params;
                        }

                    };

                    queue.add(request);
                }
            }
        });
    }
    public boolean isValidFormData() {
        String edtstrnewpass = newpass.getText().toString();
        String edtstrconfpass = confpass.getText().toString();
        String edtstrphno = cont.getText().toString();
        //Pattern pattern1 = Pattern.compile("[a-zA-Z0-9\\\\!\\\\@\\\\#\\\\$]{8,20}");
        Pattern pattern1 = Pattern.compile("[0-9]{10}");
        //Matcher matcher1 = pattern1.matcher(edtstrnewpass);
        Matcher matcher1 = pattern1.matcher(edtstrphno);
        if (!matcher1.matches()) {
            cont.setError("Plz enter Contact Number.");
            cont.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(newpass.getText().toString())) {
            newpass.setError("Plz enter New Password.");
            newpass.requestFocus();
            return false;
        }
        if (!edtstrconfpass.equals(edtstrnewpass)) {
            confpass.setError("Field does not matched!!");
            confpass.requestFocus();
            return false;
        }
        return true;
    }
}
