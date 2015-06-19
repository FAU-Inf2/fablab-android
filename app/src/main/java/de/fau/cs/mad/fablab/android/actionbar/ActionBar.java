package de.fau.cs.mad.fablab.android.actionbar;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.view.MainActivity;

public class ActionBar implements ActionBarViewModel.Listener {

    @Inject
    ActionBarViewModel mViewModel;

    @InjectView(R.id.appbar)
    Toolbar toolbar;

    @InjectView(R.id.drawer_layout)
    DrawerLayout drawer;

    ActionBarDrawerToggle mDrawerToggle;

    public ActionBar(MainActivity activity, View view) {
        ButterKnife.inject(this, view);
        activity.inject(this);

        mViewModel.setListener(this);

        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);

        /* DOESNT WORK because menu is not inflated yet, happens in MainActivity.onCreateOptionsMenu, which is called AFTER onResume
        Menu menu = toolbar.getMenu();
        new MenuItemCommandBinding().bind(menu.findItem(R.id.action_opened), mViewModel.getRefreshOpenedStateCommand()); */

        mDrawerToggle = new ActionBarDrawerToggle(activity, drawer, toolbar, R.string.app_name, R.string.app_name) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        drawer.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }

    /*@Override
    public void onActionBarItemSelected() {

    }*/
}
