package de.fau.cs.mad.fablab.android.view.floatingbutton;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.viewmodel.common.BaseViewModel;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;

public class FloatingFablabButtonViewModel extends BaseViewModel {

    Listener listener;
    FloatingFablabButtonViewLauncher viewLauncher;

    private Command<Void> startProductSearchCommand = new Command<Void>() {
        @Override
        public void execute(Void parameter) {
            viewLauncher.switchToProductSearch();
        }
    };
    private Command<Void> startBarcodeScannerCommand = new Command<Void>() {
        @Override
        public void execute(Void parameter) {
            viewLauncher.switchToBarcodeScanner();
        }
    };

    @Inject
    public FloatingFablabButtonViewModel(FloatingFablabButtonViewLauncher viewLauncher){
        this.viewLauncher = viewLauncher;
    }

    public void setListener(Listener listener){
        this.listener = listener;
    }

    @Override
    public void setData(Object data) {

    }

    public Command<Void> getStartProductSearchCommand() {
        return startProductSearchCommand;
    }

    public Command<Void> getStartBarcodeScannerCommand() {
        return startBarcodeScannerCommand;
    }

    public interface Listener extends BaseViewModel.Listener{

    }
}
