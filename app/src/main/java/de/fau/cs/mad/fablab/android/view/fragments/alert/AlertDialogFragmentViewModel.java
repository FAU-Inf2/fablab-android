package de.fau.cs.mad.fablab.android.view.fragments.alert;

import java.util.List;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.model.DrupalModel;
import de.fau.cs.mad.fablab.android.model.FablabMailModel;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.fau.cs.mad.fablab.rest.core.FabTool;

public class AlertDialogFragmentViewModel {

    private DrupalModel mDrupalModel;
    private FablabMailModel mFablablMailModel;
    private Listener mListener;

    private Command<Void> mOKCommand = new Command<Void>()
    {
        @Override
        public void execute(Void parameter) {
            if (mListener != null) {
                mListener.onOK();
            }
        }
    };

    private Command<Void> mCancelCommand = new Command<Void>()
    {
        @Override
        public void execute(Void parameter) {
            if (mListener != null) {
                mListener.onCancel();
            }
        }
    };

    @Inject
    public AlertDialogFragmentViewModel(DrupalModel drupalModel, FablabMailModel fablabMailModel)
    {
        mDrupalModel = drupalModel;
        mFablablMailModel = fablabMailModel;
    }

    public void setListener(Listener listener)
    {
        mListener = listener;
    }

    public Command<Void> getOKCommand()
    {
        return mOKCommand;
    }

    public Command<Void> getCancelCommand()
    {
        return mCancelCommand;
    }

    public List<FabTool> getTools()
    {
        return mDrupalModel.getFabTools();
    }

    public String getMailAddress()
    {
        return mFablablMailModel.getMailAddress();
    }

    public interface Listener
    {
        void onOK();

        void onCancel();
    }
}
