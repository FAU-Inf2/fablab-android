package de.fau.cs.mad.fablab.android.basket;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
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
        private CardView card_view;
        private TextView basket_product_name;
        private TextView basket_product_quantity;
        private ImageView basket_product_photo;
        private TextView basket_product_price;


        ProductViewHolder(View itemView) {
            super(itemView);

            this.card_view = (CardView) itemView.findViewById(R.id.card_view);
            this.basket_product_name = (TextView) itemView.findViewById(R.id.basket_product_name);
            this.basket_product_quantity = (TextView) itemView.findViewById(R.id.basket_product_quantity);
            this.basket_product_photo = (ImageView) itemView.findViewById(R.id.basket_product_photo);
            this.basket_product_price = (TextView) itemView.findViewById(R.id.basket_product_price);
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
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.basket_product, viewGroup, false);
        ProductViewHolder pvh = new ProductViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(ProductViewHolder productViewHolder, int i) {
        productViewHolder.basket_product_name.setText(products.get(i).getName());
        productViewHolder.basket_product_price.setText(String.valueOf(products.get(i).getPrice()));
        productViewHolder.basket_product_photo.setImageResource(R.drawable.no_image_avl);
        productViewHolder.basket_product_quantity.setText(String.valueOf(products.get(i).getAmount()) +" " + products.get(i).getUnit());
    }
}