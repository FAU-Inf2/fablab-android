package de.fau.cs.mad.fablab.android.view.actionbar;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.model.SpaceApiModel;
import de.fau.cs.mad.fablab.android.model.events.SpaceApiStateChangedEvent;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.greenrobot.event.EventBus;

public class ActionBarViewModel {

    @Inject
    SpaceApiModel mSpaceApiModel;
    Listener listener;
    ActionBarModel model;
    EventBus mEventBus = EventBus.getDefault();

    private boolean opened;
    private ActionBarTime time;

    private Command<Integer> refreshOpenedStateCommand = new Command<Integer>() {
        @Override
        public void execute(Integer parameter) {
            mSpaceApiModel.refreshState();
        }
    };

    public ActionBarViewModel() {
        model = new ActionBarModel();
        mEventBus.register(this);
    }

    public void setListener(Listener listener){
        this.listener = listener;
    }

    public Command<Integer> getRefreshOpenedStateCommand () {
        return refreshOpenedStateCommand;
    }

    @SuppressWarnings("unused")
    public void onEvent(SpaceApiStateChangedEvent event) {
        // TODO update state
    }

    public interface Listener {
        //void onActionBarItemSelected();
    }
}
