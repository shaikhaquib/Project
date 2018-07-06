package com.abatechnology.kirana2door.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.abatechnology.kirana2door.Activity.DataVar;
import com.abatechnology.kirana2door.Activity.ItemClickListener;
import com.abatechnology.kirana2door.Activity.MainActivity;
import com.abatechnology.kirana2door.Activity.MyHolder;
import com.abatechnology.kirana2door.Activity.OrderHistoryHolder;
import com.abatechnology.kirana2door.Activity.SubOrderHistory;
import com.abatechnology.kirana2door.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class OrderHistoryAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private Context context;
    private LayoutInflater inflater;
    List<DataVar> data= Collections.emptyList();
    DataVar current;
    int currentPos=0;
    int[] img;

    public OrderHistoryAdapter(Context context, List<DataVar> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.activity_order_history_adapter, parent,false);
        OrderHistoryHolder holder=new OrderHistoryHolder(view);
        return holder;    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // Get current position of item in recyclerview to bind data and assign values from list
        OrderHistoryHolder myHolder= (OrderHistoryHolder) holder;
        final DataVar current=data.get(position);
        myHolder.orderid.setText(current.order_id);
        myHolder.total.setText("Total :- ₹. "+current.final_ammount);
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(current.order_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String newstr = new SimpleDateFormat("dd/MM/yy, hh:mm aa").format(date);
        myHolder.date.setText("Timestamp :- "+newstr);
        myHolder.status.setText("Order Status :- "+current.order_status);

        myHolder.serItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                Intent intent = new Intent(context,SubOrderHistory.class);
                intent.putExtra("orderid",current.order_id);
                intent.putExtra("custid",current.customer_id);
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
