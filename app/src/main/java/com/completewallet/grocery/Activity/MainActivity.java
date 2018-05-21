package com.completewallet.grocery.Activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.completewallet.grocery.BottomNavigationHelper;
import com.completewallet.grocery.Connecttodb;
import com.completewallet.grocery.CustomerRegisterActivity;
import com.completewallet.grocery.Fragement.Alert;
import com.completewallet.grocery.Fragement.Cart;
import com.completewallet.grocery.Fragement.Home;
import com.completewallet.grocery.Fragement.Menu;
import com.completewallet.grocery.R;
import com.completewallet.grocery.Service.MyNotificationService;

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

public class MainActivity extends AppCompatActivity {
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    View parentLayout;
    Home home;
    Alert alert;
    Cart cart;
    Menu menu;
    public String catid;
    BottomNavigationView bottomNavigationView;
    MenuItem prevMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        if(getIntent().getExtras() != null){
            catid = getIntent().getStringExtra("category_id");
            Global.cateid=catid.trim();

        }else{
            Global.cateid="1";
        }

        SharedPreferences shared =getSharedPreferences("login", MODE_PRIVATE);
        Global.email = shared.getString( "email", "");
        Global.password = shared.getString("password","");

        parentLayout = findViewById(android.R.id.content);
        bottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation);
        BottomNavigationHelper.disableShiftMode(bottomNavigationView);

        Bottomnav();
    }

    private void Bottomnav() {
        if (bottomNavigationView != null) {

            // Select first menu item by default and show Fragment accordingly.
            android.view.Menu menu = bottomNavigationView.getMenu();
            selectFragment(menu.getItem(0));

            // Set action to perform when any menu-item is selected.
            bottomNavigationView.setOnNavigationItemSelectedListener(
                    new BottomNavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                            selectFragment(item);
                            return false;
                        }
                    });
        }

    }
    protected void selectFragment(MenuItem item) {

        item.setChecked(true);

        switch (item.getItemId()) {
            case R.id.navigation_home:
                // Action to perform when Home Menu item is selected.
                /*Bundle bundle = new Bundle();
                bundle.putString("catid", catid);
                Home tabe = new Home();
                tabe.setArguments(bundle);*/
                pushFragment(new Home());
                break;
            case R.id.navigation_alert:
                // Action to perform when Bag Menu item is selected.
                pushFragment(new Alert());
                break;
            case R.id.navigation_cart:
                // Action to perform when Account Menu item is selected.

                pushFragment(new Cart());
                break;
            case R.id.navigation_menu:
                // Action to perform when Account Menu item is selected.
                pushFragment(new Menu());
                break;
        }
    }
    protected void pushFragment(Fragment fragment) {
        if (fragment == null)
            return;

        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            if (ft != null) {
                ft.replace(R.id.rootLayout, fragment);
                ft.commit();
            }
        }
    }

    public class AddToCart extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL(Connecttodb.path+"addtocart.php");

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
                        .appendQueryParameter("product_id",params[0])
                        .appendQueryParameter("qty",params[1])
                        .appendQueryParameter("customer_email",params[2]);

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

            if(result.equalsIgnoreCase("successfully added")){
                //Snackbar.make(getWindow().getDecorView().getRootView(), "Product Successfully Added To Cart !", Snackbar.LENGTH_LONG).show();
                //Snackbar.make(MainActivity.this, "Product Successfully Added To Cart !", Snackbar.LENGTH_LONG).show();
                //Home fragment = (Home) getFragmentManager().findFragmentById(R.id.home);
                //fragment.success();
            } else if (result.equalsIgnoreCase("oops! Please try again!")){
                //Home fragment = (Home) getFragmentManager().findFragmentById(R.id.home);
                //fragment.tryagain();
            } else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {
                //Home fragment = (Home) getFragmentManager().findFragmentById(R.id.home);
                //fragment.error();
            }
        }

    }
    public class RemoveFromCart extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL(Connecttodb.path+"deletefromcart.php");

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
                        .appendQueryParameter("cart_id",params[0]);

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

            if(result.equalsIgnoreCase("successfully removed")){
                //Snackbar.make(getWindow().getDecorView().getRootView(), "Product Successfully Added To Cart !", Snackbar.LENGTH_LONG).show();
                //Snackbar.make(MainActivity.this, "Product Successfully Added To Cart !", Snackbar.LENGTH_LONG).show();
                //Home fragment = (Home) getFragmentManager().findFragmentById(R.id.home);
                //fragment.success();
            } else if (result.equalsIgnoreCase("oops! Please try again!")){
                //Home fragment = (Home) getFragmentManager().findFragmentById(R.id.home);
                //fragment.tryagain();
            } else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {
                //Home fragment = (Home) getFragmentManager().findFragmentById(R.id.home);
                //fragment.error();
            }
        }

    }
}
