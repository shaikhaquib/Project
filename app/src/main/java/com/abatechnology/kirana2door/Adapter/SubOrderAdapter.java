package com.abatechnology.kirana2door.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.abatechnology.kirana2door.Activity.DataVar;
import com.abatechnology.kirana2door.Activity.ItemClickListener;
import com.abatechnology.kirana2door.Activity.OrderHistoryHolder;
import com.abatechnology.kirana2door.Activity.SubOrderHistory;
import com.abatechnology.kirana2door.Activity.SubOrderHolder;
import com.abatechnology.kirana2door.R;

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
        current.mul_current_product_weight = Integer.parseInt(current.current_product_weight);
        current.mul_current_product_qty = Integer.parseInt(current.quantity);
        current.mul_current_product_weight = current.mul_current_product_weight * current.mul_current_product_qty;
        current.current_product_weight = String.valueOf(current.mul_current_product_weight);
        myHolder.price.setText("Price :- ₹. "+current.order_product_price+" / "+current.current_product_weight+current.current_product_unit);
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
