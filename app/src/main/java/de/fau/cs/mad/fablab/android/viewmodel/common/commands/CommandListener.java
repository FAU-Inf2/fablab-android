package de.fau.cs.mad.fablab.android.viewmodel.common.commands;

public interface CommandListener {

    void onIsAvailableChanged(boolean newValue);

    void onIsExecutableChanged(boolean newValue);
}
