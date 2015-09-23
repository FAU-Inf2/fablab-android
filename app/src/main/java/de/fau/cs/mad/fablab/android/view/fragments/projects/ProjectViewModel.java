package de.fau.cs.mad.fablab.android.view.fragments.projects;

import de.fau.cs.mad.fablab.android.viewmodel.common.Project;

public class ProjectViewModel {

    private Project mProject;

    public ProjectViewModel(Project project)
    {
        mProject = project;
    }

    public String getTitle()
    {
        return mProject.getProjectFile().getFilename();
    }

    public String getShortDescription()
    {
        return mProject.getProjectFile().getDescription();
    }
}
