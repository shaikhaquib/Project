package com.abatechnology.kirana2door.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.abatechnology.kirana2door.Connecttodb;
import com.abatechnology.kirana2door.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddReview extends AppCompatActivity {

    RatingBar ratingBar;
    EditText editText;
    Button button;
    RequestQueue queue;
    String channel;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review);

        queue= Volley.newRequestQueue(this);
        ratingBar = findViewById(R.id.addmyRatingBar);
        editText =findViewById(R.id.addcomm);
        button = findViewById(R.id.adreSub);

        dialog=new ProgressDialog(AddReview.this);
        dialog.setMessage("Loading....");
        SharedPreferences shared = getSharedPreferences("login", MODE_PRIVATE);
         channel = (shared.getString( "email", ""));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().isEmpty()){
                    editText.setError("Please write something !");
                }else {
                    addReview();
                }
            }
        });
    }

    private void addReview() {
        dialog.show();

        final StringRequest request = new StringRequest(StringRequest.Method.POST, Connecttodb.path + "newreview", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Toast.makeText(AddReview.this, response, Toast.LENGTH_LONG).show();

                if (response.equals("Thanks for your review")){
                    Context context=getApplicationContext();
                    Intent intent = new Intent(context,ProductReview.class);
                    intent.putExtra("product_id",getIntent().getStringExtra("product_id"));
                    startActivity(intent);
                    finish();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(AddReview.this, "Connection problem !", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String ,String> map = new HashMap<>();
                map.put("product_id",getIntent().getStringExtra("product_id"));
                map.put("customer_id",channel);
                map.put("rating", String.valueOf(ratingBar.getProgress()));
                map.put("comment",editText.getText().toString());

                return map;
            }
        };
        queue.add(request);

    }

    @Override
    public void onBackPressed() {
        Context context=getApplicationContext();
        Intent intent = new Intent(context,ProductReview.class);
        intent.putExtra("product_id",getIntent().getStringExtra("product_id"));
        startActivity(intent);
    }
}
