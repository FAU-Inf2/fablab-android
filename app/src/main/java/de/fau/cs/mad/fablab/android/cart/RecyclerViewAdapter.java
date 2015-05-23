package de.fau.cs.mad.fablab.android.cart;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.rest.core.CartEntry;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ProductViewHolder> {

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        private TextView cart_product_name;
        private TextView cart_product_quantity;
        private ImageView cart_product_photo;
        private TextView cart_product_price;


        ProductViewHolder(View itemView) {
            super(itemView);

            this.cart_product_name = (TextView) itemView.findViewById(R.id.cart_product_name);
            this.cart_product_quantity = (TextView) itemView.findViewById(R.id.cart_product_quantity);
            this.cart_product_photo = (ImageView) itemView.findViewById(R.id.cart_product_photo);
            this.cart_product_price = (TextView) itemView.findViewById(R.id.cart_product_price);
        }
    }

    private List<CartEntry> products;


    public RecyclerViewAdapter(List<CartEntry> products) {
        this.products = products;
    }


    public void addProduct(CartEntry product) {
        for(int i = 0; i< products.size(); i++){
            if(products.get(i).getProductId() == product.getProductId()){
                products.get(i).setAmount(products.get(i).getAmount() + 1);
                notifyItemChanged(i);
                return;
            }
        }
        products.add(product);
        notifyItemInserted(getItemCount()-1);
    }

    public void clear() {
        products.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_entry, viewGroup, false);
        ProductViewHolder pvh = new ProductViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(ProductViewHolder productViewHolder, int i) {
        productViewHolder.cart_product_name.setText(products.get(i).getName());
        productViewHolder.cart_product_price.setText(String.valueOf(products.get(i).getPrice()));
        productViewHolder.cart_product_photo.setImageResource(R.drawable.no_image_avl);
        productViewHolder.cart_product_quantity.setText(String.valueOf(products.get(i).getAmount()) +
                Html.fromHtml(productViewHolder.itemView.getResources().getString(R.string.non_breaking_space)) +
                products.get(i).getUnit());
    }
}