package de.fau.cs.mad.fablab.android.view.navdrawer;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.eventbus.NavigationEvent;
import de.fau.cs.mad.fablab.android.viewmodel.common.BaseViewModel;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.greenrobot.event.EventBus;

public class NavigationDrawerViewModel extends BaseViewModel {

    Listener listener;
    NavigationDrawerModel model;
    EventBus mEventBus;

    Command navigateToNewsCommand = new Command() {
        @Override
        public void execute(Object parameter) {
            mEventBus.post(NavigationEvent.News);
            listener.onNavigationDrawerItemSelected();
        }
    };

    Command navigateToBarcodeScannerCommand = new Command() {
        @Override
        public void execute(Object parameter) {
            mEventBus.post(NavigationEvent.BarcodeScanner);
            listener.onNavigationDrawerItemSelected();
        }
    };

    Command navigateToProductSearchCommand = new Command() {
        @Override
        public void execute(Object parameter) {
            mEventBus.post(NavigationEvent.ProductSearch);
            listener.onNavigationDrawerItemSelected();
        }
    };

    @Inject
    public NavigationDrawerViewModel() {
        this.model = new NavigationDrawerModel();
        this.mEventBus = EventBus.getDefault();
    }

    public void setListener(Listener listener){
        this.listener = listener;
    }
    public void setData(int itemId) {
        model.setItemId(itemId);
    }

    @Override
    public void setData(Object data) {
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

    public interface Listener extends BaseViewModel.Listener{
        void onNavigationDrawerItemSelected();
    }
}
