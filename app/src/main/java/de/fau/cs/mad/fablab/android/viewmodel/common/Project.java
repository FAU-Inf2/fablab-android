package de.fau.cs.mad.fablab.android.viewmodel.common;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

import de.fau.cs.mad.fablab.rest.core.ProjectFile;

@DatabaseTable(tableName = "project")
public class Project implements Serializable {

    @DatabaseField(columnName = "id_project", generatedId = true)
    private long mID;
    @DatabaseField(columnName = "project_file", canBeNull = false, dataType = DataType.SERIALIZABLE)
    private ProjectFile mProjectFile;
    @DatabaseField(columnName = "gist_id")
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

    public Project()
    {

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

    public long getID()
    {
        return mID;
    }

    public void setID(long id)
    {
        mID = id;
    }

    @Override
    public String toString() {
        return "Project{" + "mProjectFile=" + mProjectFile + ", mGistID='" + mGistID + '\'' + '}';
    }
}
