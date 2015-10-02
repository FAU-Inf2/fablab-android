package de.fau.cs.mad.fablab.android.view.fragments.projects;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.model.events.DeleteProjectEvent;
import de.fau.cs.mad.fablab.android.viewmodel.common.Project;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.greenrobot.event.EventBus;

public class ConfirmDeleteDialogFragmentViewModel {

    private Listener mListener;
    private EventBus mEventBus = EventBus.getDefault();
    private int mDeletePosition = -1;
    private Project mProject;

    private Command<Void> mDeleteProjectCommand = new Command<Void>() {
        @Override
        public void execute(Void parameter) {
            if(mListener != null)
            {
                mEventBus.post(new DeleteProjectEvent(true, getDeletePosition(), getProject()));
                mListener.onButtonClicked();
            }
        }
    };

    private Command<Void> mNotDeleteProjectCommand = new Command<Void>() {
        @Override
        public void execute(Void parameter) {
            if(mListener != null)
            {
                mEventBus.post(new DeleteProjectEvent(false, getDeletePosition(), getProject()));
                mListener.onButtonClicked();
            }
        }
    };

    @Inject
    public ConfirmDeleteDialogFragmentViewModel()
    {

    }

    public Command<Void> getDeleteProjectCommand()
    {
        return mDeleteProjectCommand;
    }

    public Command<Void> getNotDeleteProjectCommand()
    {
        return mNotDeleteProjectCommand;
    }

    public void setDeletePosition(int deletePosition)
    {
        mDeletePosition = deletePosition;
    }

    public int getDeletePosition()
    {
        return mDeletePosition;
    }

    public void setProject(Project project)
    {
        mProject = project;
    }

    public Project getProject()
    {
        return mProject;
    }

    public void setListener(Listener listener)
    {
        mListener = listener;
    }

    public interface Listener{
        void onButtonClicked();
    }
}
