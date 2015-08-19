package de.fau.cs.mad.fablab.android.model.events;

public class SpaceApiStateChangedEvent {
    private final boolean mOpen;
    private final long mTime;

    public SpaceApiStateChangedEvent(boolean open, long time) {
        mOpen = open;
        mTime = time;
    }

    public boolean getOpen() {
        return mOpen;
    }

    public long getTime() {
        return mTime;
    }
}
