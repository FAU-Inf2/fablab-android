package de.fau.cs.mad.fablab.android.view.fragments.reservation;

import com.pedrogomez.renderers.AdapteeCollection;
import com.pedrogomez.renderers.ListAdapteeCollection;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.model.DrupalModel;
import de.fau.cs.mad.fablab.android.model.ToolUsageModel;
import de.fau.cs.mad.fablab.android.viewmodel.common.BaseViewModel;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.fau.cs.mad.fablab.rest.core.FabTool;
import de.fau.cs.mad.fablab.rest.core.ToolUsage;
import de.fau.cs.mad.fablab.rest.core.User;

public class ReservationFragmentViewModel extends BaseViewModel<ToolUsage> {

    @Inject
    DrupalModel mDrupalModel;
    @Inject
    ToolUsageModel mToolUsageModel;

    private Listener mListener;
    private User mUser;

    private ListAdapteeCollection<ToolUsageViewModel> mToolUsageViewModelCollection;

    private Command<Void> mAddCommand = new Command<Void>() {
        @Override
        public void execute(Void parameter) {
            if (mListener != null) {
                mListener.onAdd();
            }
        }
    };

    private Command<Integer> mToolChangedCommand = new Command<Integer>() {
        @Override
        public void execute(Integer parameter) {
            if (mListener != null) {
                mListener.onToolChanged(parameter);
            }
        }
    };

    public ReservationFragmentViewModel() {
        mToolUsageViewModelCollection = new ListAdapteeCollection<>();
    }

    public void setListener(Listener listener) {
        mListener = listener;
        mToolUsageModel.setObservableArrayListListener(this);
    }

    public Command<Void> getAddCommand() {
        return mAddCommand;
    }

    public Command<Integer> getToolChangedCommand() {
        return mToolChangedCommand;
    }

    public List<FabTool> getTools() {
        return mDrupalModel.getFabTools();
    }

    public List<ToolUsage> getToolUsages(long toolId) {
        return mToolUsageModel.getToolUsages(toolId);
    }

    private Command<Void> mOnDeleteButtonClickedCommand = new Command<Void>()
    {
        @Override
        public void execute(Void parameter) {
            // To-do stuff ... mModel.deleteReservation(mUser.getUsername(), mUser.getPassword());
        }
    };

    @Override
    public void onAllItemsAdded(Collection<? extends ToolUsage> collection) {
        addItems(collection);
    }

    private void addItems(Collection<? extends ToolUsage> toolUsages) {
        for (ToolUsage toolUsage : toolUsages) {
            mToolUsageViewModelCollection.add(new ToolUsageViewModel(toolUsage));
        }
        if (mListener != null) {
            mListener.onToolListChanged();
        }
    }

    @Override
    public void onAllItemsRemoved(List<ToolUsage> removedItems) {
        mToolUsageViewModelCollection.clear();
        if (mListener != null) {
            mListener.onToolListChanged();
        }
    }

    public AdapteeCollection<ToolUsageViewModel> getToolUsageViewModelCollection() {
        return mToolUsageViewModelCollection;
    }


    public void setUser(User user)
    {
        mUser = user;
    }

    public User getUser()
    {
        return mUser;
    }

    public interface Listener {
        void onAdd();
        void onToolChanged(int parameter);
        void onToolListChanged();
    }
}
