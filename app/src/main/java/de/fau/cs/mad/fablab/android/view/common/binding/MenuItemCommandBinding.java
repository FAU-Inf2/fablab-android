package de.fau.cs.mad.fablab.android.view.common.binding;

import android.view.MenuItem;

import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.CommandListener;

public class MenuItemCommandBinding implements Binding<MenuItem, Integer>, CommandListener,
        MenuItem.OnMenuItemClickListener {
    private Command<Integer> mCommand;
    private MenuItem mMenuItem;

    @Override
    public void bind(MenuItem menuItem, Command<Integer> command) {
        mCommand = command;
        mMenuItem = menuItem;

        mMenuItem.setOnMenuItemClickListener(this);
    }

    @Override
    public void onIsAvailableChanged(boolean newValue) {
        mMenuItem.setVisible(newValue);
    }

    @Override
    public void onIsExecutableChanged(boolean newValue) {
        mMenuItem.setEnabled(newValue);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (mCommand.isExecutable()) {
            mCommand.execute(item.getItemId());
        }
        return true;
    }
}
