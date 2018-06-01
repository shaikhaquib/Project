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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.completewallet.grocery.Connecttodb;
import com.completewallet.grocery.CustomerRegisterActivity;
import com.completewallet.grocery.R;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.ganfra.materialspinner.MaterialSpinner;

public class AccountUpdate extends AppCompatActivity {
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private EditText fname,lname, email, phno, pass,conpass,add,pin;
    MaterialSpinner spinner;
    private Button btn;
    UserData outerObject;
    ArrayAdapter<String> adapter;
    View parentLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_update);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        spinner =  findViewById(R.id.Circle);
        String[] ITEMS = {"Mumbai", "Thane", "Panvel", "Navi Mumbai", "Kalyan", "Uran"};
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ITEMS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        fname = (EditText) findViewById(R.id.first);
        lname = (EditText) findViewById(R.id.last);
        add = (EditText) findViewById(R.id.address);
        pin = (EditText) findViewById(R.id.pin);
        email = (EditText) findViewById(R.id.email);
        phno = (EditText) findViewById(R.id.contact);
        btn = (Button) findViewById(R.id.btnreg);
        parentLayout = findViewById(android.R.id.content);
        new AccountUpdate.UserInfo().execute(Global.email);
    }



    public boolean isValidFormData() {
        String edtstremail = email.getText().toString();
        String edtstrphno = phno.getText().toString();
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
        if (!matcher4.matches()) {
            pin.setError("Plz enter valid Pincode.");
            pin.requestFocus();
            return false;
        }
        return true;
    }
    public void UpdateData(View arg0) {
        if (isValidFormData()) {

            final String a1 = fname.getText().toString().trim();
            final String a2 = lname.getText().toString().trim();
            final String a3 = add.getText().toString().trim();
            final String a4 = pin.getText().toString().trim();
            final String a5 = email.getText().toString().trim();
            final String a6 = phno.getText().toString().trim();
            final String a7 = spinner.getSelectedItem().toString().trim();
            new AccountUpdate.UpdateCustomer().execute(a1,a2,a3,a4,a5,a6,a7);
        }
    }
    private class UpdateCustomer extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(AccountUpdate.this);
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
                url = new URL(Connecttodb.path+"updatecustomer");

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
                        .appendQueryParameter("pin",params[3])
                        .appendQueryParameter("email",params[4])
                        .appendQueryParameter("contact",params[5])
                        .appendQueryParameter("city",params[6]);

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
                Snackbar snackbar = Snackbar.make(parentLayout, "Successfully Updated !", Snackbar.LENGTH_LONG);
                snackbar.show();

            }else if (result.equalsIgnoreCase("please fill all values")){
                Snackbar snackbar = Snackbar.make(parentLayout, "Please fill all values !", Snackbar.LENGTH_LONG);
                snackbar.show();
            } else if (result.equalsIgnoreCase("oops! Please try again!")){
                Snackbar snackbar = Snackbar.make(parentLayout, "oops! Please try again !", Snackbar.LENGTH_LONG);
                snackbar.show();
            } else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {
                Snackbar snackbar = Snackbar.make(parentLayout, "Connection Problem! OR No Internet Connection !", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }

    }
    public class UserInfo extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(AccountUpdate.this);
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
                    fname.setText((json_data.getString("first_name")));
                    lname.setText((json_data.getString("last_name")));
                    add.setText((json_data.getString("address")));
                    pin.setText((json_data.getString("pincode")));
                    email.setText((json_data.getString("email_id")));
                    phno.setText((json_data.getString("contact_no")));
                    int spinnerPosition = adapter.getPosition((json_data.getString("city")).trim());
                    spinner.setSelection(spinnerPosition+1);
                }
            } catch (JSONException e) {

            }

        }

        @Override
        protected void onCancelled() {

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
