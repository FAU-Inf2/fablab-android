package de.fau.cs.mad.fablab.android.view.common.binding;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;

public class EditTextCommandBinding implements Binding<EditText, String>, TextWatcher {
    private Command<String> mCommand;

    @Override
    public void bind(EditText editText, Command<String> command) {
        mCommand = command;

        editText.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (mCommand.isExecutable()) {
            mCommand.execute(s.toString());
        }
    }
}
