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

    Command navigationDrawerItemSelectedCommand = new Command<Integer>() {
        @Override
        public void execute(Integer menuItemId) {
            mEventBus.post(new NavigationEvent(menuItemId));
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

    public Command<Integer> getNavigationItemSelectedCommand() {
        return navigationDrawerItemSelectedCommand;
    }

    public interface Listener extends BaseViewModel.Listener{
        void onNavigationDrawerItemSelected();
    }
}
