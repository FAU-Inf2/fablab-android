package de.fau.cs.mad.fablab.android.model;

import com.j256.ormlite.dao.RuntimeExceptionDao;

import de.fau.cs.mad.fablab.android.viewmodel.common.Project;
import de.fau.cs.mad.fablab.rest.myapi.ProjectsApi;

public class ProjectModel {

    private ProjectsApi mProjectsApi;
    private RuntimeExceptionDao<Project, Long> mProjectDao;

    public ProjectModel(ProjectsApi projectsApi, RuntimeExceptionDao<Project, Long> projectDao) {
        mProjectsApi = projectsApi;
        mProjectDao = projectDao;
    }

    public void saveProject(Project project)
    {
        mProjectDao.createOrUpdate(project);
        System.out.println("SIZE PROJECT DAO: " + mProjectDao.countOf());
    }
}
