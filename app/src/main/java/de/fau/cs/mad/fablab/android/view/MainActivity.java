package de.fau.cs.mad.fablab.android.view;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.eventbus.NavigationEvent;
import de.fau.cs.mad.fablab.android.model.StorageFragment;
import de.fau.cs.mad.fablab.android.navdrawer.NavigationDrawer;
import de.fau.cs.mad.fablab.android.navdrawer.NavigationDrawerLauncher;
import de.fau.cs.mad.fablab.android.navdrawer.NavigationDrawerViewModel;
import de.fau.cs.mad.fablab.android.view.floatingbutton.FloatingFablabButton;
import de.fau.cs.mad.fablab.android.view.fragments.news.NewsFragment;
import de.greenrobot.event.EventBus;

public class MainActivity extends AppCompatActivity {

    private FloatingFablabButton fablabButton;

    private static final String NAV_ITEM_ID = "navItemId";

    private final Handler mDrawerActionHandler = new Handler();
    private static final long DRAWER_CLOSE_DELAY_MS = 250;

    @InjectView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @InjectView(R.id.appbar)
    Toolbar toolbar;

    @InjectView(R.id.navigation)
    NavigationView navigationView;

    private ActionBarDrawerToggle mDrawerToggle;
    private int mNavItemId;

    EventBus eventbus = EventBus.getDefault();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);



        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new NewsFragment()).commit();
            //initialize our storage
            StorageFragment.initializeStorage(this);

            // load saved navigation state if present
            mNavItemId = R.id.drawer_item_news;
        } else {
            mNavItemId = savedInstanceState.getInt(NAV_ITEM_ID);
        }

        NavigationDrawer navigationDrawer = new NavigationDrawer(mDrawerLayout, navigationView);
        navigationDrawer.setViewModel(new NavigationDrawerViewModel(new NavigationDrawerLauncher(this)));
        navigationDrawer.createView();

        eventbus.register(this);

        navigate(mNavItemId);
    }

    private void navigate(int itemId) {
        NewsFragment newsfragment = new NewsFragment();
        switch(itemId) {
            case R.id.drawer_item_news:
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, newsfragment).commit();
                break;
            case R.id.drawer_item_productsearch:
                // Test ... einfach Fragment mal loeschen
                getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentById(R.id.fragment_container)).commit();
                break;
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        StorageFragment.initializeStorage(this);
        fablabButton = new FloatingFablabButton(this, findViewById(android.R.id.content));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void onEvent(NavigationEvent event) {
        navigate(event.getitemId());
    }
}
