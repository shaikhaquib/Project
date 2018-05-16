package com.completewallet.grocery.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.completewallet.grocery.Activity.DataVar;
import com.completewallet.grocery.Activity.ItemClickListener;
import com.completewallet.grocery.Activity.OrderHistoryHolder;
import com.completewallet.grocery.Activity.SubOrderHistory;
import com.completewallet.grocery.Activity.SubOrderHolder;
import com.completewallet.grocery.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class SubOrderAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private Context context;
    private LayoutInflater inflater;
    List<DataVar> data= Collections.emptyList();
    DataVar current;
    int currentPos=0;
    int[] img;

    public SubOrderAdapter(Context context, List<DataVar> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.activity_sub_order_adapter, parent,false);
        SubOrderHolder holder=new SubOrderHolder(view);
        return holder;    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // Get current position of item in recyclerview to bind data and assign values from list
        SubOrderHolder myHolder= (SubOrderHolder) holder;
        final DataVar current=data.get(position);
        myHolder.productname.setText(current.orderproduct_name);
        myHolder.price.setText("Price :- ₹. "+current.order_product_price);
        myHolder.qty.setText("Product Quantity :- "+current.quantity);
        Glide.with(context).load(current.orderproduct_image).into(myHolder.ProductImage);
    }

    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }
    public void setFilter(List ListModels) {
        data = new ArrayList<>();
        data.addAll(ListModels);
        notifyDataSetChanged();
    }

}
