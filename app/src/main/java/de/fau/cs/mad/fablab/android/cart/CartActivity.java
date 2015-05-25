package de.fau.cs.mad.fablab.android.cart;

import android.app.Dialog;
import android.os.Bundle;

import java.util.List;

import de.fau.cs.mad.fablab.android.BaseActivity;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.rest.core.CartEntry;


public class CartActivity extends BaseActivity {

    private List<CartEntry> cart_entries;
    private Dialog dialog;
    private RecyclerViewAdapter adapter;

    @Override
    protected void baseOnCreate(Bundle savedInstanceState) {
        // Setting up Basket without SlidingUpPanel
        initCartPanel(false);
        initFabButton();
    }

    @Override
    protected void baseSetContentView() {
        setContentView(R.layout.activity_cart);
    }
}