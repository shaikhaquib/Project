package com.abatechnology.kirana2door.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.abatechnology.kirana2door.Adapter.MainAdapt;
import com.abatechnology.kirana2door.Adapter.RewardAdapter;
import com.abatechnology.kirana2door.Adapter.ViewPagerAdapter;
import com.abatechnology.kirana2door.Connecttodb;
import com.abatechnology.kirana2door.R;
import com.abatechnology.kirana2door.Service.MyNotificationService;
import com.abatechnology.kirana2door.SpacesItemDecoration;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter;
import com.yarolegovich.discretescrollview.transform.Pivot;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class Category extends AppCompatActivity {
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    public RecyclerView rvcategory;
    private String[] strArrData = {"No Suggestions"};
    View parentLayout;
    String[] imagearray ;
    RequestQueue queue;
    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;
    String currentversion;

    DiscreteScrollView RewardView;
    private InfiniteScrollAdapter infiniteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        setTitle("Category..");

        queue= Volley.newRequestQueue(this);
        multipleimage();
        update();
        SharedPreferences shared =getSharedPreferences("login", MODE_PRIVATE);
        Global.email = shared.getString( "email", "");
        Global.password = shared.getString("password","");
        startService(new Intent(getApplicationContext(),MyNotificationService.class));
        RewardView = findViewById(R.id.offereSlider);
        parentLayout = findViewById(android.R.id.content);

        infiniteAdapter = InfiniteScrollAdapter.wrap(new RewardAdapter(getApplicationContext(), imagearray));
        RewardView.setAdapter(infiniteAdapter);
        RewardView.setItemTransformer(new ScaleTransformer.Builder()
                .setMaxScale(1.05f)
                .setMinScale(0.8f)
                .setPivotX(Pivot.X.CENTER) // CENTER is a default one
                .setPivotY(Pivot.Y.BOTTOM) // CENTER is a default one
                .build());

        //MainRecycler=findViewById(R.id.mainlist);
        rvcategory=findViewById(R.id.mainlist);
        rvcategory.setNestedScrollingEnabled(false);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.activity_horzontal);
        rvcategory.setNestedScrollingEnabled(false);
        rvcategory.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        rvcategory.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        //MainRecycler.setAdapter(new MainAdapt(getApplicationContext(),img1));
        new AsyncFetch().execute();



    }

    private void update() {
        final StringRequest request = new StringRequest(StringRequest.Method.POST, Connecttodb.path + "checkversion", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                    currentversion = pInfo.versionName;
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

                try {

                    JSONObject jsonObject =new JSONObject(response);
                    double newverison = Double.parseDouble(jsonObject.getString("version"));
                    double verison = Double.parseDouble(currentversion);
                    final String link = jsonObject.getString("link");

                    if (verison < newverison){
                        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(Category.this);
                        View mView = layoutInflaterAndroid.inflate(R.layout.updateapp, null);
                        android.app.AlertDialog.Builder alertDialogBuilderUserInput = new android.app.AlertDialog.Builder(Category.this);
                        alertDialogBuilderUserInput.setView(mView);
                        TextView button=mView.findViewById(R.id.UpdApp);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse(link));
                                startActivity(intent);
                            }
                        });
                        final android.app.AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
                        alertDialogAndroid.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        alertDialogAndroid.setCancelable(false);
                        alertDialogAndroid.show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Category.this, "Connection problem !", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String ,String> map = new HashMap<>();
                map.put("product_id","2");
                return map;
            }
        };
        queue.add(request);
    }

    public void slider(){ viewPager = (ViewPager) findViewById(R.id.catviewPager);

        sliderDotspanel = (LinearLayout) findViewById(R.id.catSliderDots);

        if (imagearray.length<2){

            sliderDotspanel.setVisibility(View.GONE );
        }

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this,imagearray);

        viewPager.setAdapter(viewPagerAdapter);

        dotscount = viewPagerAdapter.getCount();
        dots = new ImageView[dotscount];

        for(int i = 0; i < dotscount; i++){

            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);

            sliderDotspanel.addView(dots[i], params);

        }

//        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for(int i = 0; i< dotscount; i++){
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new MyTimerTask(), 2000, 4000);}
    private class AsyncFetch extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(Category.this);
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
                url = new URL(Connecttodb.path+"getcategory");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            try {

                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");

                // setDoOutput to true as we recieve data from json file
                conn.setDoOutput(true);

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
                    dataList.add(json_data.getString("category_id"));
                    DataVar EData = new DataVar();
                    EData.category_id = json_data.getString("category_id");
                    EData.category_name = json_data.getString("category_name");
                    EData.category_img = json_data.getString("category_img");
                    data.add(EData);
                }
                strArrData = dataList.toArray(new String[dataList.size()]);

                // Setup and Handover data to recyclerview
                rvcategory.setAdapter(new MainAdapt(Category.this,data));


            } catch (JSONException e) {
                Snackbar snackbar = Snackbar.make(parentLayout, "Connection Problem! OR No Internet Connection !", Snackbar.LENGTH_LONG);

                snackbar.show();
                //Toast.makeText(Category.this, "Please Check Internet Connection", Toast.LENGTH_LONG).show();
            }

        }

    }
    public class MyTimerTask extends TimerTask {

        @Override
        public void run() {

            Category.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if(viewPager.getCurrentItem() == 0){
                        viewPager.setCurrentItem(1);
                    } else if(viewPager.getCurrentItem() == 1){
                        viewPager.setCurrentItem(2);
                    } else if(viewPager.getCurrentItem() == 2){
                        viewPager.setCurrentItem(3);
                    } else if(viewPager.getCurrentItem() == 3){
                        viewPager.setCurrentItem(4);
                    }else if(viewPager.getCurrentItem() == 4){
                        viewPager.setCurrentItem(5);
                    } else if(viewPager.getCurrentItem() == 5){
                        viewPager.setCurrentItem(0);
                    }


                }
            });

        }
    }

    private void multipleimage() {
        final StringRequest request = new StringRequest(StringRequest.Method.POST, Connecttodb.path + "getmultiplebanner", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                ArrayList<String> dataList = new ArrayList<String>();

                try {
                    JSONArray array =new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject json_data = array.getJSONObject(i);
                        dataList.add(json_data.getString("banner_img"));

                    }
                    imagearray=dataList.toArray(new String[dataList.size()]);
                    slider();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Category.this, "Connection problem !", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String ,String> map = new HashMap<>();
                map.put("product_id","2");
                return map;
            }
        };
        queue.add(request);
    }

}
