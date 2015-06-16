package de.fau.cs.mad.fablab.android.navdrawer;

import de.fau.cs.mad.fablab.android.model.NavigationDrawerStorage;
import de.fau.cs.mad.fablab.android.model.StorageFragment;

public class NavigationDrawerModel {

    /*private static final String NAV_ITEM_ID = "navItemId";
    private static final long DRAWER_CLOSE_DELAY_MS = 250;*/

    private NavigationDrawerStorage storage;

    public NavigationDrawerModel() {
        this.storage = StorageFragment.getNavigationDrawerStorage();
    }

    public int getItemId() {
        return storage.getItemId();
    }

    public void setItemId(int itemId) {
        storage.setItemId(itemId);
    }
}
