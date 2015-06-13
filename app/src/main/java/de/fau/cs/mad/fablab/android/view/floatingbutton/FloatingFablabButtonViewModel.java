package de.fau.cs.mad.fablab.android.view.floatingbutton;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.viewmodel.common.BaseViewModel;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;

public class FloatingFablabButtonViewModel extends BaseViewModel {

    Listener listener;
    FloatingFablabButtonViewLauncher viewLauncher;

    private Command startProductSearchCommand = new Command() {
        @Override
        public void execute(Object parameter) {
            viewLauncher.switchToProductSearch();
        }
    };
    private Command startBarcodeScannerCommand = new Command() {
        @Override
        public void execute(Object parameter) {
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

    public Command getStartProductSearchCommand() {
        return startProductSearchCommand;
    }

    public Command getStartBarcodeScannerCommand() {
        return startBarcodeScannerCommand;
    }

    public interface Listener extends BaseViewModel.Listener{

    }
}
