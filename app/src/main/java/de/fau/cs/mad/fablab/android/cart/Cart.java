package de.fau.cs.mad.fablab.android.cart;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.brnunes.swipeablerecyclerview.SwipeableRecyclerViewTouchListener;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.List;

import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.db.DatabaseHelper;
import de.fau.cs.mad.fablab.rest.core.CartEntry;
import de.fau.cs.mad.fablab.rest.core.Product;

public enum Cart {
    MYCART;

    private List<CartEntry> products;
    private List<Boolean> isProductRemoved;
    private List<CartEntry> removed_products;
    private RuntimeExceptionDao<CartEntry, Long> dao;
    private RecyclerViewAdapter adapter;
    private Context context;
    private View view;
    private SlidingUpPanelLayout mLayout;
    private boolean slidingUp;


    Cart(){
    }

    // initialization of the db - getting all products that are in the cart
    // call this method on startup activity
    public void init(Context context){
        dao = DatabaseHelper.getHelper(context).getCartEntryDao();
        products = dao.queryForAll();
        isProductRemoved = new ArrayList<>();
        for(int i=0;i<products.size();i++){
            isProductRemoved.add(false);
        }
        removed_products = new ArrayList<>();
    }

    // Setting up the view for every context c including the sliding up panel
    public void setSlidingUpPanel(Context c, View v, boolean slidingUp){
        this.context = c;
        this.view = v;
        this.slidingUp = slidingUp;
        // Set Layout and Recyclerview
        RecyclerView cart_rv = (RecyclerView) v.findViewById(R.id.cart_recycler_view);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        cart_rv.setLayoutManager(llm);
        cart_rv.setHasFixedSize(true);
        // Add Entries to view
        adapter = new RecyclerViewAdapter(this.context, products);
        cart_rv.setAdapter(adapter);

        // Set up listener to be able to swipe to left/right to remove items
        SwipeableRecyclerViewTouchListener swipeTouchListener =
                new SwipeableRecyclerViewTouchListener(cart_rv,
                        new SwipeableRecyclerViewTouchListener.SwipeListener() {
                            @Override
                            public boolean canSwipe(int position) {
                                return true;
                            }

                            @Override
                            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    View card = recyclerView.getChildAt(position);
                                    LinearLayout ll = (LinearLayout) card.findViewById(R.id.cart_entry_undo);
                                    LinearLayout ll_before = (LinearLayout) card.findViewById(R.id.product_view);
                                    if(isProductRemoved.get(position) == true){
                                    //if(ll.getVisibility() == View.VISIBLE){
                                        //if(removed_products.size() != 0)
                                            //removed_products.remove(0);
                                        //ll.setVisibility(View.GONE);
                                        ll_before.setClickable(true);
                                        ll.setClickable(false);
                                        Cart.MYCART.removeEntry(products.get(position));
                                        adapter.notifyItemRemoved(position);
                                        updateVisibility();
                                    }else{
                                        isProductRemoved.set(position, true);
                                        Cart.MYCART.addToRemovedProducts(products.get(position));
                                        ll_before.setClickable(false);
                                        ll.setClickable(true);
                                        //ll.setVisibility(View.VISIBLE);
                                        Log.i(("drin"),("drin"));
                                    }
                                }
                                refresh();

                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    View card = recyclerView.getChildAt(position);
                                    LinearLayout ll = (LinearLayout) card.findViewById(R.id.cart_entry_undo);
                                    LinearLayout ll_before = (LinearLayout) card.findViewById(R.id.product_view);

                                    if(ll.getVisibility() == View.VISIBLE){
                                        Cart.MYCART.removeEntry(products.get(position));
                                        //if(removed_products.size() != 0)
                                            //removed_products.remove(0);
                                        ll.setVisibility(View.GONE);
                                        ll_before.setClickable(true);
                                        ll.setClickable(false);
                                        adapter.notifyItemRemoved(position);
                                        adapter.notifyDataSetChanged();
                                        updateVisibility();
                                    }else{
                                        Cart.MYCART.addToRemovedProducts(products.get(position));
                                        ll_before.setClickable(false);
                                        ll.setVisibility(View.VISIBLE);
                                        ll.setClickable(true);
                                    }

                                }
                                refresh();
                            }
                        });

        cart_rv.addOnItemTouchListener(swipeTouchListener);


        // Set up listener to show more than just one line of the product name
        cart_rv.addOnItemTouchListener(new RecyclerItemClickListener(context, cart_rv, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position, MotionEvent e) {
                TextView product_title = (TextView) view.findViewById(R.id.cart_product_name);

                Spinner cart_product_quantity_spinner = (Spinner) view.findViewById(R.id.cart_product_quantity_spinner);
                Button undo = (Button) view.findViewById(R.id.cart_product_undo);
                ImageView undo_img = (ImageView) view.findViewById(R.id.cart_product_undo_img);
                LinearLayout ll = (LinearLayout) view.findViewById(R.id.cart_entry_undo);

                int x = (int)e.getRawX();
                int y = (int)e.getRawY();

                if(!inViewInBounds(cart_product_quantity_spinner, x, y) &&
                        ll.getVisibility() == View.GONE){
                    if(product_title.getLineCount() == 1){
                        product_title.setSingleLine(false);
                    }else{
                        product_title.setSingleLine(true);
                    }
                }else if(inViewInBounds(undo_img, x, y) && ll.getVisibility() == View.VISIBLE){
                    undo.performClick();
                }

            }

            // check the touch position for quantity change / card click
            @Override
            public boolean inViewInBounds(View view, int x, int y){
                Rect outRect = new Rect();
                int[] location = new int[2];
                view.getDrawingRect(outRect);
                view.getLocationOnScreen(location);
                outRect.offset(location[0], location[1]);
                return outRect.contains(x, y);
            }

            @Override
            public void onItemLongClick(View view, final int position){

            }
        }));

        Button checkoutButton = (Button) v.findViewById(R.id.cart_button_checkout);
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (products.size() == 0) {
                    Toast.makeText(context, R.string.cart_empty, Toast.LENGTH_LONG).show();
                    return;
                }
                Intent intent = new Intent(context, CheckoutActivity.class);
                context.startActivity(intent);
            }
        });


        // set total price
        refresh();

        // Basket empty? -> show msg if slidinguppanel is not used
        if(products.size() == 0 && !slidingUp){
            Toast.makeText(context,  R.string.cart_empty, Toast.LENGTH_LONG).show();
        }

        // Set up Sliding up Panel
        if(slidingUp) {
            mLayout = (SlidingUpPanelLayout) view.findViewById(R.id.sliding_layout);

            // only the top should be draggable
            mLayout.setDragView(view.findViewById(R.id.dragPart));


            mLayout.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
                @Override
                public void onPanelSlide(View panel, float slideOffset) {
                    TextView total_price_top = (TextView) view.findViewById(R.id.cart_total_price_preview);
                    total_price_top.setAlpha(1 - slideOffset);
                    int diff = (int) (view.getResources().getDimension(R.dimen.slidinguppanel_panel_height) - view.getResources().getDimension(R.dimen.slidinguppanel_panel_height_opened));

                    LinearLayout drag = (LinearLayout) view.findViewById(R.id.dragPart);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            (int) (view.getResources().getDimension(R.dimen.slidinguppanel_panel_height) -
                                     (diff*slideOffset)));

                    layoutParams.setMargins((int) view.getResources().getDimension(R.dimen.slidinguppanel_drag_bg_stroke_margin),0,(int) view.getResources().getDimension(R.dimen.slidinguppanel_drag_bg_stroke_margin),0);
                    drag.setLayoutParams(layoutParams);
                }

                @Override
                public void onPanelExpanded(View panel) {
                    //Log.i("app", "onPanelExpanded");

                }

                @Override
                public void onPanelCollapsed(View panel) {
                    //Log.i("app", "onPanelCollapsed");
                }

                @Override
                public void onPanelAnchored(View panel) {
                    //Log.i("app", "onPanelAnchored");
                }

                @Override
                public void onPanelHidden(View panel) {
                    //Log.i("app", "onPanelHidden");
                }
            });
            updateVisibility();
        }

    }

    public void addToRemovedProducts(CartEntry entry){
        for(int i=0;i<products.size();i++){
            if(products.get(i).getProductId() == entry.getProductId()){
                removed_products.add(products.get(i));
                dao.delete(entry);
            }
        }
    }

    // remove product from deleted list
    public void addRemovedProduct(int position){
        if(removed_products.size() != 0) {
            CartEntry entry = products.get(position);
            dao.create(entry);
            removed_products.remove(entry);
            adapter.notifyDataSetChanged();
            refresh();
        }
    }


    // Getter for RecyclerViewAdapter class to check whether a product has an active removed view or not
    public List<Boolean> getIsProductRemoved(){
        return isProductRemoved;
    }

    // refresh TextView of the total price and #items in cart
    public void refresh(){
        TextView total_price = (TextView) view.findViewById(R.id.cart_total_price);
        total_price.setText(Cart.MYCART.totalPrice());
        String base = view.getResources().getString(R.string.bold_start) + products.size() +
                view.getResources().getString(R.string.cart_article_label) +
                view.getResources().getString(R.string.cart_preview_delimiter) +
                Cart.MYCART.totalPrice() +
                view.getResources().getString(R.string.bold_end);
        TextView total_price_top = (TextView) view.findViewById(R.id.cart_total_price_preview);
        total_price_top.setText(Html.fromHtml(base));
    }

    // panel only visible if cart is not empty
    public void updateVisibility(){
        if(this.slidingUp) {
            if (products.size() == 0) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                mLayout.setPanelHeight((int) (view.getResources().getDimension(R.dimen.zero) / view.getResources().getDisplayMetrics().density));
            } else {
                mLayout.setPanelHeight((int) view.getResources().getDimension(R.dimen.slidinguppanel_panel_height));
            }
        }
    }

    // returns all existing products of the cart
    public List<CartEntry> getProducts(){
        return products;
    }

    // update CartEntry
    public void updateEntry(CartEntry entry){
        for(CartEntry temp : products){
            if(temp.getProductId() == entry.getProductId()){
                temp.setAmount(entry.getAmount());
                dao.update(temp);
                adapter.notifyDataSetChanged();
                refresh();
                updateVisibility();
            }
        }
    }

    // remove CartEntry
    public void removeEntry(CartEntry entry){
        for(int i=0; i<products.size(); i++){
            if(products.get(i).getProductId() == entry.getProductId()){
                dao.delete(entry);
                products.remove(i);
                isProductRemoved.remove(i);
                adapter.notifyDataSetChanged();
                refresh();
                updateVisibility();
                break;
            }
        }


        for(int j=0;j<removed_products.size();j++){
            if(removed_products.get(j).getProductId() == entry.getProductId()){
                removed_products.remove(j);

            }
        }

    }

    public void removeAllEntries() {
        dao.delete(products);
        products.clear();
        isProductRemoved.clear();
        refresh();
        updateVisibility();
    }

    // add product to cart
    public void addProduct(Product product, double amount){
        // update existing cart entry
        for(CartEntry temp : products){
            if (temp.getProductId() == product.getProductId()){
                temp.setAmount(temp.getAmount() + amount);
                dao.update(temp);
                adapter.notifyDataSetChanged();
                refresh();
                updateVisibility();
                return;
            }
        }

        // create new one
        CartEntry new_entry = new CartEntry(product, amount);
        dao.create(new_entry);
        products.add(new_entry);
        isProductRemoved.add(false);
        adapter.notifyDataSetChanged();
        refresh();
        updateVisibility();
    }

    // return total price
    public String totalPrice(){

        double total = 0;
        for(int i=0;i<products.size();i++){
            if(!removed_products.contains(products.get(i)))
                total += products.get(i).getPrice()*products.get(i).getAmount();
        }


        return String.format( "%.2f", total ) + Html.fromHtml(view.getResources().getString(R.string.non_breaking_space)) +
                view.getResources().getString(R.string.currency);
    }

}