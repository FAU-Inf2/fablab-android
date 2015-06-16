package de.fau.cs.mad.fablab.android.navdrawer;

import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import de.fau.cs.mad.fablab.android.eventbus.NavigationEvent;
import de.greenrobot.event.EventBus;

public class NavigationDrawer implements NavigationDrawerViewModel.Listener, NavigationView.OnNavigationItemSelectedListener {
    NavigationDrawerViewModel viewModel;
    EventBus eventbus = EventBus.getDefault();

    DrawerLayout mDrawerLayout;
    NavigationView navigationView;

    public NavigationDrawer(DrawerLayout mDrawerLayout, NavigationView navigationView) {
        this.mDrawerLayout = mDrawerLayout;
        this.navigationView = navigationView;
    }

    public void createView() {
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().findItem(viewModel.getItemId()).setChecked(true);
    }

    public void setViewModel(NavigationDrawerViewModel viewModel){
        this.viewModel = viewModel;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        menuItem.setChecked(true);
        viewModel.setData(menuItem.getItemId());
        viewModel.getLauncher().closeDrawer(mDrawerLayout);

        eventbus.post(new NavigationEvent(menuItem.getItemId()));
        return true;
    }
}
