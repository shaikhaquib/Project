package com.abatechnology.kirana2door.Activity;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.abatechnology.kirana2door.R;

public class OrderHistoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView orderid,total,status,date;
    ItemClickListener itemClickListener;


    // create constructor to get widget reference
    public OrderHistoryHolder(View itemView) {
        super(itemView);
        orderid= (TextView) itemView.findViewById(R.id.orderid);
        total= (TextView) itemView.findViewById(R.id.total);
        status= (TextView) itemView.findViewById(R.id.status);
        date= (TextView) itemView.findViewById(R.id.date);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        this.itemClickListener.onItemClick(v,getLayoutPosition());
    }
    public void serItemClickListener(ItemClickListener ic)
    {
        this.itemClickListener=ic;
    }

}
