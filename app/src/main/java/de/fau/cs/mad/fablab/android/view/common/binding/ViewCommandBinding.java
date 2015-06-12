package de.fau.cs.mad.fablab.android.view.common.binding;

import android.view.View;

import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.CommandListener;

/***
 *
 */
public class ViewCommandBinding extends Binding implements CommandListener, View.OnClickListener {


    private final View view;
    private final Command<?> command;

    public ViewCommandBinding(View view, Command command)
    {
        if(view == null)
        {
            throw new IllegalArgumentException("button must not be null");
        }
        if(command == null)
        {
            throw new IllegalArgumentException("command must not be null");
        }

        this.view = view;
        this.command = command;

        this.view.setOnClickListener(this);
    }

    @Override
    public void onIsAvailableChanged(boolean newValue) {

    }

    @Override
    public void onIsExecutableChanged(boolean newValue) {

    }

    @Override
    public void onClick(View v) {
        if (command.isExecutable()) {
            command.execute(null);
        }
    }
}
