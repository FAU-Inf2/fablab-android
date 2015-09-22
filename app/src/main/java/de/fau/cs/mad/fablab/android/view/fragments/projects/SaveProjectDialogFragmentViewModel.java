package de.fau.cs.mad.fablab.android.view.fragments.projects;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.model.ProjectModel;

public class SaveProjectDialogFragmentViewModel {

    private ProjectModel mModel;

    @Inject
    public SaveProjectDialogFragmentViewModel(ProjectModel projectModel)
    {
        mModel = projectModel;
    }
}
