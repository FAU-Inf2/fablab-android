package de.fau.cs.mad.fablab.android.view.navdrawer;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.eventbus.NavigationEvent;
import de.fau.cs.mad.fablab.android.viewmodel.common.BaseViewModel;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.greenrobot.event.EventBus;

public class NavigationDrawerViewModel extends BaseViewModel {

    Listener mListener;
    NavigationDrawerModel mModel;
    EventBus mEventBus;

    Command navigateToNewsCommand = new Command() {
        @Override
        public void execute(Object parameter) {
            mEventBus.post(NavigationEvent.News);
            mListener.onNavigationDrawerItemSelected();
        }
    };

    Command navigateToBarcodeScannerCommand = new Command() {
        @Override
        public void execute(Object parameter) {
            mEventBus.post(NavigationEvent.BarcodeScanner);
            mListener.onNavigationDrawerItemSelected();
        }
    };

    Command navigateToProductSearchCommand = new Command() {
        @Override
        public void execute(Object parameter) {
            mEventBus.post(NavigationEvent.ProductSearch);
            mListener.onNavigationDrawerItemSelected();
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

    public Command getNavigateToBarcodeScannerCommand() {
        return navigateToBarcodeScannerCommand;
    }

    public Command getNavigateToNewsCommand() {
        return navigateToNewsCommand;
    }

    public Command getNavigateToProductSearchCommand() {
        return navigateToProductSearchCommand;
    }

    public interface Listener extends BaseViewModel.Listener {
        void onNavigationDrawerItemSelected();
    }
}
