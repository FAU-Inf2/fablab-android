package de.fau.cs.mad.fablab.android.navdrawer;

import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.model.NavigationDrawerStorage;

public class NavigationDrawerModel {

    /*private static final String NAV_ITEM_ID = "navItemId";
    private static final long DRAWER_CLOSE_DELAY_MS = 250;*/

    private NavigationDrawerStorage storage;
    //
    int itemId = R.id.drawer_item_news;

    public NavigationDrawerModel(NavigationDrawerStorage navigationDrawerStorage) {
        this.storage = navigationDrawerStorage;
    }

    public NavigationDrawerModel() { }

    public int getItemId() {
        return /*storage.getItemId()*/ itemId;
    }

    public void setItemId(int itemId) {
        //storage.setItemId(itemId);
        this.itemId = itemId;
    }
}
