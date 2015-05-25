package de.fau.cs.mad.fablab.android.cart;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.rest.core.CartEntry;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ProductViewHolder> {

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        private TextView cart_product_name;
        private ImageView cart_product_photo;
        private TextView cart_product_price;
        private Spinner cart_product_quantity_spinner;
        private TextView cart_product_quantity;


        ProductViewHolder(View itemView) {
            super(itemView);

            this.cart_product_quantity = (TextView) itemView.findViewById(R.id.cart_product_quantity);
            this.cart_product_quantity_spinner = (Spinner) itemView.findViewById(R.id.cart_product_quantity_spinner);
            this.cart_product_name = (TextView) itemView.findViewById(R.id.cart_product_name);
            this.cart_product_photo = (ImageView) itemView.findViewById(R.id.cart_product_photo);
            this.cart_product_price = (TextView) itemView.findViewById(R.id.cart_product_price);
        }
    }

    private List<CartEntry> products;
    private Context context;


    public RecyclerViewAdapter(Context context ,List<CartEntry> products) {
        this.context = context;
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
    public void onBindViewHolder(final ProductViewHolder productViewHolder, final int i) {
        productViewHolder.cart_product_name.setText(products.get(i).getName());
        String formated_price = String.format("%.2f", products.get(i).getPrice());
        productViewHolder.cart_product_price.setText(formated_price +
                Html.fromHtml(productViewHolder.itemView.getResources().getString(R.string.non_breaking_space)) +
                Html.fromHtml(productViewHolder.itemView.getResources().getString(R.string.currency)));
        productViewHolder.cart_product_photo.setImageResource(R.drawable.no_image_avl);

        // Set quantity text
        productViewHolder.cart_product_quantity.setText(
                productViewHolder.itemView.getResources().getString(R.string.cart_product_quantity) +
                Html.fromHtml(productViewHolder.itemView.getResources().getString(R.string.non_breaking_space)) +
                Html.fromHtml(productViewHolder.itemView.getResources().getString(R.string.italic_start)) +
                products.get(i).getUnit() +
                Html.fromHtml(productViewHolder.itemView.getResources().getString(R.string.italic_end)) +
                productViewHolder.itemView.getResources().getString(R.string.colon));

        List<String> list = new ArrayList<String>();
        // TODO: adapt this later to the available amount of the product
        int qty = (int) products.get(i).getAmount();
        int start = 1;
        if(qty-500 > 1)
            start = qty-500;

        for(int j=start;j<qty+500;j++){
            list.add(String.valueOf(j));
        }

        ArrayAdapter<String> aa=new ArrayAdapter<String>(context,R.layout.cart_entry_quantity_spinner_item, list);
        aa.setDropDownViewResource(R.layout.cart_entry_quantity_spinner_dropdown);

        productViewHolder.cart_product_quantity_spinner.setAdapter(aa);

        productViewHolder.cart_product_quantity_spinner.setSelection(qty - 1);

        // Set up listener for quantity change
        productViewHolder.cart_product_quantity_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (Integer.parseInt((String)parent.getSelectedItem()) != (int) products.get(i).getAmount()) {
                    double amount_new = Double.parseDouble(((String) parent.getSelectedItem()).replaceAll("\\s+", ""));
                    products.get(i).setAmount(amount_new);
                    Cart.MYCART.refresh();
                    Cart.MYCART.updateVisibility();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent){

            }
        });
    }
}