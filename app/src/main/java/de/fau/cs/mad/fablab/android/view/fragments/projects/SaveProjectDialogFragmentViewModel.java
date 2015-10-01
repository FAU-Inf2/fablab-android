package de.fau.cs.mad.fablab.android.view.fragments.projects;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.model.ProjectModel;
import de.fau.cs.mad.fablab.android.model.events.CC0LicenseEvent;
import de.fau.cs.mad.fablab.android.model.events.ProjectSavedEvent;
import de.fau.cs.mad.fablab.android.viewmodel.common.Project;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.greenrobot.event.EventBus;

public class SaveProjectDialogFragmentViewModel {

    private ProjectModel mModel;
    private Listener mListener;
    private Project mProject;
    private EventBus mEventBus = EventBus.getDefault();

    private Command<Void> mSaveProjectLocallyCommand = new Command<Void>() {
        @Override
        public void execute(Void parameter) {
            if(mListener != null)
            {
                mListener.showProgressBar(true);
            }
            mProject.setLastUpdated(System.currentTimeMillis());
            mModel.saveProject(mProject);
        }
    };

    private Command<Void> mSaveProjectGithubCommand = new Command<Void>() {
        @Override
        public void execute(Void parameter) {
            if(mListener != null)
            {
                mListener.showLicenseInformation();
            }
        }
    };

    @Inject
    public SaveProjectDialogFragmentViewModel(ProjectModel projectModel)
    {
        mModel = projectModel;
        mEventBus.register(this);
    }

    public Command<Void> getSaveProjectLocallyCommand()
    {
        return mSaveProjectLocallyCommand;
    }

    public Command<Void> getSaveProjectGithubCommand()
    {
        return mSaveProjectGithubCommand;
    }

    public void setListener(Listener listener)
    {
        mListener = listener;
    }

    public void setProject(Project project)
    {
        mProject = project;
    }

    public void unregister()
    {
        if(mEventBus.isRegistered(this))
        {
            mEventBus.unregister(this);
        }
    }

    @SuppressWarnings("unused")
    public void onEvent(ProjectSavedEvent event) {
        if(mListener != null)
        {
            mListener.showProgressBar(false);
        }
        if(event.getSuccess())
        {
            if(event.getGistID() != null)
            {
                mProject.setGistID(event.getGistID());
                mModel.saveProject(mProject);
            }
            if(mListener != null)
            {
                mListener.saveSuccess();
            }
        }
        else
        {
            if(mListener != null)
            {
                mListener.saveFailure();
            }
        }
        if(mListener != null)
        {
            mListener.onSaveProjectClicked();
        }
    }

    @SuppressWarnings("unused")
    public void onEvent(CC0LicenseEvent event) {
        if(event.getState())
        {
            if(mListener != null)
            {
                mListener.showProgressBar(true);
            }
            mProject.setLastUpdated(System.currentTimeMillis());
            mModel.uploadProjectGithub(mProject);
        }
        else
        {
           if(mListener != null)
           {
               mListener.onSaveProjectClicked();
           }
        }
    }

    public interface Listener{
        void onSaveProjectClicked();
        void saveFailure();
        void saveSuccess();
        void showProgressBar(boolean show);
        void showLicenseInformation();
    }
}
