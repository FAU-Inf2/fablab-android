package de.fau.cs.mad.fablab.android.cart;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.LinearLayout;

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
        LinearLayout drag = (LinearLayout) findViewById(R.id.dragPart);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                (int) getResources().getDimension(R.dimen.slidinguppanel_panel_height_opened) );

        layoutParams.setMargins((int) getResources().getDimension(R.dimen.slidinguppanel_drag_bg_stroke_margin), 0, (int) getResources().getDimension(R.dimen.slidinguppanel_drag_bg_stroke_margin), 0);
        drag.setLayoutParams(layoutParams);
    }

    @Override
    protected void baseSetContentView() {
        setContentView(R.layout.activity_cart);
    }
}