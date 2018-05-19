package com.completewallet.grocery.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.completewallet.grocery.Activity.DataVar;
import com.completewallet.grocery.Activity.ItemClickListener;
import com.completewallet.grocery.Activity.MainActivity;
import com.completewallet.grocery.Activity.MyHolder;
import com.completewallet.grocery.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Shaikh Aquib on 16-Apr-18.
 */

public class MainAdapt extends  RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private Context context;
    private LayoutInflater inflater;
    List<DataVar> data= Collections.emptyList();
    DataVar current;
    int currentPos=0;
    int[] img;

    public MainAdapt(Context context, List<DataVar> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.category, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // Get current position of item in recyclerview to bind data and assign values from list
        MyHolder myHolder= (MyHolder) holder;
        final DataVar current=data.get(position);
        myHolder.CategoryName.setText(current.category_name);
        Glide.with(context).load(current.category_img).into(myHolder.CategoryImage);

        myHolder.serItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                Intent intent = new Intent(context,MainActivity.class);
                intent.putExtra("category_id",current.category_id);
                context.startActivity(intent);
            }
        });
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
