package de.fau.cs.mad.fablab.android.view.fragments.projects;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.model.ProjectModel;
import de.fau.cs.mad.fablab.android.viewmodel.common.Project;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;

public class SaveProjectDialogFragmentViewModel {

    private ProjectModel mModel;
    private Listener mListener;
    private Project mProject;

    private Command<Void> mSaveProjectLocallyCommand = new Command<Void>() {
        @Override
        public void execute(Void parameter) {
            mProject.setLastUpdated(System.currentTimeMillis());
            mModel.saveProject(mProject);

            if(mListener != null) {
                mListener.onSaveProjectLocallyClicked();
            }
        }
    };

    @Inject
    public SaveProjectDialogFragmentViewModel(ProjectModel projectModel)
    {
        mModel = projectModel;
    }

    public Command<Void> getSaveProjectLocallyCommand()
    {
        return mSaveProjectLocallyCommand;
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
        void onSaveProjectLocallyClicked();
    }
}
