package de.fau.cs.mad.fablab.android.model.events;

public class SpaceApiStateChangedEvent {
    private final boolean mOpen;
    private final long mTime;
    private final String mMessage;

    public SpaceApiStateChangedEvent(boolean open, long time, String message) {
        mOpen = open;
        mTime = time;
        mMessage = message;
    }

    public boolean getOpen() {
        return mOpen;
    }

    public long getTime() {
        return mTime;
    }

    public String getMessage() {
        return mMessage;
    }
}
