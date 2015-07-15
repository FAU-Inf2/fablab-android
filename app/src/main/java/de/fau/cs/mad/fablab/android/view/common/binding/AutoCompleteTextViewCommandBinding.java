package de.fau.cs.mad.fablab.android.view.common.binding;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;

public class AutoCompleteTextViewCommandBinding implements Binding<AutoCompleteTextView, String>,
        AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {

    private Command<String> mCommand;

    @Override
    public void bind(AutoCompleteTextView autoCompleteTextView, Command<String> command) {

        mCommand = command;

        autoCompleteTextView.setOnItemClickListener(this);
        autoCompleteTextView.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        mCommand.execute(String.valueOf(adapterView.getItemAtPosition(i)));
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        mCommand.execute(String.valueOf(adapterView.getItemAtPosition(i)));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

}
