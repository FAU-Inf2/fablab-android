package de.fau.cs.mad.fablab.android.view.fragments.projects;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.view.common.fragments.BaseFragment;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;

public class EditProjectFragmentViewModel extends BaseFragment {

    private Listener mListener;

    private Command<Void> mSaveProjectCommand = new Command<Void>() {
        @Override
        public void execute(Void parameter) {
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

    public interface Listener{
        void onSaveProjectClicked();
    }
}
