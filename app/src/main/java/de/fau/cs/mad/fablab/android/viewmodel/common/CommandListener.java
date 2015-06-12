package de.fau.cs.mad.fablab.android.viewmodel.common;

public interface CommandListener {

    void onIsAvailableChanged(boolean newValue);

    void onIsExecutableChanged(boolean newValue);
}
