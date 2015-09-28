package de.fau.cs.mad.fablab.android.view.fragments.projects;

import de.fau.cs.mad.fablab.android.viewmodel.common.Project;

public class ProjectClickedEvent {

    private final Project mProject;

    public ProjectClickedEvent(Project project)
    {
        mProject = project;
    }

    public Project getProject()
    {
        return mProject;
    }
}
