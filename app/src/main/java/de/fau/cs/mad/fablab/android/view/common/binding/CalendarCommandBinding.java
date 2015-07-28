package de.fau.cs.mad.fablab.android.view.common.binding;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;

import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.CommandListener;

public class CalendarCommandBinding implements Binding<View, Intent>, CommandListener,
        View.OnClickListener {

    private Command<Intent> mCommand;
    private View mView;

    @Override
    public void bind(View view, Command<Intent> command)
    {
        mCommand = command;
        mView = view;

        mCommand.setListener(this);
        mView.setOnClickListener(this);
    }

    @Override
    public void onIsAvailableChanged(boolean newValue)
    {
        mView.setVisibility(newValue ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onIsExecutableChanged(boolean newValue)
    {
        mView.setEnabled(newValue);
    }

    @Override
    public void onClick(View v)
    {
        if (mCommand.isExecutable())
        {
            mCommand.execute(null);
        }
    }
}

