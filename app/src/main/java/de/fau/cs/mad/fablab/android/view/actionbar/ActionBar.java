package de.fau.cs.mad.fablab.android.view.actionbar;

import android.content.Context;
import android.content.res.Configuration;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.view.activities.MainActivity;
import de.fau.cs.mad.fablab.android.view.common.binding.MenuItemCommandBinding;

public class ActionBar implements ActionBarViewModel.Listener {

    @Bind(R.id.appbar)
    Toolbar toolbar;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawer;
    @Bind(R.id.appbar_time)
    TextView time_tv;

    @Bind(R.id.appbar_title)
    TextView appbar_title;
    @Bind(R.id.appbar_fau)
    TextView appbar_fau;
    @Bind(R.id.appbar_fablab)
    TextView appbar_fablab;
    @Bind(R.id.icon_fablab)
    ImageView icon_fablab;

    private MenuItem mOpenStateMenuItem;
    private ActionBarDrawerToggle mDrawerToggle;
    private Context mContext;

    @Inject
    ActionBarViewModel mViewModel;

    public ActionBar(MainActivity activity, View view) {
        mContext = activity;

        ButterKnife.bind(this, view);
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

    public void showLogo(boolean show) {
        if (show) {
            appbar_fablab.setVisibility(View.VISIBLE);
            appbar_fau.setVisibility(View.VISIBLE);
            icon_fablab.setVisibility(View.VISIBLE);
            time_tv.setVisibility(View.VISIBLE);
        } else {
            appbar_fablab.setVisibility(View.GONE);
            appbar_fau.setVisibility(View.GONE);
            icon_fablab.setVisibility(View.GONE);
            time_tv.setVisibility(View.GONE);
        }
    }

    public void showTime(boolean show)
    {
        if (show)
        {
            time_tv.setVisibility(View.VISIBLE);
        }
        else
        {
            time_tv.setVisibility(View.GONE);
        }
    }

    public void showTitle(boolean show) {
        if (show) {
            appbar_title.setVisibility(View.VISIBLE);
        } else {
            appbar_title.setVisibility(View.GONE);
            appbar_title.setText("");
        }
    }

    public void setTitle(String title) {
        appbar_title.setText(title);
    }

    public void bindMenuItems() {
        mOpenStateMenuItem = toolbar.getMenu().findItem(R.id.action_opened);
        new MenuItemCommandBinding().bind(mOpenStateMenuItem,
                mViewModel.getShowDoorStateToastCommand());
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

    @Override
    public void onShowDoorStateToast(boolean state, String time) {
        String text;
        if (time.isEmpty()) {
            text = mContext.getString(state ? R.string.appbar_opened : R.string.appbar_fablab_closed);
        } else {
            text = mContext.getString(state ? R.string.appbar_opened : R.string.appbar_closed)
                    + " " + mContext.getString(R.string.appbar_opened_since) + " " + time;
        }
        Toast.makeText(mContext, text, Toast.LENGTH_LONG).show();
    }
}
