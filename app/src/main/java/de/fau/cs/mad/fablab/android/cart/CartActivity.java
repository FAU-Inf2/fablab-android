package de.fau.cs.mad.fablab.android.cart;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.navdrawer.AppbarDrawerInclude;
import de.fau.cs.mad.fablab.rest.core.CartEntry;


public class CartActivity extends ActionBarActivity{

    private List<CartEntry> cart_entries;
    private Dialog dialog;
    private RecyclerViewAdapter adapter;
    private AppbarDrawerInclude appbarDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        appbarDrawer = AppbarDrawerInclude.getInstance(this);
        appbarDrawer.create();

        // Setting up Basket without SlidingUpPanel
        Cart.MYCART.setSlidingUpPanel(this, findViewById(android.R.id.content), false);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        appbarDrawer.createMenu(menu);
        appbarDrawer.startTimer();
        return true;
    }

    @Override
    public void onPause() {
        super.onPause();
        appbarDrawer.stopTimer();
    }

    @Override
    public void onResume() {
        super.onResume();
        appbarDrawer.startTimer();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_opened) {
            appbarDrawer.updateOpenState(item);
            Toast appbar_opened_toast = Toast.makeText(this, appbarDrawer.openedMessage, Toast.LENGTH_SHORT);
            appbar_opened_toast.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}