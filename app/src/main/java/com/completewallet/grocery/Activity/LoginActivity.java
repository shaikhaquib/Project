package com.completewallet.grocery.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.completewallet.grocery.Connecttodb;
import com.completewallet.grocery.CustomerRegisterActivity;
import com.completewallet.grocery.R;
import com.completewallet.grocery.SessionManager;

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

public class LoginActivity extends AppCompatActivity {
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    private EditText etEmail;
    private EditText etPassword;
    SessionManager manager;
    public TextView textnew,txtforgot,guestuser;
    Button btnlogin;
    View parentLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        manager=new SessionManager(this);
        etEmail = (EditText) findViewById(R.id.email);
        etPassword = (EditText) findViewById(R.id.password);
        textnew = (TextView) findViewById(R.id.registration);
        txtforgot = (TextView) findViewById(R.id.forgotp);
        btnlogin = (Button) findViewById(R.id.btnLogin);
        parentLayout = findViewById(android.R.id.content);
        guestuser = findViewById(R.id.skiplogin);

        guestuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                manager.setSkip(true);

                Snackbar snackbar = Snackbar.make(parentLayout, "Login as a guest user !", Snackbar.LENGTH_LONG);
                snackbar.show();
                Intent intent = new Intent(LoginActivity.this, Category.class);
                startActivity(intent);
            }
        });



        // Managing User session

        if (manager.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this, Category.class);
            startActivity(intent);
            finish();
        }else if (manager.isSkip()){

            // If user skipped login section

            Snackbar snackbar = Snackbar.make(parentLayout, "Login as a guest user !", Snackbar.LENGTH_LONG);
            snackbar.show();

            Intent intent = new Intent(LoginActivity.this, Category.class);
            startActivity(intent);
            finish();
        }
    }



    public void Registration(View view) {
        //startActivity(new Intent(getApplicationContext(),Registration.class));
        startActivity(new Intent(getApplicationContext(),CustomerRegisterActivity.class));
    }
    // Triggers when LOGIN Button clicked
    public void Login(View arg0) {

        // Get text from email and passord field
        final String email = etEmail.getText().toString();
        final String password = etPassword.getText().toString();

        // Initialize  AsyncLogin() class with email and password
        new AsyncLogin().execute(email,password);

    }
    private class AsyncLogin extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(LoginActivity.this);
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
                url = new URL(Connecttodb.path+"login");

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
                        .appendQueryParameter("username", params[0])
                        .appendQueryParameter("password", params[1]);
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

            if(result.equalsIgnoreCase("true"))
            {
                manager.setLogin(true);
                /* Here launching another activity when login successful. If you persist login state
                use sharedPreferences of Android. and logout button to clear sharedPreferences.
                 */

                Intent intent = new Intent(LoginActivity.this,Category.class);
                Credentials Data = new Credentials();
                Data.email_id= etEmail.getText().toString();
                Data.password = etPassword.getText().toString();
                SharedPreferences login = getSharedPreferences("login", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = login.edit();
                editor.putString("password", etPassword.getText().toString());
                editor.putString("email",etEmail.getText().toString());
                editor.commit();
                startActivity(intent);
                LoginActivity.this.finish();

            } else if (result.equalsIgnoreCase("false")) {
                Snackbar snackbar = Snackbar.make(parentLayout, "Invalid email or password !", Snackbar.LENGTH_LONG);
                snackbar.show();
                // If username and password does not match display a error message
                //Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_LONG).show();

            } else if (result.equalsIgnoreCase("Please fill email and password")) {
                Snackbar snackbar = Snackbar.make(parentLayout, "Please fill email and password !", Snackbar.LENGTH_LONG);

                snackbar.show();
                // If username and password does not match display a error message
                //Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_LONG).show();

            } else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                Snackbar snackbar = Snackbar.make(parentLayout, "Connection Problem! OR No Internet Connection!", Snackbar.LENGTH_LONG);

                snackbar.show();
                // Toast.makeText(LoginActivity.this, "Connection Problem! OR No Internet Connection!", Toast.LENGTH_LONG).show();

            }
        }

    }

    private Boolean exit = false;

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);}
}
