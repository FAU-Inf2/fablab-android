package de.fau.cs.mad.fablab.android.view.fragments.inventory;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;

public class InventoryFragmentViewModel {

    private Listener mListener;

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

    @Inject
    public InventoryFragmentViewModel()
    {

    }

    public void setListener(Listener listener)
    {
        mListener = listener;
    }

    public Command<Void> getOnScanButtonClickedCommand()
    {
        return mOnScanButtonClickedCommand;
    }

    public Command<Void> getOnSearchButtonClickedCommand()
    {
        return mOnSearchButtonClickedCommand;
    }

    public interface Listener
    {
        void onScanButtonClicked();

        void onSearchButtonClicked();
    }
}
