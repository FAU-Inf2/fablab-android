package de.fau.cs.mad.fablab.android.cart;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.brnunes.swipeablerecyclerview.SwipeableRecyclerViewTouchListener;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.db.DatabaseHelper;
import de.fau.cs.mad.fablab.rest.core.Cart;
import de.fau.cs.mad.fablab.rest.core.CartEntry;
import de.fau.cs.mad.fablab.rest.core.CartStatusEnum;
import de.fau.cs.mad.fablab.rest.core.Product;

public enum CartSingleton {
    MYCART;

    private RuntimeExceptionDao<CartEntry, Long> cartEntryDao;
    private RuntimeExceptionDao<Cart, String> cartDao;
    private RuntimeExceptionDao<Product, String> productDao;
    private RecyclerViewAdapter adapter;
    private Context context;
    private View view;
    private SlidingUpPanelLayout mLayout;
    private boolean slidingUp;

    private Cart cart;
    private List<CartEntry> products;

    private CartEntry removed_entry;
    private int removed_pos;



    CartSingleton() {
    }

    public Cart getCart() {
        return cart;
    }

    // initialization of the db - getting all products that are in the cart
    // call this method on startup activity
    public void init(Context context) {
        cartDao = DatabaseHelper.getHelper(context).getCartDao();
        cartEntryDao = DatabaseHelper.getHelper(context).getCartEntryDao();
        productDao = DatabaseHelper.getHelper(context).getProductDao();
        QueryBuilder<Cart, String> queryBuilder = cartDao.queryBuilder();
        try {
            queryBuilder.where().eq("status", CartStatusEnum.SHOPPING).or().eq("status", CartStatusEnum.PENDING);
            cart = cartDao.queryForFirst(queryBuilder.prepare());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (cart == null) {
            cart = new Cart();
            cart.setCartCode(Long.toString(new Random().nextLong()));
            cartDao.create(cart);
            products = new ArrayList<>();
            cartDao.refresh(cart);
        } else {
            products = cartEntryDao.queryForEq("cart_id", cart.getCartCode());
        }
    }

    // Setting up the view for every context c including the sliding up panel
    public void setSlidingUpPanel(Context c, View v, boolean slidingUp) {
        this.context = c;
        this.view = v;
        this.slidingUp = slidingUp;

        // Set Layout and Recyclerview
        RecyclerView cart_rv = (RecyclerView) v.findViewById(R.id.cart_recycler_view);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        cart_rv.setLayoutManager(llm);
        cart_rv.setHasFixedSize(false);
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
                                    CartSingleton.MYCART.removeEntry(products.get(position));
                                }
                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    CartSingleton.MYCART.removeEntry(products.get(position));
                                }
                            }
                        });

        cart_rv.addOnItemTouchListener(swipeTouchListener);

        // checkout button
        Button checkoutButton = (Button) v.findViewById(R.id.cart_button_checkout);
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cart.getProducts().size() == 0) {
                    Toast.makeText(context, R.string.cart_empty, Toast.LENGTH_LONG).show();
                    return;
                }
                Intent intent = new Intent(context, CheckoutActivity2.class);
                context.startActivity(intent);
            }
        });


        // set total price
        refresh();

        // Basket empty? -> show msg if slidinguppanel is not used
        if (products.size() == 0 && !slidingUp) {
            Toast.makeText(context, R.string.cart_empty, Toast.LENGTH_LONG).show();
        }

        // Set up Sliding up Panel
        if (slidingUp) {
            mLayout = (SlidingUpPanelLayout) view.findViewById(R.id.sliding_layout);

            // Adapt Panel height when user rotates device
            if (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
                TextView total_price_top = (TextView) view.findViewById(R.id.cart_total_price_preview);
                total_price_top.setAlpha(0);

                LinearLayout drag = (LinearLayout) view.findViewById(R.id.dragPart);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        (int) (view.getResources().getDimension(R.dimen.slidinguppanel_panel_height_opened)));

                layoutParams.setMargins((int) view.getResources().getDimension(R.dimen.slidinguppanel_drag_bg_stroke_margin), 0, (int) view.getResources().getDimension(R.dimen.slidinguppanel_drag_bg_stroke_margin), 0);
                drag.setLayoutParams(layoutParams);
            }

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
                                    (diff * slideOffset)));

                    layoutParams.setMargins((int) view.getResources().getDimension(R.dimen.slidinguppanel_drag_bg_stroke_margin), 0, (int) view.getResources().getDimension(R.dimen.slidinguppanel_drag_bg_stroke_margin), 0);
                    drag.setLayoutParams(layoutParams);
                }

                @Override
                public void onPanelExpanded(View panel) {}

                @Override
                public void onPanelCollapsed(View panel) {
                    refresh();
                }

                @Override
                public void onPanelAnchored(View panel) {}

                @Override
                public void onPanelHidden(View panel) {}
            });
            updateVisibility();
        }
    }

    // refresh TextView of the total price and #items in cart
    public void refresh() {
        TextView total_price = (TextView) view.findViewById(R.id.cart_total_price);
        total_price.setText(CartSingleton.MYCART.totalPrice());
        String base = view.getResources().getString(R.string.bold_start) + products.size() +
                view.getResources().getString(R.string.cart_article_label) +
                view.getResources().getString(R.string.cart_preview_delimiter) +
                CartSingleton.MYCART.totalPrice() +
                view.getResources().getString(R.string.bold_end);
        TextView total_price_top = (TextView) view.findViewById(R.id.cart_total_price_preview);
        total_price_top.setText(Html.fromHtml(base));
    }

    // panel only visible if cart is not empty
    public void updateVisibility() {
        if (this.slidingUp) {
            if (products.size() == 0) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                mLayout.setPanelHeight((int) (view.getResources().getDimension(R.dimen.zero) / view.getResources().getDisplayMetrics().density));
            } else {
                mLayout.setPanelHeight((int) view.getResources().getDimension(R.dimen.slidinguppanel_panel_height));
            }
        }
    }

    // returns all existing products of the carts
    public List<CartEntry> getProducts() {
        return products;
    }

    // update CartEntry in db table
    public void updateProducts(CartEntry entry) {
        int pos = products.indexOf(entry);
        products.get(pos).setAmount(entry.getAmount());
        cartEntryDao.update(products.get(pos));
        cartDao.refresh(cart);
        cartDao.refresh(cart);
    }

    // remove CartEntry from db table and GUI - add removed entry to undo bar
    public void removeEntry(CartEntry entry) {
        int pos = products.indexOf(entry);
        products.remove(entry);
        DeleteBuilder db_entry = cartEntryDao.deleteBuilder();
        try {
            db_entry.where().eq("id", entry.getId());
            cartEntryDao.delete(db_entry.prepare());
            cartDao.refresh(cart);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        adapter.notifyItemRemoved(pos);

        removed_pos = pos;
        removed_entry = entry;
        refresh();
        showUndoBar();
    }


    // remove all entries db tables
    public void removeAllEntries() {
        DeleteBuilder db_cart = cartDao.deleteBuilder();
        DeleteBuilder db_entries = cartEntryDao.deleteBuilder();

        try {
            db_entries.where().eq("cart_id", cart.getCartCode());
            cartEntryDao.delete(db_entries.prepare());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            db_cart.where().eq("cart_id", cart.getCartCode());
            cartDao.delete(db_cart.prepare());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        products.clear();
        adapter.notifyDataSetChanged();
    }

    // add product to cart
    public void addProduct(Product product, double amount) {
        // update existing cart entry
        for (CartEntry temp : products) {
            if (temp.getProduct().getProductId().equals(product.getProductId())) {
                temp.setAmount(temp.getAmount() + amount);
                int pos = products.indexOf(temp);
                products.get(pos).setAmount(temp.getAmount());
                cartEntryDao.update(products.get(pos));
                return;
            }
        }

        // create new one
        CartEntry new_entry = new CartEntry(product, amount);
        new_entry.setCart(cart);
        new_entry.setProduct(product);
        cartEntryDao.create(new_entry);
        cartDao.refresh(cart);

        products.add(new_entry);
        adapter.notifyDataSetChanged();
    }

    // return total price
    public String totalPrice() {
        double total = cart.getTotal();
        return String.format("%.2f", total) + Html.fromHtml(view.getResources().getString(R.string.non_breaking_space)) +
                view.getResources().getString(R.string.currency);
    }

    // show "undo-toast" for a short period
    // param entry represents the removed entry
    // param pos represents the original position of the entry in cart
    public void showUndoBar(){
        TextView remove_txt = (TextView) view.findViewById(R.id.cart_product_removed);
        remove_txt.setText(view.getResources().getString(R.string.cart_product_removed));

        final Button remove_btn = (Button) view.findViewById(R.id.cart_product_undo);
        remove_btn.setText(view.getResources().getString(R.string.cart_product_undo));

        final LinearLayout rl = (LinearLayout) view.findViewById(R.id.cart_undo_bar);

        ImageView remove_img = (ImageView) view.findViewById(R.id.cart_product_undo_img);

        // undo entry removal by clicking on the undo img or button
        remove_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl.clearAnimation();
                rl.setVisibility(View.GONE);
                cartEntryDao.create(removed_entry);
                cartDao.refresh(cart);
                products.add(removed_pos, removed_entry);
                adapter.notifyDataSetChanged();
                refresh();
            }
        });

        remove_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove_btn.performClick();
            }
        });

        rl.setClickable(true);
        rl.setVisibility(View.VISIBLE);

        rl.setAlpha(1);
        AlphaAnimation anim = new AlphaAnimation(1f, 0f);
        anim.setDuration(4000);

        anim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                rl.setVisibility(View.GONE);
                updateVisibility();
            }
        });

        rl.startAnimation(anim);
    }
}