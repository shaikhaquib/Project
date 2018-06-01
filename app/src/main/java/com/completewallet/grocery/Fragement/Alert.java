package com.completewallet.grocery.Fragement;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.completewallet.grocery.Activity.DataVar;
import com.completewallet.grocery.Activity.Global;
import com.completewallet.grocery.Connecttodb;
import com.completewallet.grocery.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Shaikh Aquib on 10-Apr-18.
 */

public class Alert extends Fragment {

    RecyclerView recyclerView;
    RequestQueue queue;
    ProgressDialog dialog;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
       View view =inflater.inflate(R.layout.alert,container,false);

       Global.notiCount=0;
       recyclerView = view.findViewById(R.id.rvalert);
       queue = Volley.newRequestQueue(getActivity());
       dialog=new ProgressDialog(getActivity());
       dialog.setMessage("Loading...");
       dialog.setCancelable(false);

       alertService();
       return view;
    }

    private void alertService() {
        dialog.show();
        StringRequest request =new StringRequest(StringRequest.Method.POST, Connecttodb.path + "orderhistory", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();

                final List<DataVar> data=new ArrayList<>();
                try {

                    JSONArray jArray = new JSONArray(response);

                    // Extract data from json and store into ArrayList as class objects
                    for(int i=0;i<jArray.length();i++){
                        JSONObject json_data = jArray.getJSONObject(i);
                        DataVar EData = new DataVar();
                        EData.order_id = json_data.getString("order_id");
                        EData.order_status = json_data.getString("order_status");
                        EData.status_change_time = json_data.getString("status_change_time");
                        data.add(EData);
                    }
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView.setAdapter(new RecyclerView.Adapter() {
                        @Override
                        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(getActivity()).inflate(R.layout.alertlayout,parent,false);
                            return new ViewHolder(view);
                        }

                        @Override
                        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                            ViewHolder myHolder= (ViewHolder) holder;
                            final DataVar current=data.get(position);

                            myHolder.id.setText("Order "+current.order_id);
                            myHolder.comment.setText("Your order has been "+current.order_status);
                            myHolder.time.setText(current.status_change_time);
                        }

                        @Override
                        public int getItemCount() {
                            return data.size();
                        }
                        class  ViewHolder extends RecyclerView.ViewHolder{
                            RatingBar ratingBar;
                            TextView id,comment,time;
                            public ViewHolder(View itemView) {
                                super(itemView);
                                id =itemView.findViewById(R.id.alertid);
                                comment=itemView.findViewById(R.id.alertmessage);
                                time=itemView.findViewById(R.id.alertime);
                            }
                        }
                    });



                } catch (JSONException e) {}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String ,String> map = new HashMap<>();
                map.put("email", Global.email);
                return map;
            }

        };
        queue.add(request);
    }
}
