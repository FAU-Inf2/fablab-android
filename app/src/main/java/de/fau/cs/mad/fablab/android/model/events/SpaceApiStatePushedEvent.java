package de.fau.cs.mad.fablab.android.model.events;

import net.spaceapi.State;

public class SpaceApiStatePushedEvent {
    private final State mState;

    public SpaceApiStatePushedEvent(State state){
        mState = state;
    }

    public State getState() {
        return mState;
    }
}
