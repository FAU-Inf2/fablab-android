package de.fau.cs.mad.fablab.android.view.fragments.projects;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.model.ProjectModel;
import de.fau.cs.mad.fablab.android.model.events.CC0LicenseEvent;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.greenrobot.event.EventBus;

public class LicenseInformationDialogFragmentViewModel {

    private Listener mListener;
    private ProjectModel mModel;
    private EventBus mEventBus = EventBus.getDefault();

    private Command<Void> mOKCommand = new Command<Void>() {
        @Override
        public void execute(Void parameter) {
            if(mListener != null) {
                mListener.buttonClicked();
            }
            mEventBus.post(new CC0LicenseEvent(true));
        }
    };

    private Command<Void> mCancelCommand = new Command<Void>() {
        @Override
        public void execute(Void parameter) {
            if(mListener != null) {
                mListener.buttonClicked();
            }
            mEventBus.post(new CC0LicenseEvent(false));
        }
    };

    @Inject
    public LicenseInformationDialogFragmentViewModel(ProjectModel model)
    {
        mModel = model;
    }

    public Command<Void> getOKCommand()
    {
        return mOKCommand;
    }

    public Command<Void> getCancelCommand()
    {
        return mCancelCommand;
    }

    public void setListener(Listener listener)
    {
        mListener = listener;
    }

    public interface Listener{
        void buttonClicked();
    }
}
