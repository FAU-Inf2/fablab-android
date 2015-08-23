package de.fau.cs.mad.fablab.android.view.fragments.inventory;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.model.InventoryModel;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.fau.cs.mad.fablab.rest.core.User;

public class InventoryFragmentViewModel {

    private Listener mListener;
    private User mUser;
    private InventoryModel mModel;

    private Command<Void> mOnScanButtonClickedCommand = new Command<Void>()
    {
        @Override
        public void execute(Void parameter) {
            if (mListener != null) {
                mListener.onScanButtonClicked();
            }
        }
    };

    private Command<Void> mOnSearchButtonClickedCommand = new Command<Void>()
    {
        @Override
        public void execute(Void parameter) {
            if (mListener != null) {
                mListener.onSearchButtonClicked();
            }
        }
    };

    private Command<Void> mOnDeleteButtonClickedCommand = new Command<Void>()
    {
        @Override
        public void execute(Void parameter) {
            mModel.deleteInventory();
        }
    };

    private Command<Void> mOnShowInventoryClickedCommand = new Command<Void>()
    {
        @Override
        public void execute(Void parameter) {

        }
    };

    @Inject
    public InventoryFragmentViewModel(InventoryModel model)
    {
        mModel = model;
    }

    public void setListener(Listener listener)
    {
        mListener = listener;
    }

    public void setUser(User user)
    {
        mUser = user;
    }

    public User getUser()
    {
        return mUser;
    }

    public Command<Void> getOnScanButtonClickedCommand()
    {
        return mOnScanButtonClickedCommand;
    }

    public Command<Void> getOnSearchButtonClickedCommand()
    {
        return mOnSearchButtonClickedCommand;
    }

    public Command<Void> getOnDeleteButtonClickedCommand()
    {
        return mOnDeleteButtonClickedCommand;
    }

    public Command<Void> getOnShowInventoryClickedCommand()
    {
        return mOnShowInventoryClickedCommand;
    }

    public interface Listener
    {
        void onScanButtonClicked();

        void onSearchButtonClicked();
    }
}
