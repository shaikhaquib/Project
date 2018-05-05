package com.completewallet.grocery.Activity;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.completewallet.grocery.R;

public class ProductHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView productname,description,price,mrp,quantity,test;
    public CheckBox checkBox;
    public CheckBox minus,plus,wish,addtocart,viewdetails;
    public Button buynow;
    public ImageView proimg;
    ItemClickListener itemClickListener;


    // create constructor to get widget reference
    public ProductHolder(View itemView) {
        super(itemView);
        productname= (TextView) itemView.findViewById(R.id.productname);
        description= (TextView) itemView.findViewById(R.id.description);
        price= (TextView) itemView.findViewById(R.id.price);
        mrp= (TextView) itemView.findViewById(R.id.mrp);
        quantity= (TextView) itemView.findViewById(R.id.quantity);
        minus= (CheckBox) itemView.findViewById(R.id.minus);
        plus= (CheckBox) itemView.findViewById(R.id.plus);
        wish= (CheckBox) itemView.findViewById(R.id.wish);
        addtocart= (CheckBox) itemView.findViewById(R.id.addtocart);
        viewdetails= (CheckBox) itemView.findViewById(R.id.viewdetails);
        buynow= (Button) itemView.findViewById(R.id.buynow);
        proimg = (ImageView) itemView.findViewById(R.id.proimg);
        /*test=itemView.findViewById(R.id.testtext);
        checkBox=itemView.findViewById(R.id.test);*/
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