package de.fau.cs.mad.fablab.android.view.fragments.projects;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;

public class ProjectFragmentViewModel {

    private Listener mListener;

    private Command<Void> mNewProjectCommand = new Command<Void>() {
        @Override
        public void execute(Void parameter) {
            if(mListener != null) {
                mListener.onNewProjectClicked();
            }
        }
    };

    @Inject
    ProjectFragmentViewModel()
    {

    }

    public Command<Void> getNewProjectCommand()
    {
        return mNewProjectCommand;
    }

    public void setListener(Listener listener)
    {
        mListener = listener;
    }

    public interface Listener{
        void onNewProjectClicked();
    }
}
