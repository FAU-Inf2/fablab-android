package de.fau.cs.mad.fablab.android.view.floatingbutton;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.eventbus.NavigationEvent;
import de.fau.cs.mad.fablab.android.viewmodel.common.BaseViewModel;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.greenrobot.event.EventBus;

public class FloatingFablabButtonViewModel extends BaseViewModel {

    Listener mListener;
    EventBus mEventBus;

    @Inject
    public FloatingFablabButtonViewModel(){
        mEventBus = EventBus.getDefault();
    }

    private Command<Void> startProductSearchCommand = new Command<Void>() {
        @Override
        public void execute(Void parameter) {
            mEventBus.post(NavigationEvent.ProductSearch);
            mListener.onFloatingButtonClicked();
        }
    };
    private Command<Void> startBarcodeScannerCommand = new Command<Void>() {
        @Override
        public void execute(Void parameter) {
            mEventBus.post(NavigationEvent.BarcodeScanner);
            mListener.onFloatingButtonClicked();
        }
    };

    public void setListener(Listener listener){
        this.mListener = listener;
    }

    public Command<Void> getStartProductSearchCommand() {
        return startProductSearchCommand;
    }

    public Command<Void> getStartBarcodeScannerCommand() {
        return startBarcodeScannerCommand;
    }

    public interface Listener extends BaseViewModel.Listener{
        void onFloatingButtonClicked();
    }
}
