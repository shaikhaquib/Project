package com.completewallet.grocery.Adapter;

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

import com.bumptech.glide.Glide;
import com.completewallet.grocery.Activity.CartHolder;
import com.completewallet.grocery.Activity.DataVar;
import com.completewallet.grocery.Activity.MainActivity;
import com.completewallet.grocery.Activity.BuyNow;
import com.completewallet.grocery.R;

        import java.util.ArrayList;
import java.util.List;

public class CartAdapter  extends  RecyclerView.Adapter<RecyclerView.ViewHolder>  {
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

    public CartAdapter(Context context, List<DataVar> data, boolean login){
        this.context=context;
        //inflater= LayoutInflater.from(context);
        this.data=data;
        this.login=login;
        //SessionManager manager = new SessionManager(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view=LayoutInflater.from(context).inflate(R.layout.cartitem, parent,false);
        CartHolder holder=new CartHolder(view);
        return holder;    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // Get current position of item in recyclerview to bind data and assign values from list
        final CartHolder myHolder= (CartHolder) holder;
        final DataVar current=data.get(position);

        myHolder.productname.setText(current.cartproduct_name);
        myHolder.description.setText(current.cartproduct_discription_1);
        current.multicartprice= Integer.parseInt(current.cartproduct_price);
        current.multicartweight= Integer.parseInt(current.cartproduct_weight);
        current.multicartqty= Integer.parseInt(current.cartqty);
        current.multicartprice = current.multicartprice * current.multicartqty;
        current.multicartweight = current.multicartweight * current.multicartqty;

        //myHolder.price.setText("₹. "+current.cartproduct_price+" "+"/"+" "+current.cartproduct_weight+current.cartunits);
        myHolder.price.setText("₹. "+current.multicartprice+" "+"/"+" "+current.multicartweight+current.cartunits);
        myHolder.mrp.setText("₹."+current.cartproduct_mrp+" "+"/"+" "+current.cartproduct_weight+current.cartunits);


        myHolder.cquantity.setText("Quantity :- "+current.cartqty);
        //  myHolder.quantity.setText(current.product_weight);

        myHolder.removefromcart.setTag(current);

        Glide.with(context).load(current.cartproduct_image).into(myHolder.proimg);


       /* myHolder.serItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                Intent intent = new Intent(context,Category.class);
                intent.putExtra("category_id",current.product_id);
                Toast.makeText(context, ""+current.product_id, Toast.LENGTH_SHORT).show();
                context.startActivity(intent);
            }
        });*/
        myHolder.buynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!login){  AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(context);
                    } else {
                        builder = new AlertDialog.Builder(context);
                    }
                    builder.setTitle("Sorry ! please login first")
                            .setMessage("This feature not available for guest user")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    //SessionManager
                                    //context.startActivity(new Intent(context,LoginActivity.class));
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //  startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                                }
                            })
                            .show();}else {
                    Intent intent = new Intent(context, BuyNow.class);
                    intent.putExtra("product_id", current.cartproduct_id);
                    intent.putExtra("quantity", current.cartqty);
                    intent.putExtra("name", current.cartproduct_name);
                    intent.putExtra("price", current.multicartprice);
                    context.startActivity(intent);
                }
            }
        });

        myHolder.removefromcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity outerObject = new MainActivity();
                MainActivity.RemoveFromCart innerObject = outerObject.new RemoveFromCart();
                //MainActivity.new AddToCart().execute(current.product_id,current.product_weight,"qwerty@gmail.com");
                innerObject.execute(current.cart_id);
                Snackbar snackbar = Snackbar.make(view, "Product Successfully Removed From Cart !"+current.cart_id, Snackbar.LENGTH_LONG);
                snackbar.show();
                Intent intent = new Intent(context,MainActivity.class);
                intent.putExtra("category_id","1");
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
