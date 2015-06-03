package de.fau.cs.mad.fablab.android;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import de.fau.cs.mad.fablab.android.cart.CartSingleton;
import de.fau.cs.mad.fablab.android.eventbus.DoorEvent;
import de.fau.cs.mad.fablab.android.navdrawer.AppbarDrawerInclude;
import de.greenrobot.event.EventBus;

public abstract class BaseActivity extends ActionBarActivity {
    private static final int DISPLAY_CART_NONE = 0;
    private static final int DISPLAY_CART_SLIDING_UP = 1;
    private static final int DISPLAY_CART_FULL = 2;
    // Navigation Drawer
    public AppbarDrawerInclude appbarDrawer;
    private EventBus eventBus = EventBus.getDefault();
    private int cartDisplayOptions = DISPLAY_CART_NONE;

    protected abstract void baseOnCreate(Bundle savedInstanceState);
    protected abstract void baseSetContentView();
    protected void baseOnDestroy() { }
    protected boolean baseOnCreateOptionsMenu(Menu menu) { return true; }

    @Override
    final public void onCreate(Bundle savedInstanceState) {
        baseSetContentView();

        appbarDrawer = AppbarDrawerInclude.getInstance(this);
        appbarDrawer.create();

        eventBus.register(this);

        if (savedInstanceState != null) {
            cartDisplayOptions = savedInstanceState.getInt("display_cart");
        }

        baseOnCreate(savedInstanceState);

        super.onCreate(savedInstanceState);
    }

    public void onEvent(DoorEvent event) {
        appbarDrawer.updateOpenStateEvent(event);
    }

    public void initFabButton() {
        FabButton.MYFABUTTON.init(findViewById(android.R.id.content), this);
    }

    public void initCartPanel(boolean show) {
        CartSingleton.MYCART.setSlidingUpPanel(this, findViewById(android.R.id.content), show);
        cartDisplayOptions = show ? DISPLAY_CART_SLIDING_UP : DISPLAY_CART_FULL;
    }

    @Override
    final public void onDestroy() {
        baseOnDestroy();
        eventBus.unregister(this);
        super.onDestroy();
    }

    @Override
    final public boolean onCreateOptionsMenu(Menu menu) {
        appbarDrawer.createMenu(menu);
        appbarDrawer.startTimer();
        baseOnCreateOptionsMenu(menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    final public void onPause() {
        baseOnPause();
        super.onPause();
    }

    @Override
    final public void onResume() {
        baseOnResume();
        if (cartDisplayOptions != DISPLAY_CART_NONE) {
            CartSingleton.MYCART.setSlidingUpPanel(this, findViewById(android.R.id.content),
                    cartDisplayOptions == DISPLAY_CART_SLIDING_UP);
        }
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("display_cart", cartDisplayOptions);
    }

    protected void baseOnPause() {
        appbarDrawer.stopTimer();
    }

    protected void baseOnResume() {
        appbarDrawer.startTimer();
    }

    @Override
    final public boolean onOptionsItemSelected(MenuItem item) {
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
