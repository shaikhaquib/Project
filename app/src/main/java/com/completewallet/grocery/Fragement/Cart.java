package com.completewallet.grocery.Fragement;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.completewallet.grocery.Activity.Product;
import com.completewallet.grocery.Adapter.Holder;
import com.completewallet.grocery.R;
import com.completewallet.grocery.SessionManager;

/**
 * Created by Shaikh Aquib on 10-Apr-18.
 */

public class Cart extends Fragment {
    private int[] img1 = {
            R.drawable.img,
            R.drawable.img2,
            R.drawable.img3,
            R.drawable.img4,
            R.drawable.img5,
            R.drawable.img6,
            R.drawable.img7,
            R.drawable.img8,
            R.drawable.img9,
            R.drawable.img10
    };

    LinearLayout guestlayout ;
    SessionManager manager ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.cartview,container,false);

        manager=new SessionManager(getActivity());
        guestlayout = view.findViewById(R.id.guesusererror);
        RecyclerView recyclerView=view.findViewById(R.id.cartRecycler);

        if (manager.isSkip()){
            guestlayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                View v = inflater.inflate(R.layout.cartitem, parent, false);
                return new Holder(v);             }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                Holder myViewHolder=(Holder) holder;
                myViewHolder.imageView.setImageResource(img1[position]);
                myViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getActivity(), Product.class));
                    }
                });
            }

            @Override
            public int getItemCount() {
                return img1.length;
            }


        });

        return view;
    }
}
