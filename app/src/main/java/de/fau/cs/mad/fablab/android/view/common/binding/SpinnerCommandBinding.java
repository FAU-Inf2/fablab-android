package de.fau.cs.mad.fablab.android.view.common.binding;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.CommandListener;

public class SpinnerCommandBinding implements Binding<Spinner, Integer>, CommandListener,
        AdapterView.OnItemSelectedListener {
    private Command<Integer> mCommand;
    private Spinner mSpinner;

    @Override
    public void bind(Spinner spinner, Command<Integer> command) {
        mCommand = command;
        mSpinner = spinner;

        mCommand.setListener(this);
        mSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onIsAvailableChanged(boolean newValue) {
        mSpinner.setVisibility(newValue ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onIsExecutableChanged(boolean newValue) {
        mSpinner.setEnabled(newValue);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (mCommand.isExecutable()) {
            mCommand.execute(position);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
