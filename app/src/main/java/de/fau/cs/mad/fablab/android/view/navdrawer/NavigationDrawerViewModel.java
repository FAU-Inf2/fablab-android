package de.fau.cs.mad.fablab.android.view.navdrawer;

import android.os.Bundle;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.model.UserModel;
import de.fau.cs.mad.fablab.android.model.events.UserRetrievedEvent;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.fau.cs.mad.fablab.rest.core.User;
import de.greenrobot.event.EventBus;

public class NavigationDrawerViewModel {
    private static final String KEY_SELECTED_ITEM = "key_selected_item";

    private Listener mListener;
    private EventBus mEventBus = EventBus.getDefault();
    private User mLoggedInUser;
    private UserModel mModel;

    private int mSelectedItem = R.id.drawer_item_news;

    private final Command<Void> mNavigateToNewsCommand = new Command<Void>() {
        @Override
        public void execute(Void parameter) {
            mEventBus.post(NavigationEvent.News);
        }
    };

    private final Command<Void> mNavigateToBarcodeScannerCommand = new Command<Void>() {
        @Override
        public void execute(Void parameter) {
            mEventBus.post(NavigationEvent.BarcodeScanner);
        }
    };

    private final Command<Void> mNavigateToProductSearchCommand = new Command<Void>() {
        @Override
        public void execute(Void parameter) {
            mEventBus.post(NavigationEvent.ProductSearch);
        }
    };

    private final Command<Void> mNavigateToCategorySearchCommand = new Command<Void>() {
        @Override
        public void execute(Void parameter) {
            mEventBus.post(NavigationEvent.CategorySearch);
        }
    };

    private final Command<Void> mNavigateToAboutCommand = new Command<Void>() {
        @Override
        public void execute(Void parameter) {
            mEventBus.post(NavigationEvent.About);
        }
    };

    private final Command<Void> mNavigateToSettingsCommand = new Command<Void>() {
        @Override
        public void execute(Void parameter) {
            mEventBus.post(NavigationEvent.Settings);
        }
    };

    private final Command<Void> mNavigateToAlertCommand = new Command<Void>() {
        @Override
        public void execute(Void parameter) {
            mEventBus.post(NavigationEvent.Alert);
        }
    };

    private final Command<Void> mLogoutCommand = new Command<Void>()
    {
        @Override
        public void execute(Void parameter) {
            mEventBus.post(NavigationEvent.News);
            mListener.loggedOut();
        }
    };

    private final Command<Void> mInventoryCommand = new Command<Void>()
    {
        @Override
        public void execute(Void parameter) {
            mEventBus.post(NavigationEvent.InventoryLogin);
        }
    };

    @Inject
    public NavigationDrawerViewModel(UserModel userModel) {
        mModel = userModel;
        mEventBus.register(this);
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public Command<Void> getNavigateToBarcodeScannerCommand() {
        return mNavigateToBarcodeScannerCommand;
    }

    public Command<Void> getNavigateToNewsCommand() {
        return mNavigateToNewsCommand;
    }

    public Command<Void> getNavigateToProductSearchCommand() {
        return mNavigateToProductSearchCommand;
    }

    public Command<Void> getNavigateToCategorySearchCommand()
    {
        return mNavigateToCategorySearchCommand;
    }

    public Command<Void> getNavigateToAboutCommand() {
        return mNavigateToAboutCommand;
    }

    public Command<Void> getNavigateToSettingsCommand() {
        return mNavigateToSettingsCommand;
    }

    public Command<Void> getNavigateToAlertCommand()
    {
        return mNavigateToAlertCommand;
    }

    public Command<Void> getLogoutCommand()
    {
        return mLogoutCommand;
    }

    public Command<Void> getInventoryCommand()
    {
        return mInventoryCommand;
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
        }
        if (mListener != null) {
            mListener.onNavigationDrawerItemSelected(mSelectedItem);
        }
    }

    public void saveState(Bundle outState) {
        outState.putInt(KEY_SELECTED_ITEM, mSelectedItem);
    }

    public void onEvent(UserRetrievedEvent event) {
        mLoggedInUser = event.getUser();
        mListener.loggedIn(mLoggedInUser);
    }

    public User getLoggedInUser()
    {
        return mLoggedInUser;
    }

    public interface Listener {
        void onNavigationDrawerItemSelected(int itemId);

        void loggedIn(User user);

        void loggedOut();
    }
}
