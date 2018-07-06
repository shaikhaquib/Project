package com.abatechnology.kirana2door.Activity;

        import android.support.v7.widget.RecyclerView;
        import android.view.View;
        import android.widget.Button;
        import android.widget.CheckBox;
        import android.widget.ImageView;
        import android.widget.TextView;

        import com.abatechnology.kirana2door.R;

public class CartHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView productname,description,price,mrp,cquantity;
    public CheckBox minus,plus,removefromcart;
    public Button buynow;
    public ImageView proimg;
    ItemClickListener itemClickListener;


    // create constructor to get widget reference
    public CartHolder(View itemView) {
        super(itemView);
        productname= (TextView) itemView.findViewById(R.id.productname);
        description= (TextView) itemView.findViewById(R.id.description);
        price= (TextView) itemView.findViewById(R.id.price);
        mrp= (TextView) itemView.findViewById(R.id.mrp);
        cquantity= (TextView) itemView.findViewById(R.id.cartqty);
        removefromcart= (CheckBox) itemView.findViewById(R.id.removefromcart);
        buynow= (Button) itemView.findViewById(R.id.buynow);
        proimg = (ImageView) itemView.findViewById(R.id.proimg);
        /*test=itemView.findViewById(R.id.testtext);
        checkBox=itemView.findViewById(R.id.test);*/
        //fitemView.setOnClickListener(this);

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