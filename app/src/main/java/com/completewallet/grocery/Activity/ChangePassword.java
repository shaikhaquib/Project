package com.completewallet.grocery.Activity;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.completewallet.grocery.Connecttodb;
import com.completewallet.grocery.R;

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

public class ChangePassword extends AppCompatActivity {
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    private EditText old,newpass,confpass;
    View parentLayout;
    private Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        old = (EditText) findViewById(R.id.oldpass);
        newpass = (EditText) findViewById(R.id.newpass);
        confpass = (EditText) findViewById(R.id.confpass);
        parentLayout = findViewById(android.R.id.content);
        btn = (Button) findViewById(R.id.submit);
    }

    public boolean isValidFormData() {
        String edtstrnewpass = newpass.getText().toString();
        String edtstrconfpass = confpass.getText().toString();

        //Pattern pattern1 = Pattern.compile("[a-zA-Z0-9\\\\!\\\\@\\\\#\\\\$]{8,20}");

        //Matcher matcher1 = pattern1.matcher(edtstrnewpass);

        if (TextUtils.isEmpty(old.getText().toString())) {
            old.setError("Plz enter Old Password.");
            old.requestFocus();
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
    // Triggers when LOGIN Button clicked
    public void update(View arg0) {
        if(isValidFormData()) {
            // Get text from email and passord field
            final String r1 = confpass.getText().toString().trim();
            final String r2 = old.getText().toString().trim();
            //final String email = getIntent().getStringExtra("EMAIL");
            final String email = Global.email;

            // Initialize  AsyncLogin() class with email and password
            new ChangePassword.AsyncLogin().execute(r1, r2, email);
        }
    }

    private class AsyncLogin extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(ChangePassword.this);
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
                url = new URL(Connecttodb.path+"chpass.php");

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
                        .appendQueryParameter("pass",params[0])
                        .appendQueryParameter("oldpass",params[1])
                        .appendQueryParameter("intentemail",params[2]);

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

            if(result.equalsIgnoreCase("successfully updated"))
            {
                /* Here launching another activity when login successful. If you persist login state
                use sharedPreferences of Android. and logout button to clear sharedPreferences.
                 */
                Snackbar snackbar = Snackbar.make(parentLayout, "Successfully Saved", Snackbar.LENGTH_LONG);
                snackbar.show();


            }else if (result.equalsIgnoreCase("Invalid Old Password..!!")){
                Snackbar snackbar = Snackbar.make(parentLayout, "Invalid Old Password..!!", Snackbar.LENGTH_LONG);
                snackbar.show();
                // If username and password does not match display a error message

            }else if (result.equalsIgnoreCase("oops! Please try again!")){
                Snackbar snackbar = Snackbar.make(parentLayout, "oops! Please try again!", Snackbar.LENGTH_LONG);
                snackbar.show();

            } else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {
                Snackbar snackbar = Snackbar.make(parentLayout, "OOPs! Something went wrong. Connection Problem.", Snackbar.LENGTH_LONG);
                snackbar.show();

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