package de.fau.cs.mad.fablab.android.view.common.binding;

import android.view.KeyEvent;
import android.view.View;

import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;

public class EnterKeyCommandBinding  implements Binding<View, Void>, View.OnKeyListener {
    private Command<Void> mCommand;

    @Override
    public void bind(View view, Command<Void> command) {
        mCommand = command;

        view.setOnKeyListener(this);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        // If the event is a key-down event on the "enter" button
        if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)
                && mCommand.isExecutable()) {
            mCommand.execute(null);
            return true;
        }
        return false;
    }
}