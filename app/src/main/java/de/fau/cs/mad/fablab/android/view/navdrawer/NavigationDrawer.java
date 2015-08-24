package de.fau.cs.mad.fablab.android.view.navdrawer;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.view.activities.MainActivity;
import de.fau.cs.mad.fablab.android.view.common.binding.MenuItemCommandBinding;
import de.fau.cs.mad.fablab.android.view.common.binding.ViewCommandBinding;
import de.fau.cs.mad.fablab.rest.core.Roles;
import de.fau.cs.mad.fablab.rest.core.User;

public class NavigationDrawer implements NavigationDrawerViewModel.Listener {
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.navigation)
    NavigationView mNavigationView;

    // views for login
    @Bind(R.id.navdrawer_header_login)
    LinearLayout mHeaderLogin;
    @Bind(R.id.drupal_login_button)
    Button mLoginButton;
    @Bind(R.id.drupal_login_username)
    EditText mUsernameET;
    @Bind(R.id.drupal_login_password)
    EditText mPasswordET;

    // views when logged in
    @Bind(R.id.navdrawer_header_loggedin)
    LinearLayout mHeaderLoggedIn;
    @Bind(R.id.navdrawer_name)
    TextView mUsernameLoggedIn;


    @Inject
    NavigationDrawerViewModel mViewModel;

    public NavigationDrawer(MainActivity activity, View view) {
        ButterKnife.bind(this, view);
        activity.inject(this);

        mViewModel.setListener(this);

        Menu menu = mNavigationView.getMenu();
        new MenuItemCommandBinding().bind(menu.findItem(R.id.drawer_item_news),
                mViewModel.getNavigateToNewsCommand());
        new MenuItemCommandBinding().bind(menu.findItem(R.id.drawer_item_productsearch),
                mViewModel.getNavigateToProductSearchCommand());
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
        new ViewCommandBinding().bind(mLoginButton, mViewModel.getLoginCommand());

        menu.findItem(R.id.drawer_item_logout).setVisible(false);
        menu.findItem(R.id.drawer_item_inventory).setVisible(false);

        //TODO comment following two lines for login
        mHeaderLogin.setVisibility(View.GONE);
        mHeaderLoggedIn.setVisibility(View.GONE);
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

    @Override
    public String getUsername()
    {
        if(mUsernameET != null)
        {
            return mUsernameET.getText().toString();
        }
        return null;
    }

    @Override
    public String getPassword()
    {
        if(mPasswordET != null)
        {
            return mPasswordET.getText().toString();
        }
        return null;
    }

    @Override
    public void loggedIn(User user) {
        Menu menu = mNavigationView.getMenu();
        menu.findItem(R.id.drawer_item_logout).setVisible(true);

        if(user.getRoles().contains(Roles.INVENTORY))
        {
            menu.findItem(R.id.drawer_item_inventory).setVisible(true);
        }

        mHeaderLogin.setVisibility(View.GONE);
        mHeaderLoggedIn.setVisibility(View.VISIBLE);

        mUsernameLoggedIn.setText(user.getUsername());
    }

    @Override
    public void loggedOut(User user) {
        Menu menu = mNavigationView.getMenu();
        menu.findItem(R.id.drawer_item_logout).setVisible(false);

        if(user.getRoles().contains(Roles.INVENTORY))
        {
            menu.findItem(R.id.drawer_item_inventory).setVisible(false);
        }

        mHeaderLogin.setVisibility(View.VISIBLE);
        mHeaderLoggedIn.setVisibility(View.GONE);
        mPasswordET.setText("");
        mUsernameET.setText("");
    }
}
