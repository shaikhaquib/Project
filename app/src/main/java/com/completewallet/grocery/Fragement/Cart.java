package com.completewallet.grocery.Fragement;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.completewallet.grocery.Activity.Credentials;
import com.completewallet.grocery.Activity.DataVar;
import com.completewallet.grocery.Activity.Global;
import com.completewallet.grocery.Activity.LoginActivity;
import com.completewallet.grocery.Activity.OrderHistoryActivity;
import com.completewallet.grocery.Activity.Product;
import com.completewallet.grocery.Adapter.CartAdapter;
import com.completewallet.grocery.Adapter.Holder;
import com.completewallet.grocery.Checkout;
import com.completewallet.grocery.Connecttodb;
import com.completewallet.grocery.R;
import com.completewallet.grocery.SessionManager;
import com.completewallet.grocery.SpacesItemDecoration;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter;

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
import java.util.List;

import static android.support.design.widget.Snackbar.LENGTH_LONG;

/**
 * Created by Shaikh Aquib on 10-Apr-18.
 */

public class Cart extends Fragment {

    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private String[] strArrData = {"No Suggestions"};
    public Context context;
    public TextView txttotal,loginlink , checkout;
    public String email , responce;
    View parentLayout;
    public Credentials CData;
    public RecyclerView recyclerView;
    LinearLayout guestlayout ;
    SessionManager manager ;
    RequestQueue queue;
    int total= 0;
    boolean login =true ;
    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.cartview,container,false);

        queue = Volley.newRequestQueue(getActivity());
        manager=new SessionManager(getActivity());
        guestlayout = view.findViewById(R.id.guesusererror);
        recyclerView=view.findViewById(R.id.cartRecycler);
        txttotal = view.findViewById(R.id.carttotal);
        checkout =view.findViewById(R.id.checkout);
        loginlink = view.findViewById(R.id.loginlink);


/*
        if (responce.isEmpty()){
            checkout.setVisibility(View.GONE);
        }
*/

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getActivity() , Checkout.class);
                intent.putExtra("array",responce);
                intent.putExtra("total",total);
                startActivity(intent);
            }
        });

        if (manager.isSkip()){
            login=false;
        }else {
            login=true;
        }

        if (manager.isSkip()){
            guestlayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        loginlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.setLogin(false);
                manager.setSkip(false);
                SharedPreferences login = getActivity().getSharedPreferences("login", getActivity().MODE_PRIVATE);
                SharedPreferences.Editor deditor = login.edit();
                deditor.clear();
                deditor.commit();
                startActivity(new Intent(getActivity(),LoginActivity.class));
                getActivity().finish();
            }
        });
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        context = getActivity();
        /*SharedPreferences shared =this.getActivity().getSharedPreferences("login", context.MODE_PRIVATE);
        Global.email = shared.getString( "email", "");*/
        email = Global.email;
        parentLayout = view.findViewById(android.R.id.content);
        new Cart.ProductFetch().execute(email);

        return view;
    }

    private void checkoutservice() {
        //Intent intent=new Intent(getActivity() , )
    }

    public class ProductFetch extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(context);
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

                // Enter URL address where your json file resides
                url = new URL(Connecttodb.path+"getcartitem.php");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
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
                        .appendQueryParameter("email",params[0]);

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
                return e1.toString();
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
                return e.toString();
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {
            //this method will be running on UI thread
            responce = result;

            ArrayList<String> dataList = new ArrayList<String>();

            pdLoading.dismiss();
            List<DataVar> data=new ArrayList<>();

            pdLoading.dismiss();
            try {

                JSONArray jArray = new JSONArray(result);

                // Extract data from json and store into ArrayList as class objects
                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);
                    dataList.add(json_data.getString("cart_id"));
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
                    price=price*wt;

                    total = price + total;
                    EData.cartcustomer_id = json_data.getString("customer_id");
                    data.add(EData);
                }

                txttotal.setText(String.valueOf("Total Price :- ₹. "+total));
                strArrData = dataList.toArray(new String[dataList.size()]);

                // Setup and Handover data to recyclerview
                recyclerView.setAdapter(new CartAdapter(context,data,login));


            } catch (JSONException e) {
                Snackbar snackbar = Snackbar.make(view, "No Item In Cart Or Please Check Internet Connection !", LENGTH_LONG);
                snackbar.show();
                    checkout.setVisibility(View.GONE);
                //Toast.makeText(context, "No Item In Cart Or Please Check Internet Connection", Toast.LENGTH_LONG).show();
            }

        }

    }

}
