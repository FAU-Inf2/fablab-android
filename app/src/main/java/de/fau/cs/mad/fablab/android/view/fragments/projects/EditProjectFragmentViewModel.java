package de.fau.cs.mad.fablab.android.view.fragments.projects;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.model.ProjectModel;
import de.fau.cs.mad.fablab.android.model.events.ProjectImageUploadedEvent;
import de.fau.cs.mad.fablab.android.viewmodel.common.Project;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.fau.cs.mad.fablab.rest.core.ProjectFile;
import de.fau.cs.mad.fablab.rest.core.ProjectImageUpload;
import de.greenrobot.event.EventBus;

public class EditProjectFragmentViewModel {

    private Listener mListener;
    private Project mProject;
    private ProjectModel mModel;
    private EventBus mEventBus = EventBus.getDefault();

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

    private Command<Void> mAddPhotoGalleryCommand = new Command<Void>() {
        @Override
        public void execute(Void parameter) {
            if(mListener != null) {
                ProjectFile projectFile = createProjectFile();
                if(mProject == null)
                {
                    mProject = new Project();
                }
                mProject.setProjectFile(projectFile);
                if (getProject().getGistID() == null)
                {
                    mListener.projectNotUploaded();
                }
                else
                {
                    mListener.startPicturePicker();
                }
            }
        }
    };

    private Command<Void> mAddPhotoCameraCommand = new Command<Void>() {
        @Override
        public void execute(Void parameter) {
            if(mListener != null) {
                ProjectFile projectFile = createProjectFile();
                if(mProject == null)
                {
                    mProject = new Project();
                }
                mProject.setProjectFile(projectFile);
                if (getProject().getGistID() == null)
                {
                    mListener.projectNotUploaded();
                }
                else
                {
                    mListener.startCamera();
                }
            }
        }
    };

    @Inject
    public EditProjectFragmentViewModel(ProjectModel projectModel)
    {
        mModel = projectModel;
        mEventBus.register(this);
    }

    public Command<Void> getSaveProjectCommand()
    {
        return mSaveProjectCommand;
    }

    public Command<Void> getAddPhotoCameraCommand()
    {
        return mAddPhotoCameraCommand;
    }

    public Command<Void> getAddPhotoGalleryCommand()
    {
        return mAddPhotoGalleryCommand;
    }

    public void setListener(Listener listener)
    {
        mListener = listener;
    }

    public void setProject(Project project)
    {
        mProject = project;
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

    public void uploadImage(ProjectImageUpload imageUpload)
    {
        mModel.uploadImage(imageUpload);
    }


    @SuppressWarnings("unused")
    public void onEvent(ProjectImageUploadedEvent event) {
        if(mListener != null)
        {
            mListener.showProgressBar(false);
        }
        if(event.getSuccess())
        {
            if(event.getFilePath() != null)
            {
                if(mListener != null)
                {
                    String text = mListener.getText();
                    text = text + "\n";
                    text = text + "![titel](" + event.getFilePath() + ")";
                    text = text + "\n";
                    mListener.setDescription(text);
                }
            }
        }
        else
        {
            if(mListener != null)
            {
                mListener.uploadFailure();
            }
        }
    }

    public interface Listener{
        void onSaveProjectClicked();
        String getTitle();
        String getShortDescription();
        String getText();
        void startPicturePicker();
        void startCamera();
        void projectNotUploaded();
        void showProgressBar(boolean show);
        void uploadFailure();
        void setDescription(String text);
    }
}
