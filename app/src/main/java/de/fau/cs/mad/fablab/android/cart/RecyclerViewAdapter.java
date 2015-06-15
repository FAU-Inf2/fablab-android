package de.fau.cs.mad.fablab.android.cart;


import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
        private Button cart_product_undo;
        private TextView cart_product_removed;
        private LinearLayout ll;
        private ImageView cart_product_undo_img;



        ProductViewHolder(View itemView) {
            super(itemView);

            this.ll = (LinearLayout) itemView.findViewById(R.id.cart_entry_undo);
            this.cart_product_removed = (TextView) itemView.findViewById(R.id.cart_product_removed);
            this.cart_product_undo = (Button) itemView.findViewById(R.id.cart_product_undo);
            this.cart_product_quantity = (TextView) itemView.findViewById(R.id.cart_product_quantity);
            this.cart_product_quantity_spinner = (Spinner) itemView.findViewById(R.id.cart_product_quantity_spinner);
            this.cart_product_name = (TextView) itemView.findViewById(R.id.cart_product_name);
            this.cart_product_photo = (ImageView) itemView.findViewById(R.id.cart_product_photo);
            this.cart_product_price = (TextView) itemView.findViewById(R.id.cart_product_price);
            this.cart_product_undo_img = (ImageView) itemView.findViewById(R.id.cart_product_undo_img);
        }
    }

    private List<CartEntry> products;
    private Context context;


    public RecyclerViewAdapter(Context context ,List<CartEntry> products) {
        this.context = context;
        this.products = products;
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
        if(CartSingleton.MYCART.getIsProductRemoved().get(i) == true){
            productViewHolder.ll.setVisibility(View.VISIBLE);
        }else{
            productViewHolder.ll.setVisibility(View.GONE);
        }

        String[] name = products.get(i).getProduct().getName().split(" ");

        String product_name = "";
        for(int k=0;k<name.length;k++){
            if(k == 0){
                product_name += productViewHolder.itemView.getResources().getString(R.string.bold_start) +
                        name[k] +
                        productViewHolder.itemView.getResources().getString(R.string.bold_end) +
                        productViewHolder.itemView.getResources().getString(R.string.non_breaking_space);
            }else{
                product_name +=  name[k] +
                        productViewHolder.itemView.getResources().getString(R.string.non_breaking_space);
            }
        }

        productViewHolder.cart_product_name.setText(Html.fromHtml(product_name));
        String formated_price = String.format("%.2f", products.get(i).getProduct().getPrice());
        productViewHolder.cart_product_price.setText(formated_price +
                Html.fromHtml(productViewHolder.itemView.getResources().getString(R.string.non_breaking_space)) +
                Html.fromHtml(productViewHolder.itemView.getResources().getString(R.string.currency)));
        productViewHolder.cart_product_photo.setImageResource(R.drawable.no_image_avl);
        productViewHolder.cart_product_removed.setText(productViewHolder.itemView.getResources().getString(R.string.cart_product_removed));
        productViewHolder.cart_product_undo.setText(productViewHolder.itemView.getResources().getString(R.string.cart_product_undo));


        // check the availability of attribute "unit" from product
        String italic_unit = productViewHolder.itemView.getResources().getString(R.string.cart_product_quantity) +
                    productViewHolder.itemView.getResources().getString(R.string.non_breaking_space) +
                    productViewHolder.itemView.getResources().getString(R.string.italic_start) +
                    products.get(i).getProduct().getUnit() +
                    productViewHolder.itemView.getResources().getString(R.string.italic_end) +
                    productViewHolder.itemView.getResources().getString(R.string.colon);



        // Set unit text/quantity title
        productViewHolder.cart_product_quantity.setText(Html.fromHtml(italic_unit));

        List<String> list = new ArrayList<>();
        // TODO: adapt this later to the available amount of the product
        int qty = (int) products.get(i).getAmount();
        int start = 1;
        if(qty-500 > 1)
            start = qty-500;

        for(int j=start;j<qty+500;j++){
            list.add(String.valueOf(j));
        }

        ArrayAdapter<String> aa=new ArrayAdapter<>(context,R.layout.cart_entry_quantity_spinner_item, list);
        aa.setDropDownViewResource(R.layout.cart_entry_quantity_spinner_dropdown);

        productViewHolder.cart_product_quantity_spinner.setAdapter(aa);

        productViewHolder.cart_product_quantity_spinner.setSelection(qty - 1);

        // Set up listener for quantity change
        productViewHolder.cart_product_quantity_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (Integer.parseInt((String) parent.getSelectedItem()) != (int) products.get(i).getAmount()) {
                    double amount_new = Double.parseDouble(((String) parent.getSelectedItem()).replaceAll("\\s+", ""));
                    products.get(i).setAmount(amount_new);
                    CartSingleton.MYCART.updateVisibility();
                    CartSingleton.MYCART.updateProducts(i);
                    CartSingleton.MYCART.refresh();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Set up undo button listener
        productViewHolder.cart_product_undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartSingleton.MYCART.addRemovedProduct(i);
                CartSingleton.MYCART.getIsProductRemoved().set(i, false);
                productViewHolder.ll.setVisibility(View.GONE);
                productViewHolder.ll.setClickable(false);
            }
        });

        // Set up undo img click listener (same behavior as undo button)
        productViewHolder.cart_product_undo_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productViewHolder.cart_product_undo.performClick();
            }
        });

        // Set up click listener
        CardView cv = (CardView) productViewHolder.itemView;
        cv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }

            // recognize long press to differentiate between swipe/long press and click
            final GestureDetector gestureDetector = new GestureDetector(new GestureDetector.OnGestureListener() {
                @Override
                public boolean onDown(MotionEvent e) {
                    return true;
                }

                @Override
                public void onShowPress(MotionEvent e) {

                }

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    int x = (int) e.getRawX();
                    int y = (int) e.getRawY();

                    // show / hide full product title
                    if(!inViewInBounds(productViewHolder.cart_product_quantity_spinner, x, y) &&
                            productViewHolder.ll.getVisibility() == View.GONE){
                        if(productViewHolder.cart_product_name.getLineCount() == 1){
                            productViewHolder.cart_product_name.setSingleLine(false);
                        }else{
                            productViewHolder.cart_product_name.setSingleLine(true);
                        }
                    }
                    return true;
                }

                @Override
                public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                    return false;
                }

                @Override
                public void onLongPress(MotionEvent e) {

                }

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    return false;
                }
            });

            // Check whether pos(x,y) is in View
            public boolean inViewInBounds(View view, int x, int y){
                Rect outRect = new Rect();
                int[] location = new int[2];
                view.getDrawingRect(outRect);
                view.getLocationOnScreen(location);
                outRect.offset(location[0], location[1]);
                return outRect.contains(x, y);
            }
        });
    }
}