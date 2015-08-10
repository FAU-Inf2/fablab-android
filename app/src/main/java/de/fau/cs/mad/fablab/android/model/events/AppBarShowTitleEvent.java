package de.fau.cs.mad.fablab.android.model.events;

public class AppBarShowTitleEvent {
    private final boolean mState;

    public AppBarShowTitleEvent(boolean state){
        mState = state;
    }

    public boolean getState() {
        return mState;
    }
}
