package com.completewallet.grocery.Adapter;

import android.content.Context;
        import android.content.Intent;
        import android.support.design.widget.Snackbar;
        import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Toast;

        import com.bumptech.glide.Glide;
import com.completewallet.grocery.Activity.CartHolder;
import com.completewallet.grocery.Activity.Category;
        import com.completewallet.grocery.Activity.DataVar;
        import com.completewallet.grocery.Activity.ItemClickListener;
        import com.completewallet.grocery.Activity.MainActivity;
        import com.completewallet.grocery.Activity.MyHolder;
        import com.completewallet.grocery.Activity.Product;
        import com.completewallet.grocery.Activity.ProductHolder;
        import com.completewallet.grocery.CustomerRegisterActivity;
        import com.completewallet.grocery.R;

        import java.util.ArrayList;
        import java.util.Collections;
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

    public CartAdapter(Context context, List<DataVar> data){
        this.context=context;
        //inflater= LayoutInflater.from(context);
        this.data=data;
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
        myHolder.price.setText("₹. "+current.cartproduct_price+" "+"/"+" "+current.cartproduct_weight+current.cartunits);
        myHolder.mrp.setText("₹."+current.cartproduct_mrp+" "+"/"+" "+current.cartproduct_weight+current.cartunits);
        //  myHolder.quantity.setText(current.product_weight);

        myHolder.quantity.setText(current.cartqty);
        myHolder.plus.setTag(current);
        myHolder.minus.setTag(current);
        myHolder.quantity.setTag(current);
        myHolder.removefromcart.setTag(current);

        Glide.with(context).load(current.cartproduct_image).into(myHolder.proimg);
        wt = Float.valueOf(current.cartqty);

       /* myHolder.serItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                Intent intent = new Intent(context,Category.class);
                intent.putExtra("category_id",current.product_id);
                Toast.makeText(context, ""+current.product_id, Toast.LENGTH_SHORT).show();
                context.startActivity(intent);
            }
        });*/


        myHolder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                float qt = Float.valueOf(current.cartminimum_quantity);
                wt = wt + qt;

                String s = Float.toString(wt);
                myHolder.quantity.setText(s);
            }
        });
        myHolder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float qt = Float.valueOf(current.cartminimum_quantity);
                if(wt>0){
                    wt = wt - qt;
                }

                String s = Float.toString(wt);
                myHolder.quantity.setText(s);
            }
        });
        myHolder.removefromcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity outerObject = new MainActivity();
                MainActivity.AddToCart innerObject = outerObject.new AddToCart();
                //MainActivity.new AddToCart().execute(current.product_id,current.product_weight,"qwerty@gmail.com");
                innerObject.execute(current.product_id,current.product_weight,"qwerty@gmail.com");
                Snackbar snackbar = Snackbar.make(view, "Product Successfully Added To Cart !", Snackbar.LENGTH_LONG);
                snackbar.show();
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
