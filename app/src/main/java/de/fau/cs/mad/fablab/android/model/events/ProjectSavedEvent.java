package de.fau.cs.mad.fablab.android.model.events;

public class ProjectSavedEvent {

    private String mGistID;
    private boolean mSuccess;

    public ProjectSavedEvent(String gistID, boolean success)
    {
        mGistID = gistID;
        mSuccess = success;
    }

    public void setGistID(String gistID)
    {
        mGistID = gistID;
    }

    public String getGistID()
    {
        return mGistID;
    }

    public void setSuccess(boolean success)
    {
        mSuccess = success;
    }

    public boolean getSuccess()
    {
        return mSuccess;
    }
}
