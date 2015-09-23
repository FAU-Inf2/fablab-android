package de.fau.cs.mad.fablab.android.view.fragments.projects;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.model.ProjectModel;
import de.fau.cs.mad.fablab.android.viewmodel.common.Project;

public class SaveProjectDialogFragmentViewModel {

    private ProjectModel mModel;
    private Listener mListener;
    private Project mProject;

    @Inject
    public SaveProjectDialogFragmentViewModel(ProjectModel projectModel)
    {
        mModel = projectModel;
    }

    public void setListener(Listener listener)
    {
        mListener = listener;
    }

    public void setProject(Project project)
    {
        mProject = project;
        System.out.println(mProject);
    }

    public interface Listener{

    }
}
