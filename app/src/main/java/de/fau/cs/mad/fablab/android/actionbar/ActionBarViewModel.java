package de.fau.cs.mad.fablab.android.actionbar;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.viewmodel.common.BaseViewModel;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;

public class ActionBarViewModel extends BaseViewModel {

    Listener listener;
    ActionBarModel model;

    private boolean opened;
    private ActionBarTime time;

    Command actionBarItemSelectedCommand = new Command<Void>() {
        @Override
        public void execute(Void parameter) {
            listener.onActionBarItemSelected();
        }
    };

    @Inject
    public ActionBarViewModel() {
        this.model = new ActionBarModel();
    }

    public void setListener(Listener listener){
        this.listener = listener;
    }

    public void setData(boolean opened, ActionBarTime time) {
        model.setData(opened, time);
    }

    @Override
    public void setData(Object data) {
    }

    public Command<Void> getActionBarItemSelectedCommand() {
        return actionBarItemSelectedCommand;
    }

    public interface Listener extends BaseViewModel.Listener {
        void onActionBarItemSelected();
    }
}
