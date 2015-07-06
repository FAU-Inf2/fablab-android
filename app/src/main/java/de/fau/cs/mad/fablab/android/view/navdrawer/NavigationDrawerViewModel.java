package de.fau.cs.mad.fablab.android.view.navdrawer;

import android.os.Bundle;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.greenrobot.event.EventBus;

public class NavigationDrawerViewModel {
    private static final String KEY_SELECTED_ITEM = "key_selected_item";

    private Listener mListener;
    private EventBus mEventBus = EventBus.getDefault();

    private int mSelectedItem = R.id.drawer_item_news;

    private final Command<Integer> mNavigateToNewsCommand = new Command<Integer>() {
        @Override
        public void execute(Integer itemid) {
            mEventBus.post(NavigationEvent.News);
        }
    };

    private final Command<Integer> mNavigateToBarcodeScannerCommand = new Command<Integer>() {
        @Override
        public void execute(Integer itemid) {
            mEventBus.post(NavigationEvent.BarcodeScanner);
        }
    };

    private final Command<Integer> mNavigateToProductSearchCommand = new Command<Integer>() {
        @Override
        public void execute(Integer itemid) {
            mEventBus.post(NavigationEvent.ProductSearch);
        }
    };

    private final Command<Integer> mNavigateToAboutCommand = new Command<Integer>() {
        @Override
        public void execute(Integer itemid) {
            mEventBus.post(NavigationEvent.About);
        }
    };

    @Inject
    public NavigationDrawerViewModel() {

    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public Command<Integer> getNavigateToBarcodeScannerCommand() {
        return mNavigateToBarcodeScannerCommand;
    }

    public Command<Integer> getNavigateToNewsCommand() {
        return mNavigateToNewsCommand;
    }

    public Command<Integer> getNavigateToProductSearchCommand() {
        return mNavigateToProductSearchCommand;
    }

    public Command<Integer> getNavigateToAboutCommand() {
        return mNavigateToAboutCommand;
    }

    public void setSelection(int itemId) {
        mSelectedItem = itemId;
        if (mListener != null) {
            mListener.onNavigationDrawerItemSelected(itemId);
        }
    }

    public void restoreState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mSelectedItem = savedInstanceState.getInt(KEY_SELECTED_ITEM);
            if (mListener != null) {
                mListener.onNavigationDrawerItemSelected(mSelectedItem);
            }
        }
    }

    public void saveState(Bundle outState) {
        outState.putInt(KEY_SELECTED_ITEM, mSelectedItem);
    }

    public interface Listener {
        void onNavigationDrawerItemSelected(int itemId);
    }
}
