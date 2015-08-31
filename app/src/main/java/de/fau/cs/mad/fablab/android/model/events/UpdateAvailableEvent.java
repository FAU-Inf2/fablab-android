package de.fau.cs.mad.fablab.android.model.events;

public class UpdateAvailableEvent {
    private final boolean mIsRequired;
    private final String mMessage;

    public UpdateAvailableEvent(boolean isRequired, String message) {
        mIsRequired = isRequired;
        mMessage = message;
    }

    public boolean isRequired() {
        return mIsRequired;
    }

    public String getMessage() {
        return mMessage;
    }
}
