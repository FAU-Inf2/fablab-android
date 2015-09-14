package de.fau.cs.mad.fablab.android.model.events;

import android.support.annotation.NonNull;

import de.fau.cs.mad.fablab.rest.core.User;

public class NavigationEventProductSearchInventory {

    private User mUser;
    private boolean mProductSearch;

    public NavigationEventProductSearchInventory(@NonNull User user, boolean productSearch)
    {
        mUser = user;
        mProductSearch = productSearch;
    }

    public User getUser()
    {
        return mUser;
    }

    public void setUser(User user)
    {
        mUser = user;
    }

    public void setProductSearch(boolean productSearch)
    {
        mProductSearch = productSearch;
    }

    public boolean getProductSearch()
    {
        return mProductSearch;
    }
}
