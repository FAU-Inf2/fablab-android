package de.fau.cs.mad.fablab.android.view.navdrawer;

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
import de.fau.cs.mad.fablab.android.view.MainActivity;
import de.fau.cs.mad.fablab.android.view.common.binding.MenuItemCommandBinding;

public class NavigationDrawer implements NavigationDrawerViewModel.Listener {

    @Inject
    NavigationDrawerViewModel mViewModel;

    @InjectView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @InjectView(R.id.navigation)
    NavigationView navigationView;

    public NavigationDrawer(MainActivity activity, View view) {
        ButterKnife.inject(this, view);
        activity.inject(this);

        mViewModel.setListener(this);
        Menu menu = navigationView.getMenu();

        new MenuItemCommandBinding().bind(menu.findItem(R.id.drawer_item_news), mViewModel.getNavigateToNewsCommand());
        new MenuItemCommandBinding().bind(menu.findItem(R.id.drawer_item_productsearch), mViewModel.getNavigateToProductSearchCommand());
        new MenuItemCommandBinding().bind(menu.findItem(R.id.drawer_item_scanner), mViewModel.getNavigateToBarcodeScannerCommand());
    }

    @Override
    public void onNavigationDrawerItemSelected(int itemId) {
        MenuItem item = navigationView.getMenu().findItem(itemId);
        if(item != null) item.setChecked(true);
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

}
