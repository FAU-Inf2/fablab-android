package de.fau.cs.mad.fablab.android.model.events;

public class ProductMapStatusChangedEvent {
    private final boolean mIsAvailable;

    public  ProductMapStatusChangedEvent(boolean isAvailable) {
        mIsAvailable = isAvailable;
    }

    public boolean isAvailable() {
        return mIsAvailable;
    }
}
