package com.abatechnology.kirana2door;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.abatechnology.kirana2door.Activity.DataVar;
import com.abatechnology.kirana2door.Activity.LoginActivity;
import com.abatechnology.kirana2door.Activity.OtpVerification;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
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
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.ganfra.materialspinner.MaterialSpinner;

public class CustomerRegisterActivity extends AppCompatActivity {

    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private EditText fname,lname, email, phno, pass,conpass,add,pin;
    //  MaterialSpinner spinner;
    Spinner spnArea;
    String area;
    ProgressDialog pdLoading ;

    private Button btn;
    View parentLayout;
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_register);
        pdLoading = new ProgressDialog(CustomerRegisterActivity.this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //  spinner =  findViewById(R.id.Circle);
        String[] ITEMS = {"Mumbai", "Thane", "Panvel", "Navi Mumbai", "Kalyan", "Uran"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ITEMS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        requestQueue = Volley.newRequestQueue(this);
        getArea();
        spnArea=findViewById(R.id.spnarea);
        //   spinner.setAdapter(adapter);
        fname = (EditText) findViewById(R.id.first);
        lname = (EditText) findViewById(R.id.last);
        add = (EditText) findViewById(R.id.address);
        pin = (EditText) findViewById(R.id.pin);
        email = (EditText) findViewById(R.id.email);
        pass = (EditText) findViewById(R.id.password);
        phno = (EditText) findViewById(R.id.contact);

        conpass = (EditText) findViewById(R.id.confpassword);
        btn = (Button) findViewById(R.id.btnreg);
        parentLayout = findViewById(android.R.id.content);

        /*btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isValidFormData();
            }
        });*/
    }

    private void getArea() {
        pdLoading.setMessage("\tLoading...");
        pdLoading.setCancelable(false);
        pdLoading.show();
        StringRequest stringRequest =new StringRequest(Request.Method.GET, Connecttodb.path+"getareaname", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pdLoading.dismiss();
                List<String> arrayList =new ArrayList<>();

                try {
                    JSONArray jsonArray =new JSONArray(response);
                    for (int i = 0 ; i < jsonArray.length() ; i++){
                        JSONObject jsonObject =jsonArray.getJSONObject(i);
                        //arrayList.add(jsonObject.getString("area_name").replaceAll("\\s+","_"));
                        arrayList.add(jsonObject.getString("area_name"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(CustomerRegisterActivity.this,  android.R.layout.simple_spinner_dropdown_item, arrayList);
                adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
                spnArea.setAdapter(adapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pdLoading.dismiss();

            }
        }){};
        requestQueue.add(stringRequest);
    }

    public boolean isValidFormData() {
        String edtstremail = email.getText().toString();
        String edtstrphno = phno.getText().toString();
        String edtstrp = pass.getText().toString();
        String edtstrcp = conpass.getText().toString();
        String edtstrpin = pin.getText().toString();


        //Pattern pattern1 = Pattern.compile("^\\w[a-zA-Z@#0-9.]*$");
        Pattern pattern2 = Pattern.compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Pattern pattern3 = Pattern.compile("[0-9]{10}");
        Pattern pattern4 = Pattern.compile("[0-9]{6}");

        Matcher matcher2 = pattern2.matcher(edtstremail);
        Matcher matcher3 = pattern3.matcher(edtstrphno);
        Matcher matcher4 = pattern4.matcher(edtstrpin);

        if (TextUtils.isEmpty(fname.getText().toString())) {
            fname.setError("Plz enter First Name.");
            fname.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(lname.getText().toString())) {
            lname.setError("Plz enter Last Name.");
            lname.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(add.getText().toString())) {
            add.setError("Plz enter full Address.");
            add.requestFocus();
            return false;
        }
        if (!matcher2.matches()) {
            email.setError("Plz enter valid Username without spaces.");
            email.requestFocus();
            return false;
        }
        if (!matcher3.matches()) {
            phno.setError("Plz enter valid Contact No.");
            phno.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(pass.getText().toString())) {
            pass.setError("Plz Enter Password.");
            pass.requestFocus();
            return false;
        }
        if (!edtstrp.equals(edtstrcp)) {
            conpass.setError("Field does not match!!");
            conpass.requestFocus();
            return false;
        }
        return true;
    }
    public void Registercust(View arg0) {
        if (isValidFormData()) {

            final String a0 = fname.getText().toString().trim();
            final String a1 = lname.getText().toString().trim();
            final String a2 = add.getText().toString().trim();
            final String a3 = email.getText().toString().trim();
            final String a4 = phno.getText().toString().trim();
            final String a5 = conpass.getText().toString().trim();
            final String a6 = spnArea.getSelectedItem().toString().trim();
            new CustomerRegisterActivity.RegisterCustomer().execute(a0,a1,a2,a3,a4,a5,a6);
        }
    }
    private class RegisterCustomer extends AsyncTask<String, String, String>{
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
                url = new URL(Connecttodb.path+"registercustomer");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
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
                        .appendQueryParameter("fname",params[0])
                        .appendQueryParameter("lname",params[1])
                        .appendQueryParameter("address",params[2])
                        .appendQueryParameter("email",params[3])
                        .appendQueryParameter("contact",params[4])
                        .appendQueryParameter("password",params[5])
                        .appendQueryParameter("area",params[6]);

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
                    return(result.toString());

                }else{

                    return("unsuccessful");
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

            //this method will be running on UI thread

            pdLoading.dismiss();
        //Toast.makeText(CustomerRegisterActivity.this,result,Toast.LENGTH_LONG).show();
            if(result.equalsIgnoreCase("successfully registered"))
            {
                /* Here launching another activity when login successful. If you persist login state
                use sharedPreferences of Android. and logout button to clear sharedPreferences.
                 */
                Snackbar snackbar = Snackbar.make(parentLayout, "Successfully Registered !", Snackbar.LENGTH_LONG);

                snackbar.show();
                Intent intent = new Intent(CustomerRegisterActivity.this,OtpVerification.class);
                startActivity(intent);
                finish();
                //Toast.makeText(CustomerRegisterActivity.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(ClinicRegActivity.this,DoctorDashActivity.class));

            }else if (result.equalsIgnoreCase("User already Exist")){
                Snackbar snackbar = Snackbar.make(parentLayout, "User already Exist !", Snackbar.LENGTH_LONG);

                snackbar.show();
                // If username and password does not match display a error message
                //Toast.makeText(CustomerRegisterActivity.this, "oops! Please try again!", Toast.LENGTH_LONG).show();

            } else if (result.equalsIgnoreCase("oops! Please try again!")){
                Snackbar snackbar = Snackbar.make(parentLayout, "oops! Please try again !", Snackbar.LENGTH_LONG);

                snackbar.show();
                // If username and password does not match display a error message
                //Toast.makeText(CustomerRegisterActivity.this, "oops! Please try again!", Toast.LENGTH_LONG).show();

            } else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {
                Snackbar snackbar = Snackbar.make(parentLayout, "Connection Problem! OR No Internet Connection !", Snackbar.LENGTH_LONG);

                snackbar.show();
                //Toast.makeText(CustomerRegisterActivity.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();

            }
        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                break;
        } return true;
    }
}