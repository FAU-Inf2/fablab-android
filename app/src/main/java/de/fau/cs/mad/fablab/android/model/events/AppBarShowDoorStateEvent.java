package de.fau.cs.mad.fablab.android.model.events;

public class AppBarShowDoorStateEvent {
    private final boolean mState;

    public AppBarShowDoorStateEvent(boolean state){
        mState = state;
    }

    public boolean getState() {
        return mState;
    }
}
