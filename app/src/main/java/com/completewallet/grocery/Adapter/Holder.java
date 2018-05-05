package com.completewallet.grocery.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.completewallet.grocery.R;

/**
 * Created by Shaikh Aquib on 23-Apr-18.
 */

public class Holder extends RecyclerView.ViewHolder{
    public ImageView imageView;
    public RelativeLayout viewBackground, viewForeground;

    public Holder(View itemView) {
        super(itemView);
        imageView=itemView.findViewById(R.id.proimg);
    }
}