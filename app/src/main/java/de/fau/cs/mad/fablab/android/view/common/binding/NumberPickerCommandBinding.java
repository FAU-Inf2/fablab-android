package de.fau.cs.mad.fablab.android.view.common.binding;

import android.view.View;
import android.widget.NumberPicker;

import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.CommandListener;

public class NumberPickerCommandBinding implements Binding<NumberPicker, Integer>, CommandListener,
        NumberPicker.OnValueChangeListener {
    private Command<Integer> mCommand;
    private NumberPicker mNumberPicker;

    @Override
    public void bind(NumberPicker numberPicker, Command<Integer> command) {
        mCommand = command;
        mNumberPicker = numberPicker;

        mNumberPicker.setOnValueChangedListener(this);
    }

    @Override
    public void onIsAvailableChanged(boolean newValue) {
        mNumberPicker.setVisibility(newValue ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onIsExecutableChanged(boolean newValue) {
        mNumberPicker.setEnabled(newValue);
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        if (mCommand.isExecutable()) {
            mCommand.execute(newVal);
        }
    }
}
