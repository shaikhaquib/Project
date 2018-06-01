package com.completewallet.grocery.Fragement;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.completewallet.grocery.Activity.DataVar;
import com.completewallet.grocery.Activity.Global;
import com.completewallet.grocery.Activity.Product;
import com.completewallet.grocery.Adapter.ProductAdapter;
import com.completewallet.grocery.Adapter.RewardAdapter;
import com.completewallet.grocery.R;
import com.completewallet.grocery.SessionManager;
import com.completewallet.grocery.SpacesItemDecoration;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter;
import com.yarolegovich.discretescrollview.transform.Pivot;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;
import fr.ganfra.materialspinner.MaterialSpinner;

import android.widget.Spinner;
import android.widget.Toast;
import com.completewallet.grocery.Connecttodb;
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

public class Home extends Fragment {
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private String[] strArrData = {"No Suggestions"};
    public Context context;
    public String category;
    View parentLayout;
    DiscreteScrollView RewardView, RecentTranaction;
    public RecyclerView MainRecycler;
    SessionManager manager;
    boolean login =true ;
    private InfiniteScrollAdapter infiniteAdapter, infRecent;
    View rootview;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.home, container, false);
        context = getActivity();
        manager =new SessionManager(getActivity());

        if (manager.isSkip()){
            login=false;
        }else {
            login=true;
        }

        category = Global.cateid;
        parentLayout = rootview.findViewById(android.R.id.content);

        Intialization(rootview);

        return rootview;
    }


    private void Intialization(View rootview) {
        MainRecycler=rootview.findViewById(R.id.homelist);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.activity_horzontal);


//        Spinner spinner =  rootview.findViewById(R.id.postpaidoperator);
//        String[] ITEMS = {"Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6"};
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, ITEMS);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        spinner.setAdapter(adapter);

        MainRecycler.setNestedScrollingEnabled(false);
        MainRecycler.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        MainRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        new ProductFetch().execute(category);

    }

    private class ProductFetch extends AsyncTask<String, String, String> {
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
                url = new URL(Connecttodb.path+"productlistbypid");

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
                        .appendQueryParameter("categoryid",params[0]);

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
            ArrayList<String> dataList = new ArrayList<String>();

            pdLoading.dismiss();
            List<DataVar> data=new ArrayList<>();

            pdLoading.dismiss();
            try {

                JSONArray jArray = new JSONArray(result);

                // Extract data from json and store into ArrayList as class objects
                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);
                    dataList.add(json_data.getString("product_id"));
                    DataVar EData = new DataVar();
                    EData.product_id = json_data.getString("product_id");
                    EData.product_name = json_data.getString("product_name");
                    EData.hsn_code = json_data.getString("hsn_code");
                    EData.product_discription_1 = json_data.getString("product_discription_1");
                    EData.product_discription_2 = json_data.getString("product_discription_2");
                    EData.product_image = json_data.getString("product_image");
                    EData.gst_id = json_data.getString("gst_id");
                    EData.pcategory_id = json_data.getString("category_id");
                    EData.product_price = json_data.getString("product_price");
                    EData.product_mrp = json_data.getString("product_mrp");
                    EData.product_weight = json_data.getString("product_weight");
                    EData.minimum_quantity = json_data.getString("minimum_quantity");
                    EData.minimum = Integer.parseInt(json_data.getString("minimum_quantity"));
                    EData.units = json_data.getString("units");
                    EData.status = json_data.getString("status");
                    data.add(EData);
                }
                strArrData = dataList.toArray(new String[dataList.size()]);

                // Setup and Handover data to recyclerview
                MainRecycler.setAdapter(new ProductAdapter(context,data,login));


            } catch (JSONException e) {
                Snackbar snackbar = Snackbar.make(rootview, "No Product Available In This Category !", LENGTH_LONG);

                snackbar.show();
                //Toast.makeText(context, "No Product Available In This Category !", Toast.LENGTH_LONG).show();
            }

        }

    }
}