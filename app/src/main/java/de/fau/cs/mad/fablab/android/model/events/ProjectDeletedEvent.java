package de.fau.cs.mad.fablab.android.model.events;

public class ProjectDeletedEvent {

    private boolean mSuccess;

    public ProjectDeletedEvent(boolean success)
    {
        mSuccess = success;
    }

    public boolean getState()
    {
        return mSuccess;
    }

    public void setState(boolean success)
    {
        mSuccess = success;
    }
}
