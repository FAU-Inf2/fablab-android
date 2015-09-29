package de.fau.cs.mad.fablab.android.model.events;

public class CC0LicenseEvent {

    private boolean mSuccess;

    public CC0LicenseEvent(boolean success)
    {
        mSuccess = success;
    }

    public boolean getState()
    {
        return mSuccess;
    }
}
