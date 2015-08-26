package de.fau.cs.mad.fablab.android.view.fragments.alert;

import java.util.List;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.model.DrupalModel;
import de.fau.cs.mad.fablab.android.model.MailModel;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.fau.cs.mad.fablab.rest.core.FabTool;

public class AlertDialogFragmentViewModel {

    private DrupalModel mDrupalModel;
    private MailModel mMailModel;
    private Listener mListener;

    private Command<Void> mOKCommand = new Command<Void>() {
        @Override
        public void execute(Void parameter) {
            if (mListener != null) {
                mListener.onOK();
            }
        }
    };

    @Inject
    public AlertDialogFragmentViewModel(DrupalModel drupalModel, MailModel mailModel) {
        mDrupalModel = drupalModel;
        mMailModel = mailModel;
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public Command<Void> getOKCommand() {
        return mOKCommand;
    }

    public List<FabTool> getTools() {
        return mDrupalModel.getFabTools();
    }

    public String getMailAddress() {
        return mMailModel.getFeedbackMailAddress();
    }

    public interface Listener {
        void onOK();

    }
}
