package de.fau.cs.mad.fablab.android.model.events;

public class InventoryDeletedEvent {

    private boolean mSuccess;

    public InventoryDeletedEvent(boolean success)
    {
        mSuccess = success;
    }

    public boolean getState()
    {
        return mSuccess;
    }
}
