package de.fau.cs.mad.fablab.android.navdrawer;


import android.content.Intent;
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
import android.widget.TextView;

import net.spaceapi.HackerSpace;
import net.spaceapi.State;

import de.fau.cs.mad.fablab.android.BarcodeScannerActivity;
import de.fau.cs.mad.fablab.android.MainActivity;
import de.fau.cs.mad.fablab.android.productsearch.ProductSearchActivity;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.cart.CartActivity;
import de.fau.cs.mad.fablab.android.ui.NewsActivity;
import de.fau.cs.mad.fablab.rest.SpaceApiClient;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class AppbarDrawerInclude  {
    private NavigationDrawer navdrawer;
    private DrawerLayout mDrawerLayout;
    private ActionBarActivity mainActivity;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DrawerLayout Drawer;
    private Toolbar toolbar;

    ActionBarDrawerToggle mDrawerToggle;

    Runnable openedStateRunner;

    // Opened State Handler
    private Handler openendStateHandler = new Handler();
    public String openedMessage;
    private Menu menu;

    final long REFRESH_MILLIS = 60 * 1000; // 1 minute
    final long FIRST_MILLIS = 500;

    public AppbarDrawerInclude(ActionBarActivity mainActivity) {
        this.mainActivity = mainActivity;

        // Navigation Drawer
        navdrawer = new NavigationDrawer("Fablab User", "usermail@user.mail", R.drawable.avatar);
        navdrawer.addItem(new NavigationDrawerItem("Main", MainActivity.class, R.drawable.main));
        navdrawer.addItem(new NavigationDrawerItem("Barcode", BarcodeScannerActivity.class, R.drawable.barcode));
        navdrawer.addItem(new NavigationDrawerItem("Warenkorb", CartActivity.class, R.drawable.warenkorb));
        navdrawer.addItem(new NavigationDrawerItem("Produktsuche", ProductSearchActivity.class, R.drawable.produktsuche));
        navdrawer.addItem(new NavigationDrawerItem("News", NewsActivity.class, R.drawable.news));
    }

    public void create() {
        // Appbar instantiation
        toolbar = (Toolbar) mainActivity.findViewById(R.id.appbar);
        mainActivity.setSupportActionBar(toolbar);
        mainActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);

        mRecyclerView = (RecyclerView) mainActivity.findViewById(R.id.navdrawer_RecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new NavigationDrawerAdapter(navdrawer);
        mRecyclerView.setAdapter(mAdapter);

        final GestureDetector mGestureDetector = new GestureDetector(mainActivity, new GestureDetector.SimpleOnGestureListener() {
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
                        Intent intent = new Intent(mainActivity.getApplicationContext(), navdrawer.getItems().get(child_id - 1).getIntent());
                        mainActivity.startActivity(intent);
                    }

                    return true;
                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
            }
        });

        mLayoutManager = new LinearLayoutManager(mainActivity);
        mRecyclerView.setLayoutManager(mLayoutManager);

        Drawer = (DrawerLayout) mainActivity.findViewById(R.id.DrawerLayout);
        mDrawerToggle = new ActionBarDrawerToggle(mainActivity, Drawer, toolbar, R.string.app_name, R.string.app_name){

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

    public void createMenu(Menu menu) {
        if(menu != null) {
            this.menu = menu;
            mainActivity.getMenuInflater().inflate(R.menu.menu_main, this.menu);
        }
    }

    class OpenedStateRunner implements Runnable {
        private Menu m;

        public OpenedStateRunner(Menu m) {
            this.m = m;
        }

        @Override
        public void run() {
            updateOpenState(m.findItem(R.id.action_opened));
            openendStateHandler.postDelayed(this, REFRESH_MILLIS);
        }
    }

    public void startTimer() {
        // Handler instead of TimerTask for opened / close state
        if(menu != null) {
            openedStateRunner = new OpenedStateRunner(menu);
            openendStateHandler.postDelayed(openedStateRunner, FIRST_MILLIS);
        }
    }

    public void stopTimer() {
        openendStateHandler.removeCallbacks(openedStateRunner);
    }

    public void updateOpenState(final MenuItem item) {
        // REST-Client - example usage
        SpaceApiClient spaceApiClient = new SpaceApiClient(mainActivity);
        spaceApiClient.get().getSpace(mainActivity.getString(R.string.SpaceName), new Callback<HackerSpace>() {
            @Override
            public void success(HackerSpace hackerSpace, Response response) {
                // success
                State state = hackerSpace.getState();
                if (state != null) {
                    TextView timeView = (TextView) mainActivity.findViewById(R.id.appbar_time);

                    double currentMillis = System.currentTimeMillis() / 1000L;
                    double millisDelta = (currentMillis - state.getLastchange()) / 60;
                    long time = new Double(millisDelta).longValue();

                    if (state.getOpen() && item.getTitle().toString().equals(mainActivity.getString(R.string.appbar_closed))) {
                        item.setIcon(R.drawable.opened);
                        item.setTitle(R.string.appbar_opened);
                        timeView.setTextColor(mainActivity.getResources().getColor(R.color.AppbarColorOpened));
                    } else if (!state.getOpen() && item.getTitle().toString().equals(mainActivity.getString(R.string.appbar_opened))) {
                        item.setIcon(R.drawable.closed);
                        item.setTitle(R.string.appbar_closed);
                        timeView.setTextColor(mainActivity.getResources().getColor(R.color.AppbarColorClosed));
                    }
                    timeView.setText(getTime(time));
                    openedMessage = state.getMessage();
                }
            }

            private String getTime(long time) {
                if(time < 60)
                    return time + "m";
                else if(time < (60*24)) {
                    long hours = time / 60;
                    long minutes = time % 60;
                    if(minutes == 0)
                        return hours + "h";
                    else
                        return hours + "h " + minutes + "m";
                } else {
                    return "";
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