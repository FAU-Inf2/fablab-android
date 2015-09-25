package de.fau.cs.mad.fablab.android.view.fragments.reservation;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.model.ToolUsageModel;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.fau.cs.mad.fablab.rest.core.FabTool;
import de.fau.cs.mad.fablab.rest.core.User;

public class ReservationDialogFragmentViewModel {
    @Inject
    ToolUsageModel mToolUsageModel;

    private Listener mListener;
    private User mUser;
    private FabTool mTool;

    private Command<Void> mAddCommand = new Command<Void>() {
        @Override
        public void execute(Void parameter) {
            if (mListener != null) {
                mListener.onAddReservation();
            }
        }
    };

    public Command<Void> getAddCommand() {
        return mAddCommand;
    }

    public void setTool(FabTool fabTool)
    {
        mTool = fabTool;
    }

    public FabTool getTool()
    {
        return mTool;
    }

    public void setUser(User user)
    {
        mUser = user;
    }

    public User getUser()
    {
        return mUser;
    }


    public void setListener(Listener listener) {
        mListener = listener;
    }

    public void addReservation(User user, FabTool tool, String project, long duration) {
        mToolUsageModel.addToolUsage(user, tool, project, duration);
    }

    public interface Listener {
        void onAddReservation();
    }
}
