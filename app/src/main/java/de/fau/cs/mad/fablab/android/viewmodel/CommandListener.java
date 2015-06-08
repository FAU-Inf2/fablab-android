package de.fau.cs.mad.fablab.android.viewmodel;

public interface CommandListener {

    void onIsAvailableChanged(boolean newValue);

    void onIsExecutableChanged(boolean newValue);
}
