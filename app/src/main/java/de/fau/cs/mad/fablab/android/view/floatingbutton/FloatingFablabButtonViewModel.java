package de.fau.cs.mad.fablab.android.view.floatingbutton;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.view.navdrawer.NavigationEvent;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.greenrobot.event.EventBus;

public class FloatingFablabButtonViewModel {

    private Listener mListener;
    private EventBus mEventBus = EventBus.getDefault();

    @Inject
    public FloatingFablabButtonViewModel(){

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

    private Command<Void> startCategorySearchCommand = new Command<Void>(){
        @Override
        public void execute(Void parameter) {
            mEventBus.post(NavigationEvent.CategorySearch);
            mListener.onFloatingButtonClicked();
        }
    };

    public void setListener(Listener listener){
        mListener = listener;
    }

    public Command<Void> getStartProductSearchCommand() {
        return startProductSearchCommand;
    }

    public Command<Void> getStartBarcodeScannerCommand() {
        return startBarcodeScannerCommand;
    }

    public Command<Void> getStartCategorySearchCommand()
    {
        return startCategorySearchCommand;
    }

    public interface Listener {
        void onFloatingButtonClicked();
    }
}
