package de.fau.cs.mad.fablab.android.view.fragments.icals.viewpager;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.view.fragments.icals.ICalViewLauncher;
import de.fau.cs.mad.fablab.android.viewmodel.common.BaseViewModel;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;

public class ICalDetailFragmentViewModel extends BaseViewModel {

    @Inject
    ICalViewLauncher viewLauncher;

    private Command dismissDialogCommand = new Command() {
        @Override
        public void execute(Object parameter) {
            viewLauncher.dismissDialog();
        }
    };

    public Command getDismissDialogCommand()
    {
        return dismissDialogCommand;
    }

    public void show(String title, String date, String time, String location)
    {
        this.viewLauncher.showDialog(this, title, date, time, location);
    }

}
