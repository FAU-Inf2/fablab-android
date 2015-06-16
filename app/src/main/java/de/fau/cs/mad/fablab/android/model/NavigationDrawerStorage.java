package de.fau.cs.mad.fablab.android.model;

import de.fau.cs.mad.fablab.android.R;

public class NavigationDrawerStorage {
    private int itemId;

    public NavigationDrawerStorage() {
        itemId = R.id.drawer_item_news;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }
}
