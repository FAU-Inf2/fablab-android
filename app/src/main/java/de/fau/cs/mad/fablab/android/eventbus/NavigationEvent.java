package de.fau.cs.mad.fablab.android.eventbus;

public class NavigationEvent {
    private int itemId;

    public NavigationEvent(int itemId) {
        this.itemId = itemId;
    }
    public int getitemId() {
        return itemId;
    }
}
