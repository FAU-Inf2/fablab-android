package de.fau.cs.mad.fablab.android.view.actionbar;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.model.SpaceApiModel;
import de.fau.cs.mad.fablab.android.model.events.SpaceApiStateChangedEvent;
import de.fau.cs.mad.fablab.android.util.Formatter;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.greenrobot.event.EventBus;

public class ActionBarViewModel {

    @Inject
    SpaceApiModel mSpaceApiModel;
    private Listener mListener;
    private EventBus mEventBus = EventBus.getDefault();

    private final Command<Integer> refreshOpenedStateCommand = new Command<Integer>() {
        @Override
        public void execute(Integer parameter) {
            mSpaceApiModel.refreshState();
        }
    };

    public void setListener(Listener listener){
        mListener = listener;
    }

    public Command<Integer> getRefreshOpenedStateCommand () {
        return refreshOpenedStateCommand;
    }

    public void initialize() {
        if (mListener != null) {
            mListener.onStateUpdated(mSpaceApiModel.getOpen(), Formatter.formatTime(
                    mSpaceApiModel.getTime()));
        }
    }

    public void pause() {
        mEventBus.unregister(this);
    }

    public void resume() {
        mEventBus.register(this);
    }

    @SuppressWarnings("unused")
    public void onEvent(SpaceApiStateChangedEvent event) {
        if (mListener != null) {
            mListener.onStateUpdated(event.getOpen(), Formatter.formatTime(event.getTime()));
        }
    }

    public interface Listener {
        void onStateUpdated(boolean open, String time);
    }
}
