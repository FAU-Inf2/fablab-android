package de.fau.cs.mad.fablab.android.view.binding;

import android.view.View;
import android.widget.Button;

import de.fau.cs.mad.fablab.android.viewmodel.Command;
import de.fau.cs.mad.fablab.android.viewmodel.CommandListener;

public class ButtonBinding extends Binding implements CommandListener, View.OnClickListener {


    private final View button;
    private final Command<?> command;

    public ButtonBinding(Button button, Command command)
    {
        if(button == null)
        {
            throw new IllegalArgumentException("button must not be null");
        }
        if(command == null)
        {
            throw new IllegalArgumentException("command must not be null");
        }

        this.button = button;
        this.command = command;

        this.button.setOnClickListener(this);
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
