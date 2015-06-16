package de.fau.cs.mad.fablab.android.navdrawer;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;

public class NavigationDrawerLauncher  {
    //NavigationView navigationView;
    private AppCompatActivity activity;
    //private final Handler mDrawerActionHandler = new Handler();
    //private static final long DRAWER_CLOSE_DELAY_MS = 250;

    public NavigationDrawerLauncher(AppCompatActivity activity) {
        this.activity = activity;
    }

    public AppCompatActivity getActivity() {
        return activity;
    }

    public void openDrawer(DrawerLayout mDrawerLayout) {
        mDrawerLayout.openDrawer(GravityCompat.START);
    }

    public void closeDrawer(DrawerLayout mDrawerLayout) {
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

}
