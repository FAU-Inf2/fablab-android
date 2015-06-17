package de.fau.cs.mad.fablab.android.navdrawer;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.view.MainActivity;
import de.fau.cs.mad.fablab.android.view.common.binding.NavigationViewCommandBinding;

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
        new NavigationViewCommandBinding().bind(navigationView, mViewModel.getNavigationItemSelectedCommand());
    }

    @Override
    public void onNavigationDrawerItemSelected() {
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }
}
