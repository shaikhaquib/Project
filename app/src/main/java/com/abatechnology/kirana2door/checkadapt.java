package com.abatechnology.kirana2door;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.abatechnology.kirana2door.Activity.BuyNow;
import com.abatechnology.kirana2door.Activity.CartHolder;
import com.abatechnology.kirana2door.Activity.DataVar;
import com.abatechnology.kirana2door.Activity.MainActivity;

import java.util.List;

/**
 * Created by Shaikh Aquib on 21-May-18.
 */

class checkadapt  extends  RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    //ProductHolder myHolder;
    float wt;
    int i=0;
    public View view;
    private Context context;
    private LayoutInflater inflater;
    List<DataVar> data;
    DataVar current;
    int currentPos=0;
    int[] img;
    boolean login;

    public checkadapt(Context context, List<DataVar> data){
        this.context=context;
        //inflater= LayoutInflater.from(context);
        this.data=data;
        //SessionManager manager = new SessionManager(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view=LayoutInflater.from(context).inflate(R.layout.checklayout, parent,false);
        Holder holder=new Holder(view);
        return holder;    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // Get current position of item in recyclerview to bind data and assign values from list
        final Holder myHolder= (Holder) holder;
        final DataVar current=data.get(position);

        myHolder.name.setText(current.cartproduct_name);
       // myHolder.description.setText(current.cartproduct_discription_1);
        myHolder.qty.setText(current.cartqty);
        myHolder.price.setText("₹ "+current.cartproduct_price);


    }

    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }

    class Holder extends RecyclerView.ViewHolder{

        TextView name,qty,price;
        public Holder(View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.prdname);
            qty=itemView.findViewById(R.id.prdqty);
            price=itemView.findViewById(R.id.prdprice);

        }
    }

}
