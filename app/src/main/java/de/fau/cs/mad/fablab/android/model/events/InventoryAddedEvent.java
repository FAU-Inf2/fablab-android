package de.fau.cs.mad.fablab.android.model.events;

public class InventoryAddedEvent {

    private boolean mSuccess;

    public InventoryAddedEvent(boolean success)
    {
        mSuccess = success;
    }

    public boolean getState()
    {
        return mSuccess;
    }
}
