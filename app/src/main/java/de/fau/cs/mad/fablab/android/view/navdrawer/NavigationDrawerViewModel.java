package de.fau.cs.mad.fablab.android.view.navdrawer;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.eventbus.NavigationEvent;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.greenrobot.event.EventBus;

public class NavigationDrawerViewModel {

    Listener mListener;
    NavigationDrawerModel mModel;
    EventBus mEventBus;

    Command navigateToNewsCommand = new Command<Integer>() {
        @Override
        public void execute(Integer itemid) {
            mEventBus.post(NavigationEvent.News);
            mListener.onNavigationDrawerItemSelected(itemid);
        }
    };

    Command navigateToBarcodeScannerCommand = new Command<Integer>() {
        @Override
        public void execute(Integer itemid) {
            mEventBus.post(NavigationEvent.BarcodeScanner);
            mListener.onNavigationDrawerItemSelected(itemid);
        }
    };

    Command navigateToProductSearchCommand = new Command<Integer>() {
        @Override
        public void execute(Integer itemid) {
            mEventBus.post(NavigationEvent.ProductSearch);
            mListener.onNavigationDrawerItemSelected(itemid);
        }
    };

    @Inject
    public NavigationDrawerViewModel() {
        this.mModel = new NavigationDrawerModel();
        this.mEventBus = EventBus.getDefault();
    }

    public void setListener(Listener listener) {
        this.mListener = listener;
    }

    public void setData(int itemId) {
        mModel.setItemId(itemId);
    }

    public Command<Integer> getNavigateToBarcodeScannerCommand() {
        return navigateToBarcodeScannerCommand;
    }

    public Command<Integer> getNavigateToNewsCommand() {
        return navigateToNewsCommand;
    }

    public Command<Integer> getNavigateToProductSearchCommand() {
        return navigateToProductSearchCommand;
    }

    public interface Listener {
        void onNavigationDrawerItemSelected(int itemId);
    }
}
