package com.completewallet.grocery.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.completewallet.grocery.Activity.Category;
import com.completewallet.grocery.Activity.Credentials;
import com.completewallet.grocery.Activity.DataVar;
import com.completewallet.grocery.Activity.ItemClickListener;
import com.completewallet.grocery.Activity.LoginActivity;
import com.completewallet.grocery.Activity.MainActivity;
import com.completewallet.grocery.Activity.MyHolder;
import com.completewallet.grocery.Activity.Product;
import com.completewallet.grocery.Activity.ProductHolder;
import com.completewallet.grocery.CustomerRegisterActivity;
import com.completewallet.grocery.R;
import com.completewallet.grocery.SessionManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class ProductAdapter  extends  RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    //ProductHolder myHolder;
    float wt;
    int i=0;
    public View view;
    public Credentials CData;
    private Context context;
    private LayoutInflater inflater;
    List<DataVar> data;
    DataVar current;
    int currentPos=0;
    int[] img;
    String s;
    boolean login;

    public ProductAdapter(Context context, List<DataVar> data, boolean login){
        this.context=context;
        //inflater= LayoutInflater.from(context);
        this.data=data;
        this.login=login;
        //SessionManager manager = new SessionManager(context);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view=LayoutInflater.from(context).inflate(R.layout.listitem, parent,false);
        ProductHolder holder=new ProductHolder(view);
        return holder;    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // Get current position of item in recyclerview to bind data and assign values from list
        final ProductHolder myHolder= (ProductHolder) holder;
        final DataVar current=data.get(position);

        SharedPreferences shared = context.getSharedPreferences("login", MODE_PRIVATE);
        final String email = (shared.getString( "email", ""));
        myHolder.productname.setText(current.product_name);
        myHolder.description.setText(current.product_discription_1);
        myHolder.price.setText("₹. "+current.product_price+" "+"/"+" "+current.product_weight+current.units);
        myHolder.mrp.setText("₹."+current.product_mrp+" "+"/"+" "+current.product_weight+current.units);
      //  myHolder.quantity.setText(current.product_weight);


        myHolder.plus.setTag(current);
        myHolder.minus.setTag(current);
        myHolder.quantity.setTag(current);
        myHolder.addtocart.setTag(current);
    myHolder.viewdetails.setTag(current);

        Glide.with(context).load(current.product_image).into(myHolder.proimg);
        wt = Float.valueOf(current.product_weight);

       /* myHolder.serItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                Intent intent = new Intent(context,Category.class);
                intent.putExtra("category_id",current.product_id);
                Toast.makeText(context, ""+current.product_id, Toast.LENGTH_SHORT).show();
                context.startActivity(intent);
            }
        });*/


        myHolder.viewdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,Product.class);
                intent.putExtra("product_id",current.product_id);
                Toast.makeText(context, ""+current.product_id, Toast.LENGTH_SHORT).show();
                context.startActivity(intent);
            }
        });
        myHolder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                float qt = Float.valueOf(current.minimum_quantity);
                wt = wt + qt;

                 s = Float.toString(wt);
                myHolder.quantity.setText(s);
            }
        });
        myHolder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float qt = Float.valueOf(current.minimum_quantity);
                if(wt>0){
                    wt = wt - qt;
                }

                 s = Float.toString(wt);
                myHolder.quantity.setText(s);
            }
        });
        myHolder.addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!login){  AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(context);
                    } else {
                        builder = new AlertDialog.Builder(context);
                    }
                    builder.setTitle("Sorry ! please login first")
                            .setMessage("guest user this feature not available for guest user")
                            .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    context.startActivity(new Intent(context,LoginActivity.class));
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //  startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                                }
                            })
                            .show();}else {

                MainActivity outerObject = new MainActivity();
                MainActivity.AddToCart innerObject = outerObject.new AddToCart();
               //MainActivity.new AddToCart().execute(current.product_id,current.product_weight,"qwerty@gmail.com");
                innerObject.execute(current.product_id,s,email);
                Snackbar snackbar = Snackbar.make(view, "Product Successfully Added To Cart !", Snackbar.LENGTH_LONG);
                snackbar.show();}
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
