package de.fau.cs.mad.fablab.android.viewmodel.common;

import java.io.Serializable;

import de.fau.cs.mad.fablab.rest.core.ProjectFile;

public class Project implements Serializable {

    private ProjectFile mProjectFile;
    private String mGistID;

    public Project(ProjectFile projectFile, String gistID)
    {
        mProjectFile = projectFile;
        mGistID = gistID;
    }

    public Project(ProjectFile projectFile)
    {
        mProjectFile = projectFile;
    }

    public void setProjectFile(ProjectFile projectFile)
    {
        mProjectFile = projectFile;
    }

    public ProjectFile getProjectFile()
    {
        return mProjectFile;
    }

    public void setGistID(String gistID)
    {
        mGistID = gistID;
    }

    public String getGistID()
    {
        return mGistID;
    }

    @Override
    public String toString() {
        return "Project{" + "mProjectFile=" + mProjectFile + ", mGistID='" + mGistID + '\'' + '}';
    }
}
