package com.completewallet.grocery.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.completewallet.grocery.R;


/**
 * Created by Shaikh Aquib on 29-Mar-18.
 */

public class RewardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    Context context;
    String[] img;

    public RewardAdapter(Context applicationContext, String[] img) {
       this.context = applicationContext;
        this.img = img;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.reawardadapt, parent, false);
        return new ViewHolder(v);    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder myViewHolder=(ViewHolder) holder;
        Glide.with(context).load(img[position]).into(myViewHolder.image);

    }

    @Override
    public int getItemCount() {
        return img.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.imgReward);
        }
    }
}
