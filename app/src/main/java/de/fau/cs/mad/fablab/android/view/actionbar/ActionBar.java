package de.fau.cs.mad.fablab.android.view.actionbar;

import android.content.Context;
import android.content.res.Configuration;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.view.activities.MainActivity;
import de.fau.cs.mad.fablab.android.view.common.binding.ViewCommandBinding;

public class ActionBar implements ActionBarViewModel.Listener {
    @Bind(R.id.appbar)
    Toolbar toolbar;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawer;
    @Bind(R.id.appbar_logo)
    LinearLayout logo_ll;
    @Bind(R.id.appbar_title)
    TextView title_tv;

    private ActionBarDrawerToggle mDrawerToggle;
    private TextView time_tv;
    private ImageView door_state_iv;

    private Context mContext;

    @Inject
    ActionBarViewModel mViewModel;

    public ActionBar(MainActivity activity, View view) {
        mContext = activity;

        ButterKnife.bind(this, view);
        activity.inject(this);

        activity.setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mDrawerToggle = new ActionBarDrawerToggle(activity, drawer, R.string.app_name,
                R.string.app_name);
        drawer.setDrawerListener(mDrawerToggle);
    }

    public void showNavdrawerIcon(boolean show) {
        mDrawerToggle.setDrawerIndicatorEnabled(show);
    }

    public void showLogo(boolean show) {
        logo_ll.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public void showTitle(boolean show) {
        if (show) {
            title_tv.setVisibility(View.VISIBLE);
        } else {
            title_tv.setVisibility(View.GONE);
            title_tv.setText("");
        }
    }

    public void setTitle(String title) {
        title_tv.setText(title);
    }

    public void bindMenuItems() {
        MenuItem doorStateMenuItem = toolbar.getMenu().findItem(R.id.action_door_state);
        doorStateMenuItem.setActionView(R.layout.door_state_action_view);
        View actionView = doorStateMenuItem.getActionView();
        time_tv = (TextView) actionView.findViewById(R.id.time_tv);
        door_state_iv = (ImageView) actionView.findViewById(R.id.door_state_iv);
        new ViewCommandBinding().bind(actionView, mViewModel.getShowDoorStateToastCommand());

        mViewModel.setListener(this);
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
        if (open) {
            door_state_iv.setImageResource(R.drawable.opened);
            time_tv.setTextColor(time_tv.getResources().getColor(R.color.appbar_color_opened));
        } else {
            door_state_iv.setImageResource(R.drawable.closed);
            time_tv.setTextColor(time_tv.getResources().getColor(R.color.appbar_color_closed));
        }
        time_tv.setText(time);
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
