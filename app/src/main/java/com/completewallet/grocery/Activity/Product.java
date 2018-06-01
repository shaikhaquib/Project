package com.completewallet.grocery.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.completewallet.grocery.Adapter.ViewPagerAdapter;
import com.completewallet.grocery.Connecttodb;
import com.completewallet.grocery.R;
import com.completewallet.grocery.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class Product extends AppCompatActivity {

    int minteger = 1;
    ViewPager viewPager;
    RatingBar rating;
    LinearLayout sliderDotspanel;
    private int dotscount;
    ImageView imageView;
    private ImageView[] dots;
    RequestQueue queue ;
    SessionManager manager;
    ArrayList<DataVar> list=new ArrayList<>();
    boolean login =true ;
    TextView title , desription,description2 , mrp  ,price ,quantity ,reviewcount;
    CheckBox minus ,plus, addtocart;
    String minimum_quantity,amount ,unit ,s ;
    String[] imagearray ;
    Button Buynow;
    int totalrating;
    int count = 0,wt ;
    View parentLayout;

    TextView review;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        parentLayout = findViewById(android.R.id.content);
        queue= Volley.newRequestQueue(this);

        review = findViewById(R.id.productreviews);
        manager =new SessionManager(Product.this);
        imageView=findViewById(R.id.prdimg);

        if (manager.isSkip()){
            login=false;
        }else {
            login=true;
        }

        quantity = findViewById(R.id.prquantity);
        minus =findViewById(R.id.prminus);
        plus = findViewById(R.id.replus);
        title =findViewById(R.id.prtitle);
        addtocart =findViewById(R.id.addtocartp);
        desription = findViewById(R.id.prdesc);
        description2=findViewById(R.id.prdesc2);
        mrp=findViewById(R.id.prmrp);
        Buynow=findViewById(R.id.prdBuynow);
        manager=new SessionManager(this);
        quantity.setText(String.valueOf(minteger));
        rating=findViewById(R.id.prRatingBar);
        //  stock=findViewById(R.id.prstock);
        price=findViewById(R.id.prprice);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context=getApplicationContext();
                Intent intent = new Intent(context,ProductReview.class);
                intent.putExtra("product_id",getIntent().getStringExtra("product_id"));

                startActivity(intent);
            }
        });
        Buynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (manager.isSkip()){  AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(Product.this);
                    } else {
                        builder = new AlertDialog.Builder(Product.this);
                    }
                    builder.setTitle("Sorry ! please login first")
                            .setMessage("You have logged is as guest user Please Login to buy Product")
                            .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    manager.setLogin(false);
                                    manager.setSkip(false);
                                    startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //  startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                                }
                            })
                            .show();}else {

                Context context=getApplicationContext();
                Intent intent = new Intent(context,BuyNow.class);
                intent.putExtra("product_id",getIntent().getStringExtra("product_id"));
                intent.putExtra("quantity",quantity.getText().toString());
                intent.putExtra("name",title.getText().toString());
                intent.putExtra("price",mrp.getText().toString());

                startActivity(intent);}
            }
        });

        multipleimage();
        productDetail();

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(minteger<99) {
                    minteger = minteger + 1;
                    quantity.setText(String.valueOf(minteger));
                    int amt = Integer.parseInt(amount);
                    int cal2 = amt * minteger;
                    int cal3 = wt * minteger;
                    s = String.valueOf(cal2);
                   // calculatedprice.setText(s);
                    mrp.setText("Price:  ₹"+s+" /"+ cal3 +" "+ unit);
                }

            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(minteger>1) {
                    minteger = minteger - 1;
                    quantity.setText(String.valueOf(minteger));
                    int amt = Integer.parseInt(amount);
                    int cal2 = amt * minteger;
                    int cal3 = wt * minteger;
                    s = String.valueOf(cal2);
                    // calculatedprice.setText(s);
                    mrp.setText("Price: ₹."+s+" /"+ cal3 +" "+ unit);
                }
                else {
                    quantity.setText("1");
                }

            }
        });
        addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!login){  AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(Product.this);
                    } else {
                        builder = new AlertDialog.Builder(Product.this);
                    }
                    builder.setTitle("Sorry ! please login first")
                            .setMessage("This feature not available for guest user")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    //SessionManager
                                    //context.startActivity(new Intent(context,LoginActivity.class));
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //  startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                                }
                            })
                            .show();}else {

                    MainActivity outerObject = new MainActivity();
                    MainActivity.AddToCart innerObject = outerObject.new AddToCart();
                    //MainActivity.new AddToCart().execute(current.product_id,current.product_weight,"qwerty@gmail.com");

                /*if(myHolder.quantity.getText().toString().trim() == "1"){
                    innerObject.execute(current.product_id,"1",email);
                }else{*/
                    innerObject.execute(getIntent().getStringExtra("product_id"),quantity.getText().toString().trim(),Global.email);
                    //}
                    Snackbar snackbar = Snackbar.make(parentLayout, "Product Successfully Added To Cart !", Snackbar.LENGTH_LONG);
                    snackbar.show();}
            }
        });

        Reviewlist();

        if (count >0){
        }

    }

    private void productDetail() {



        final StringRequest request = new StringRequest(StringRequest.Method.POST, Connecttodb.path + "productdetail.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    JSONObject jsonObject = array.getJSONObject(0);

                    title.setText(jsonObject.getString("product_name"));
                    quantity.setText("1");
                   // count = 1;
                    minimum_quantity = jsonObject.getString("product_weight");
                    unit=jsonObject.getString("units");
                    mrp.setText("Price: ₹ "+jsonObject.getString("product_price")+" /" +jsonObject.getString("product_weight")+" " + jsonObject.getString("units"));
                    desription.setText(jsonObject.getString("product_discription_1"));
                    description2.setText(jsonObject.getString("product_discription_2"));
                   // strprice = Float.parseFloat(jsonObject.getString("product_price"));
                    amount = (jsonObject.getString("product_price"));
                 //   stock.setText(jsonObject.getString("status"));
                    wt = Integer.parseInt(jsonObject.getString("product_weight"));
                    price.setText("₹"+jsonObject.getString("product_mrp")+" /" +jsonObject.getString("product_weight")+" " + jsonObject.getString("units"));
                    Glide.with(getApplicationContext()).load(jsonObject.getString("product_image")).into(imageView);


                    if(imagearray != null && imagearray.length > 0) {
                        //has items here. The fact that has items does not mean that the items are != null.
                        //You have to check the nullity for every item
                        Log.d("True","Not Empty");
                    }
                    else {
                        // either there is no instance of ArrayList in arrayList or the list is empty.
                        viewPager.setVisibility(View.GONE);
                        imageView.setVisibility(View.VISIBLE);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Product.this, "Connection problem !", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap <String ,String> map = new HashMap<>();
                map.put("product_id",getIntent().getStringExtra("product_id"));
                return map;
            }
        };
        queue.add(request);
    }

    private void Slider() {

        sliderDotspanel = (LinearLayout) findViewById(R.id.SliderDots);

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

        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

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
        timer.scheduleAtFixedRate(new MyTimerTask(), 2000, 4000);
    }
    public class MyTimerTask extends TimerTask {

        @Override
        public void run() {

            Product.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if(viewPager.getCurrentItem() == 0){
                        viewPager.setCurrentItem(1);
                    } else if(viewPager.getCurrentItem() == 1){
                        viewPager.setCurrentItem(2);
                    } else {
                        viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
                    }

                }
            });

        }
    }
    public void Reviewlist(){
        final StringRequest request = new StringRequest(StringRequest.Method.POST, Connecttodb.path + "reviewlist.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);

                    for (int i = 0 ; i< array.length(); i++){
                        DataVar dataVar = new DataVar();
                        JSONObject jsonObject = array.getJSONObject(i);
                        dataVar.id=jsonObject.getString("customer_id");
                        dataVar.rating=jsonObject.getString("rating");
                        dataVar.comment=jsonObject.getString("comment");
                        dataVar.time=jsonObject.getString("time");
                        count++;
                        totalrating= Integer.valueOf(jsonObject.getString("rating"))+totalrating;
                        System.out.println("avgrating="+totalrating/count);
                        rating.setProgress(totalrating/count);
                       review.setText(String.valueOf(i+1)+" Review");
                        Log.d(String.valueOf(totalrating), String.valueOf(count));
                        list.add(dataVar);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Product.this, "Connection problem !", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String ,String> map = new HashMap<>();
                map.put("product_id",getIntent().getStringExtra("product_id"));
                return map;
            }
        };
        queue.add(request);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                break;
        } return true;
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        intent.putExtra("category_id",Global.cateid);
        startActivity(intent);
    finish();}

    private void multipleimage() {
        final StringRequest request = new StringRequest(StringRequest.Method.POST, Connecttodb.path + "getmultipleimage.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                ArrayList<String> dataList = new ArrayList<String>();

                try {
                    JSONArray array =new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject json_data = array.getJSONObject(i);
                        dataList.add(json_data.getString("product_img"));

                    }
                    imagearray=dataList.toArray(new String[dataList.size()]);
                    Slider();

                } catch (JSONException e) {
                    e.printStackTrace();
                   /* viewPager.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);*/
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Product.this, "Connection problem !", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap <String ,String> map = new HashMap<>();
                map.put("product_id",getIntent().getStringExtra("product_id"));
                return map;
            }
        };
        queue.add(request);
    }

}
