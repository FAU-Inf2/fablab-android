package de.fau.cs.mad.fablab.android.model.events;

import de.fau.cs.mad.fablab.android.viewmodel.common.Project;

public class DeleteProjectEvent {

    private boolean mDelete;
    private int mDeletePosition;
    private Project mProject;

    public DeleteProjectEvent(boolean delete)
    {
        mDelete = delete;
    }

    public DeleteProjectEvent(boolean delete, int deletePosition, Project project)
    {
        mDelete = delete;
        mDeletePosition = deletePosition;
        mProject = project;
    }

    public void setDelete(boolean delete)
    {
        mDelete = delete;
    }

    public boolean getDelete()
    {
        return mDelete;
    }

    public void setDeletePosition(int deletePosition)
    {
        mDeletePosition = deletePosition;
    }

    public int getDeletePosition()
    {
        return mDeletePosition;
    }

    public void setProject(Project project)
    {
        mProject = project;
    }

    public Project getProject()
    {
        return mProject;
    }
}
