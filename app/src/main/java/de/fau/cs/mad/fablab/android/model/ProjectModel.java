package de.fau.cs.mad.fablab.android.model;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import de.fau.cs.mad.fablab.android.model.events.ProjectDeletedEvent;
import de.fau.cs.mad.fablab.android.model.events.ProjectImageUploadedEvent;
import de.fau.cs.mad.fablab.android.model.events.ProjectSavedEvent;
import de.fau.cs.mad.fablab.android.viewmodel.common.Project;
import de.fau.cs.mad.fablab.rest.core.ProjectFile;
import de.fau.cs.mad.fablab.rest.core.ProjectImageUpload;
import de.fau.cs.mad.fablab.rest.myapi.ProjectsApi;
import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ProjectModel {

    private ProjectsApi mProjectsApi;
    private RuntimeExceptionDao<Project, Long> mProjectDao;
    private EventBus mEventBus = EventBus.getDefault();

    private Callback<String> mSaveProjectCallback = new Callback<String>() {
        @Override
        public void success(String id, Response response) {
            mEventBus.post(new ProjectSavedEvent(id, true));
        }

        @Override
        public void failure(RetrofitError error) {
            mEventBus.post(new ProjectSavedEvent(null, false));
        }
    };

    private Callback<Response> mDeleteProjectCallback = new Callback<Response>() {
        @Override
        public void success(Response id, Response response) {
            mEventBus.post(new ProjectDeletedEvent(true));
        }

        @Override
        public void failure(RetrofitError error) {
            mEventBus.post(new ProjectDeletedEvent(false));
        }
    };

    private Callback<String> mUpdateProjectCallback = new Callback<String>() {
        @Override
        public void success(String id, Response response) {
            mEventBus.post(new ProjectSavedEvent(id, true));
        }

        @Override
        public void failure(RetrofitError error) {
            mEventBus.post(new ProjectSavedEvent(null, false));
        }
    };

    private Callback<String> mUploadImageCallback = new Callback<String>() {
        @Override
        public void success(String filePath, Response response) {
            mEventBus.post(new ProjectImageUploadedEvent(filePath, true));
        }

        @Override
        public void failure(RetrofitError error) {
            mEventBus.post(new ProjectImageUploadedEvent(null, false));
        }
    };

    public ProjectModel(ProjectsApi projectsApi, RuntimeExceptionDao<Project, Long> projectDao) {
        mProjectsApi = projectsApi;
        mProjectDao = projectDao;
    }

    public void saveProject(Project project)
    {
        mProjectDao.createOrUpdate(project);
        mEventBus.post(new ProjectSavedEvent(null, true));
    }

    public void uploadProjectGithub(Project project)
    {
        ProjectFile projectFile = project.getProjectFile();
        if(!projectFile.getFilename().contains(".md")) {
            projectFile.setFilename(projectFile.getFilename() + ".md");
        }
        if(project.getGistID() == null)
        {
            mProjectsApi.createProject(projectFile, mSaveProjectCallback);
        }
        else
        {
            mProjectsApi.updateProject(project.getGistID(), projectFile, mUpdateProjectCallback);
        }
    }

    public void uploadImage(ProjectImageUpload imageUpload)
    {
        mProjectsApi.uploadImage(imageUpload, mUploadImageCallback);
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

    public void deleteProject(Project project)
    {
        if(project.getGistID() != null)
        {
            mProjectDao.delete(project);
            mProjectsApi.deleteProject(project.getGistID(), mDeleteProjectCallback);
        }
        else
        {
            mProjectDao.delete(project);
            mEventBus.post(new ProjectDeletedEvent(true));
        }
    }
}
