package de.fau.cs.mad.fablab.android.view.common.binding;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;

public class ChangeAmountByOneCommand implements Binding<Button, Void>, View.OnClickListener
{
    private Command<Void> mCommand;

    @Override
    public void bind(Button button, Command<Void> command)
    {
        mCommand = command;

        button.setOnClickListener(this);
    }


    @Override
    public void onClick(View view)
    {
        if (mCommand.isExecutable())
        {
            mCommand.execute(null);
        }
    }
}
