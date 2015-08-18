package de.fau.cs.mad.fablab.android.view.navdrawer;

import android.os.Bundle;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.model.UserModel;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.fau.cs.mad.fablab.rest.core.User;
import de.greenrobot.event.EventBus;

public class NavigationDrawerViewModel {
    private static final String KEY_SELECTED_ITEM = "key_selected_item";

    private Listener mListener;
    private EventBus mEventBus = EventBus.getDefault();
    private UserModel mModel;

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

    private final Command<Integer> mNavigateToSettingsCommand = new Command<Integer>() {
        @Override
        public void execute(Integer itemid) {
            mEventBus.post(NavigationEvent.Settings);
        }
    };

    private final Command<Integer> mNavigateToAlertCommand = new Command<Integer>() {
        @Override
        public void execute(Integer itemid) {
            mEventBus.post(NavigationEvent.Alert);
        }
    };

    private final Command<Void> mLoginCommand = new Command<Void>()
    {
        @Override
        public void execute(Void parameter) {
            String password = mListener.getPassword();
            String username = mListener.getUsername();

            // get userinformation from server
            // TODO give user to listener
            //ArrayList<Roles> roles = new ArrayList();
            //roles.add(Roles.INVENTORY);
            //User user = new User("inventory", roles);
            mListener.loggedIn(mModel.getUser());
        }
    };

    private final Command<Integer> mLogoutCommand = new Command<Integer>()
    {
        @Override
        public void execute(Integer parameter) {
            mListener.loggedOut();
        }
    };

    private final Command<Integer> mInventoryCommand = new Command<Integer>()
    {
        @Override
        public void execute(Integer parameter) {
            mEventBus.post(NavigationEvent.Inventory);
        }
    };

    @Inject
    public NavigationDrawerViewModel(UserModel userModel) {
        mModel = userModel;
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

    public Command<Integer> getNavigateToSettingsCommand() {
        return mNavigateToSettingsCommand;
    }

    public Command<Integer> getNavigateToAlertCommand()
    {
        return mNavigateToAlertCommand;
    }

    public Command<Void> getLoginCommand()
    {
        return mLoginCommand;
    }

    public Command<Integer> getLogoutCommand()
    {
        return mLogoutCommand;
    }

    public Command<Integer> getInventoryCommand()
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

    public interface Listener {
        void onNavigationDrawerItemSelected(int itemId);

        String getUsername();

        String getPassword();

        void loggedIn(User user);

        void loggedOut();
    }
}
