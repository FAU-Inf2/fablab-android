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

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.model.events.UserRetrievedEvent;
import de.fau.cs.mad.fablab.android.view.activities.MainActivity;
import de.fau.cs.mad.fablab.android.view.common.binding.MenuItemCommandBinding;
import de.fau.cs.mad.fablab.android.view.common.binding.ViewCommandBinding;
import de.fau.cs.mad.fablab.rest.core.Roles;
import de.fau.cs.mad.fablab.rest.core.User;
import de.greenrobot.event.EventBus;

public class NavigationDrawer implements NavigationDrawerViewModel.Listener {
    @InjectView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @InjectView(R.id.navigation)
    NavigationView mNavigationView;

    // views for login
    @InjectView(R.id.navdrawer_header_login)
    LinearLayout mHeaderLogin;
    @InjectView(R.id.drupal_login_button)
    Button mLoginButton;
    @InjectView(R.id.drupal_login_username)
    EditText mUsernameET;
    @InjectView(R.id.drupal_login_password)
    EditText mPasswordET;

    // views when logged in
    @InjectView(R.id.navdrawer_header_loggedin)
    LinearLayout mHeaderLoggedIn;
    @InjectView(R.id.navdrawer_name)
    TextView mUsernameLoggedIn;


    @Inject
    NavigationDrawerViewModel mViewModel;

    private User mUser;
    private EventBus mEventBus = EventBus.getDefault();

    public NavigationDrawer(MainActivity activity, View view) {
        ButterKnife.inject(this, view);
        activity.inject(this);

        mViewModel.setListener(this);

        mEventBus.register(this);

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


    public void onEvent(UserRetrievedEvent event) {
        mUser = event.getUser();
        Menu menu = mNavigationView.getMenu();
        menu.findItem(R.id.drawer_item_logout).setVisible(true);

        if(mUser.getRoles().contains(Roles.INVENTORY))
        {
            menu.findItem(R.id.drawer_item_inventory).setVisible(true);
        }

        mHeaderLogin.setVisibility(View.GONE);
        mHeaderLoggedIn.setVisibility(View.VISIBLE);

        mUsernameLoggedIn.setText(mUser.getUsername());
    }

    @Override
    public void loggedOut() {
        Menu menu = mNavigationView.getMenu();
        menu.findItem(R.id.drawer_item_logout).setVisible(false);

        if(mUser.getRoles().contains(Roles.INVENTORY))
        {
            menu.findItem(R.id.drawer_item_inventory).setVisible(false);
        }

        mHeaderLogin.setVisibility(View.VISIBLE);
        mHeaderLoggedIn.setVisibility(View.GONE);
    }
}
