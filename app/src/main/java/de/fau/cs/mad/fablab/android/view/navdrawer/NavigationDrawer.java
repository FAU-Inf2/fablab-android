package de.fau.cs.mad.fablab.android.view.navdrawer;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.view.activities.MainActivity;
import de.fau.cs.mad.fablab.android.view.common.binding.MenuItemCommandBinding;

public class NavigationDrawer implements NavigationDrawerViewModel.Listener {
    @InjectView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @InjectView(R.id.navigation)
    NavigationView mNavigationView;

    @Inject
    NavigationDrawerViewModel mViewModel;

    public NavigationDrawer(MainActivity activity, View view) {
        ButterKnife.inject(this, view);
        activity.inject(this);

        mViewModel.setListener(this);

        Menu menu = mNavigationView.getMenu();
        new MenuItemCommandBinding().bind(menu.findItem(R.id.drawer_item_news),
                mViewModel.getNavigateToNewsCommand());
        new MenuItemCommandBinding().bind(menu.findItem(R.id.drawer_item_productsearch),
                mViewModel.getNavigateToProductSearchCommand());
        new MenuItemCommandBinding().bind(menu.findItem(R.id.drawer_item_scanner),
                mViewModel.getNavigateToBarcodeScannerCommand());
        new MenuItemCommandBinding().bind(menu.findItem(R.id.drawer_item_about),
                mViewModel.getNavigateToAboutCommand());
        new MenuItemCommandBinding().bind(menu.findItem(R.id.drawer_item_settings),
                mViewModel.getNavigateToSettingsCommand());
    }

    @Override
    public void onNavigationDrawerItemSelected(int itemId) {
        MenuItem item = mNavigationView.getMenu().findItem(itemId);
        if(item != null) {
            item.setChecked(true);
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    public void setSelection(int itemId) {
        mViewModel.setSelection(itemId);
    }

    public void enableDrawer(boolean enable) {
        mDrawerLayout.setDrawerLockMode(enable ? DrawerLayout.LOCK_MODE_UNLOCKED
                : DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    public void restoreState(Bundle savedInstanceState) {
        mViewModel.restoreState(savedInstanceState);
    }

    public void saveState(Bundle outState) {
        mViewModel.saveState(outState);
    }
}
