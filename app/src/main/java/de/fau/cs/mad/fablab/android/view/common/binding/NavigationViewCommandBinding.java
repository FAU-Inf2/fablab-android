package de.fau.cs.mad.fablab.android.view.common.binding;

import android.support.design.widget.NavigationView;
import android.view.MenuItem;

import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.CommandListener;


public class NavigationViewCommandBinding implements Binding<NavigationView, Integer>, CommandListener,
        NavigationView.OnNavigationItemSelectedListener {

    NavigationView mNavigationView;
    Command mCommand;

    @Override
    public void bind(NavigationView navigationView, Command<Integer> command) {
        this.mNavigationView = navigationView;
        this.mCommand = command;
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onIsAvailableChanged(boolean newValue) {

    }

    @Override
    public void onIsExecutableChanged(boolean newValue) {

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        menuItem.setChecked(true);
        if (mCommand.isExecutable()) {
            mCommand.execute(menuItem.getItemId());
        }
        return true;
    }
}
