package de.fau.cs.mad.fablab.android.model.events;

public class ProjectImageUploadedEvent {

    private String mFilePath;
    private boolean mSuccess;

    public ProjectImageUploadedEvent(String filePath, boolean success)
    {
        mFilePath = filePath;
        mSuccess = success;
    }

    public void setFilePath(String filePath)
    {
        mFilePath = filePath;
    }

    public String getFilePath()
    {
        return mFilePath;
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
