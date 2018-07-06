package com.abatechnology.kirana2door.Activity;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.abatechnology.kirana2door.R;

/**
 * Created by Bhushan on 12-Mar-17.
 */
public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    String categoryid;
    public TextView CategoryName;
    ItemClickListener itemClickListener;
    public ImageView CategoryImage;


    // create constructor to get widget reference
    public MyHolder(View itemView) {
        super(itemView);
        CategoryName= (TextView) itemView.findViewById(R.id.categoryname);
        CategoryImage = (ImageView) itemView.findViewById(R.id.categoryimg);
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