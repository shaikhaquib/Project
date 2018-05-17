package com.completewallet.grocery.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.completewallet.grocery.Connecttodb;
import com.completewallet.grocery.R;
import com.completewallet.grocery.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProductReview extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<DataVar>list=new ArrayList<>();
    RequestQueue queue;
    FloatingActionButton actionButton;
    SessionManager manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productreview);
         recyclerView=findViewById(R.id.rvReview);
         queue= Volley.newRequestQueue(this);
         actionButton = findViewById(R.id.addreview);
         manager = new SessionManager(this);
         actionButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 if (manager.isSkip()){  AlertDialog.Builder builder;
                     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                         builder = new AlertDialog.Builder(ProductReview.this);
                     } else {
                         builder = new AlertDialog.Builder(ProductReview.this);
                     }
                     builder.setTitle("Sorry ! please login first")
                             .setMessage("guest user this feature not available for guest user")
                             .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                                 public void onClick(DialogInterface dialog, int which) {
                                     // continue with delete
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
                 Intent intent = new Intent(context,AddReview.class);
                 intent.putExtra("product_id",getIntent().getStringExtra("product_id"));
                 startActivity(intent);}
             }
         });
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        Reviewlist();
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

                        list.add(dataVar);

                    }
                    recyclerView.setAdapter(new RecyclerView.Adapter() {
                        @Override
                        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(ProductReview.this).inflate(R.layout.reviewitem,parent,false);
                            return new ViewHolder(view) {};
                        }

                        @Override
                        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                            ViewHolder myHolder= (ViewHolder) holder;
                            final DataVar current=list.get(position);

                            myHolder.name.setText(current.id);
                            myHolder.ratingBar.setProgress(Integer.parseInt(current.rating));
                            myHolder.comment.setText(current.comment);
                            myHolder.time.setText(current.time);
                        }

                        @Override
                        public int getItemCount() {
                            return list.size();
                        }
                          class  ViewHolder extends RecyclerView.ViewHolder{
                              RatingBar ratingBar;
                              TextView name,comment,time;
                              public ViewHolder(View itemView) {
                                  super(itemView);
                                  ratingBar=itemView.findViewById(R.id.myRatingBar);
                                  name=itemView.findViewById(R.id.rename);
                                  comment=itemView.findViewById(R.id.reComment);
                                  time=itemView.findViewById(R.id.retime);
                              }
                          }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProductReview.this, "Connection problem !", Toast.LENGTH_SHORT).show();
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
}
