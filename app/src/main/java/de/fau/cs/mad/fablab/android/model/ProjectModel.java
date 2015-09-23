package de.fau.cs.mad.fablab.android.model;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
    }

    public List<Project> getAllProjects()
    {
        List<Project> fetchedProjects = new ArrayList<>();
        //get next Element_count elements from database
        QueryBuilder<Project, Long> queryBuilder = mProjectDao.queryBuilder();
        //sort elements in descending order according to last_updated
        queryBuilder.orderBy("last_updated", false);
        try {
            fetchedProjects = mProjectDao.query(queryBuilder.prepare());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fetchedProjects;
    }
}
