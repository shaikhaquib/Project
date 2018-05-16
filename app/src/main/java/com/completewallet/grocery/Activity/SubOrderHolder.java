package com.completewallet.grocery.Activity;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.completewallet.grocery.R;

public class SubOrderHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView productname,qty,price;
    ItemClickListener itemClickListener;
    public ImageView ProductImage;

// create constructor to get widget reference
public SubOrderHolder(View itemView) {
        super(itemView);
    productname= (TextView) itemView.findViewById(R.id.productname);
        qty= (TextView) itemView.findViewById(R.id.qty);
    price= (TextView) itemView.findViewById(R.id.price);
    ProductImage = (ImageView) itemView.findViewById(R.id.proimg);

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
