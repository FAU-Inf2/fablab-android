package de.fau.cs.mad.fablab.android.view.actionbar;

import android.content.res.Configuration;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.view.MainActivity;
import de.fau.cs.mad.fablab.android.view.common.binding.MenuItemCommandBinding;

public class ActionBar implements ActionBarViewModel.Listener {

    @InjectView(R.id.appbar)
    Toolbar toolbar;
    @InjectView(R.id.drawer_layout)
    DrawerLayout drawer;
    @InjectView(R.id.appbar_time)
    TextView time_tv;

    private MenuItem mOpenStateMenuItem;

    private ActionBarDrawerToggle mDrawerToggle;

    @Inject
    ActionBarViewModel mViewModel;

    public ActionBar(MainActivity activity, View view) {
        ButterKnife.inject(this, view);
        activity.inject(this);

        mViewModel.setListener(this);

        activity.setSupportActionBar(toolbar);
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mDrawerToggle = new ActionBarDrawerToggle(activity, drawer, R.string.app_name,
                R.string.app_name) {

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
    }

    public void showNavdrawerIcon(boolean show) {
        mDrawerToggle.setDrawerIndicatorEnabled(show);
    }

    public void bindMenuItems() {
        mOpenStateMenuItem = toolbar.getMenu().findItem(R.id.action_opened);
        new MenuItemCommandBinding().bind(mOpenStateMenuItem,
                mViewModel.getRefreshOpenedStateCommand());
        mViewModel.initialize();
    }

    public void pause() {
        mViewModel.pause();
    }

    public void resume() {
        mViewModel.resume();
    }

    public void onPostCreate() {
        mDrawerToggle.syncState();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item);
    }

    @Override
    public void onStateUpdated(boolean open, String time) {
        if (mOpenStateMenuItem != null) {
            if (open) {
                mOpenStateMenuItem.setIcon(R.drawable.opened);
                mOpenStateMenuItem.setTitle(R.string.appbar_opened);
                time_tv.setTextColor(time_tv.getResources().getColor(R.color.appbar_color_opened));
            } else {
                mOpenStateMenuItem.setIcon(R.drawable.closed);
                mOpenStateMenuItem.setTitle(R.string.appbar_closed);
                time_tv.setTextColor(time_tv.getResources().getColor(R.color.appbar_color_closed));
            }
            time_tv.setText(time);
        }
    }
}
