package de.fau.cs.mad.fablab.android;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import net.spaceapi.HackerSpace;
import net.spaceapi.State;

import de.fau.cs.mad.fablab.android.basket.BasketActivity;
import de.fau.cs.mad.fablab.android.navdrawer.NavigationDrawer;
import de.fau.cs.mad.fablab.android.navdrawer.NavigationDrawerAdapter;
import de.fau.cs.mad.fablab.android.navdrawer.NavigationDrawerItem;
import de.fau.cs.mad.fablab.rest.SpaceApiClient;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends ActionBarActivity {
    // Appbar toolbar material design for pre-lollipop versions
    private Toolbar toolbar;

    // Navigation Drawer
    private NavigationDrawer navdrawer;
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    DrawerLayout Drawer;

    ActionBarDrawerToggle mDrawerToggle;

    // Opened State Handler
    private Handler openendStateHandler = new Handler();
    private String openedMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Appbar instantiation
        toolbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Navigation Drawer
        navdrawer = new NavigationDrawer("Fablab User", "usermail@user.mail");
        navdrawer.addItem(new NavigationDrawerItem("Datenbank", BasketTestActivity.class));
        navdrawer.addItem(new NavigationDrawerItem("Barcode", BarcodeScannerActivity.class));
        navdrawer.addItem(new NavigationDrawerItem("Warenkorb", BasketActivity.class));
        navdrawer.addItem(new NavigationDrawerItem("Produktsuche", ProductSearchActivity.class));

        mRecyclerView = (RecyclerView) findViewById(R.id.navdrawer_RecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new NavigationDrawerAdapter(navdrawer);
        mRecyclerView.setAdapter(mAdapter);

        final GestureDetector mGestureDetector = new GestureDetector(MainActivity.this, new GestureDetector.SimpleOnGestureListener() {
            @Override public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });

        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {
                    int child_id = recyclerView.getChildPosition(child);
                    if(child_id > 0) {
                        Drawer.closeDrawers();
                        Intent intent = new Intent(getApplicationContext(), navdrawer.getItems().get(child_id - 1).getIntent());
                        startActivity(intent);
                    }

                    return true;
                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
            }
        });

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);
        mDrawerToggle = new ActionBarDrawerToggle(this,Drawer,toolbar,R.string.app_name,R.string.app_name){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }



        };
        Drawer.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);


        // Handler instead of TimerTask for opened / close state
        class OpenedStateRunner implements Runnable {
            private Menu m;

            public OpenedStateRunner(Menu m) {
                this.m = m;
            }
            @Override
            public void run() {
                updateOpenState(m.findItem(R.id.action_opened));
                openendStateHandler.postDelayed(this, 60 * 1000);
            }
        }

        Runnable openedStateRunner = new OpenedStateRunner(menu);

        openendStateHandler.postDelayed(openedStateRunner, 500);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_opened) {
            updateOpenState(item);
            Toast appbar_opened_toast = Toast.makeText(this, openedMessage, Toast.LENGTH_SHORT);
            appbar_opened_toast.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showBasketTest(View view){
        Intent intent = new Intent(getApplicationContext(), BasketTestActivity.class);
        startActivity(intent);
    }

    public void startBarcodeScanner(View view) {
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Intent intent = new Intent(this, BarcodeScannerActivity.class);
            startActivity(intent);
        }
    }

    public void showBasket(View view){
        Intent intent = new Intent(this, BasketActivity.class);
        startActivity(intent);
    }

    public void startProductSearch(View view) {
        Intent intent = new Intent(this, ProductSearchActivity.class);
        startActivity(intent);
    }

    private void updateOpenState(final MenuItem item) {
        // REST-Client - example usage
        SpaceApiClient spaceApiClient = new SpaceApiClient(this);
        spaceApiClient.get().getSpace(getString(R.string.SpaceName), new Callback<HackerSpace>() {
            @Override
            public void success(HackerSpace hackerSpace, Response response) {
                // success
                State state = hackerSpace.getState();
                if (state != null) {
                    if(!state.getOpen() && item.getTitle().toString().equals(getString(R.string.appbar_closed))) {
                        item.setIcon(R.drawable.opened);
                        item.setTitle(R.string.appbar_opened);
                    } else if(!state.getOpen() && item.getTitle().toString().equals(getString(R.string.appbar_opened))) {
                        item.setIcon(R.drawable.closed);
                        item.setTitle(R.string.appbar_closed);
                    }

                    openedMessage = state.getMessage();

                    Log.i("App", state.getMessage());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                // something went wrong
                Log.i("App", error.getMessage());
            }
        });
    }
}
