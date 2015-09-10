package de.fau.cs.mad.fablab.android.model.events;

public class InventoryGetInventoryEvent {

    private boolean mSuccess;

    public InventoryGetInventoryEvent(boolean success)
    {
        mSuccess = success;
    }

    public boolean getState()
    {
        return mSuccess;
    }
}
