package de.fau.cs.mad.fablab.android.view.navdrawer;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.util.UiUtils;
import de.fau.cs.mad.fablab.android.view.activities.MainActivity;
import de.fau.cs.mad.fablab.android.view.common.binding.MenuItemCommandBinding;
import de.fau.cs.mad.fablab.rest.core.User;

public class NavigationDrawer implements NavigationDrawerViewModel.Listener {
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.navigation)
    NavigationView mNavigationView;

    // views when logged in
    @Bind(R.id.navdrawer_header_loggedin)
    LinearLayout mHeaderLoggedIn;
    @Bind(R.id.navdrawer_name)
    TextView mUsernameLoggedIn;


    @Inject
    NavigationDrawerViewModel mViewModel;

    private Activity mainActivity;

    public NavigationDrawer(MainActivity activity, View view) {
        ButterKnife.bind(this, view);
        activity.inject(this);

        mainActivity = activity;

        mViewModel.setListener(this);

        Menu menu = mNavigationView.getMenu();
        new MenuItemCommandBinding().bind(menu.findItem(R.id.drawer_item_news),
                mViewModel.getNavigateToNewsCommand());
        new MenuItemCommandBinding().bind(menu.findItem(R.id.drawer_item_productsearch),
                mViewModel.getNavigateToProductSearchCommand());
        new MenuItemCommandBinding().bind(menu.findItem(R.id.drawer_item_categorysearch),
                mViewModel.getNavigateToCategorySearchCommand());
        new MenuItemCommandBinding().bind(menu.findItem(R.id.drawer_item_scanner),
                mViewModel.getNavigateToBarcodeScannerCommand());
        new MenuItemCommandBinding().bind(menu.findItem(R.id.drawer_item_inventory),
                mViewModel.getInventoryCommand());
        new MenuItemCommandBinding().bind(menu.findItem(R.id.drawer_item_alert),
                mViewModel.getNavigateToAlertCommand());
        new MenuItemCommandBinding().bind(menu.findItem(R.id.drawer_item_about),
                mViewModel.getNavigateToAboutCommand());
        new MenuItemCommandBinding().bind(menu.findItem(R.id.drawer_item_settings),
                mViewModel.getNavigateToSettingsCommand());
        new MenuItemCommandBinding().bind(menu.findItem(R.id.drawer_item_logout),
                mViewModel.getLogoutCommand());
        /*
        new MenuItemCommandBinding().bind(menu.findItem(R.id.drawer_item_projects),
                mViewModel.getNavigateToProjectsCommand());
                */

        menu.findItem(R.id.drawer_item_logout).setVisible(false);

    }

    @Override
    public void onNavigationDrawerItemSelected(int itemId) {
        MenuItem item = mNavigationView.getMenu().findItem(itemId);
        if(item != null) {
            item.setChecked(true);
        }
    }

    public void setSelection(int itemId) {
        mViewModel.setSelection(itemId);
    }

    public void enableDrawer(boolean enable) {
        mDrawerLayout.setDrawerLockMode(enable ? DrawerLayout.LOCK_MODE_UNLOCKED
                : DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    public void closeDrawer() {
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    public void restoreState(Bundle savedInstanceState) {
        mViewModel.restoreState(savedInstanceState);
    }

    public void saveState(Bundle outState) {
        mViewModel.saveState(outState);
    }

    @Override
    public void loggedIn(User user) {

        UiUtils.hideKeyboard(mainActivity);

        Menu menu = mNavigationView.getMenu();
        menu.findItem(R.id.drawer_item_logout).setVisible(true);

        mHeaderLoggedIn.setVisibility(View.VISIBLE);
        mUsernameLoggedIn.setText(user.getUsername());
    }

    @Override
    public void loggedOut() {
        Menu menu = mNavigationView.getMenu();
        menu.findItem(R.id.drawer_item_logout).setVisible(false);
        mHeaderLoggedIn.setVisibility(View.GONE);
    }
}
