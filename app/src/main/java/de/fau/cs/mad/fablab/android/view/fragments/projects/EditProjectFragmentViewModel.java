package de.fau.cs.mad.fablab.android.view.fragments.projects;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.view.common.fragments.BaseFragment;
import de.fau.cs.mad.fablab.android.viewmodel.common.Project;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.fau.cs.mad.fablab.rest.core.ProjectFile;

public class EditProjectFragmentViewModel extends BaseFragment {

    private Listener mListener;
    private Project mProject;

    private Command<Void> mSaveProjectCommand = new Command<Void>() {
        @Override
        public void execute(Void parameter) {
            ProjectFile projectFile = createProjectFile();
            if(mProject == null)
            {
                mProject = new Project();
            }
            mProject.setProjectFile(projectFile);

            if(mListener != null) {
                mListener.onSaveProjectClicked();
            }
        }
    };

    @Inject
    public EditProjectFragmentViewModel()
    {

    }

    public Command<Void> getSaveProjectCommand()
    {
        return mSaveProjectCommand;
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

    public Project getProject()
    {
        return mProject;
    }

    private ProjectFile createProjectFile()
    {
        if(mListener != null)
        {
            return new ProjectFile(mListener.getShortDescription(), mListener.getTitle(),
                    mListener.getText());
        }
        else
        {
            return null;
        }
    }

    public interface Listener{
        void onSaveProjectClicked();
        String getTitle();
        String getShortDescription();
        String getText();
    }
}
