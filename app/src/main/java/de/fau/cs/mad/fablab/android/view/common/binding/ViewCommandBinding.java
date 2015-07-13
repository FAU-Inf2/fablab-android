package de.fau.cs.mad.fablab.android.view.common.binding;

import android.view.View;

import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.CommandListener;

public class ViewCommandBinding implements Binding<View, Void>, CommandListener,
        View.OnClickListener {
    private Command<Void> mCommand;
    private View mView;

    @Override
    public void bind(View view, Command<Void> command) {
        mCommand = command;
        mView = view;

        mCommand.setListener(this);
        mView.setOnClickListener(this);
    }

    @Override
    public void onIsAvailableChanged(boolean newValue) {
        mView.setVisibility(newValue ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onIsExecutableChanged(boolean newValue) {
        mView.setEnabled(newValue);
    }

    @Override
    public void onClick(View v) {
        if (mCommand.isExecutable()) {
            mCommand.execute(null);
        }
    }
}
