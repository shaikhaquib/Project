package com.abatechnology.kirana2door.Fragement;

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
import android.util.Log;
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
import com.abatechnology.kirana2door.Activity.Credentials;
import com.abatechnology.kirana2door.Activity.DataVar;
import com.abatechnology.kirana2door.Activity.Global;
import com.abatechnology.kirana2door.Activity.LoginActivity;
import com.abatechnology.kirana2door.Activity.OrderHistoryActivity;
import com.abatechnology.kirana2door.Activity.Product;
import com.abatechnology.kirana2door.Adapter.CartAdapter;
import com.abatechnology.kirana2door.Adapter.Holder;
import com.abatechnology.kirana2door.Checkout;
import com.abatechnology.kirana2door.Connecttodb;
import com.abatechnology.kirana2door.R;
import com.abatechnology.kirana2door.SessionManager;
import com.abatechnology.kirana2door.SpacesItemDecoration;
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
    List<DataVar> data=new ArrayList<>();
    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.cartview,container,false);

        queue = Volley.newRequestQueue(getActivity());
        manager=new SessionManager(getActivity());
        guestlayout = view.findViewById(R.id.guesusererror);

        recyclerView=view.findViewById(R.id.cartRecycler);

        loginlink = view.findViewById(R.id.loginlink);
        txttotal = view.findViewById(R.id.carttotal);
        checkout =view.findViewById(R.id.checkout);

/*
        if (responce.isEmpty()){
            checkout.setVisibility(View.GONE);
        }
*/



        if (manager.isSkip()){
            login=false;
        }else {
            login=true;
        }

        if (manager.isSkip()){
            guestlayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            checkout.setVisibility(View.GONE);
            txttotal.setVisibility(View.GONE);
        }else {
            parentLayout = view.findViewById(android.R.id.content);
            new Cart.ProductFetch().execute(Global.email);
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


        return view;
    }

    private void checkoutservice() {
        //Intent intent=new Intent(getActivity() , )
    }

    public class ProductFetch extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(getActivity());
        HttpURLConnection conn;
        URL url = null;
        /*Context ctxt;
        View v;
        public ProductFetch(Context context, View v) {
            this.ctxt =context;
            this.v = v;
        }*/

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
                url = new URL(Connecttodb.path+"getcartitem");

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

            pdLoading.dismiss();


            checkout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent =new Intent(getActivity() , Checkout.class);
                    intent.putExtra("array",responce);
                    intent.putExtra("total",total);
                    startActivity(intent);
                }
            });

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


                if(data != null && !data.isEmpty()) {
                    //has items here. The fact that has items does not mean that the items are != null.
                    //You have to check the nullity for every item
                    Log.d("True","Not Empty");
                    txttotal.setText(String.valueOf("Total Price :- ₹. "+total));
                    strArrData = dataList.toArray(new String[dataList.size()]);
                    //imagearray[0] = jsonObject.getString("product_image");
                }
                else {
                    // either there is no instance of ArrayList in arrayList or the list is empty.
                    TextView textView =view.findViewById(R.id.empcart);
                    textView.setVisibility(View.VISIBLE);
                    checkout.setVisibility(View.GONE);
                    txttotal.setVisibility(View.GONE);
                }


                // Setup and Handover data to recyclerview
                //recyclerView.setAdapter(new CartAdapter(context,data,login,v));
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
