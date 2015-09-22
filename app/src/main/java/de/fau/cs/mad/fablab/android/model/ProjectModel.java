package de.fau.cs.mad.fablab.android.model;

import de.fau.cs.mad.fablab.rest.myapi.ProjectsApi;

public class ProjectModel {

    private ProjectsApi mProjectsApi;

    public ProjectModel(ProjectsApi projectsApi) {
        mProjectsApi = projectsApi;
    }
}
