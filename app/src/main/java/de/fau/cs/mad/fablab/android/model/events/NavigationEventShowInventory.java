package de.fau.cs.mad.fablab.android.model.events;

import android.support.annotation.NonNull;

import de.fau.cs.mad.fablab.rest.core.User;

public class NavigationEventShowInventory {

    private User mUser;

    public NavigationEventShowInventory(@NonNull User user)
    {
        mUser = user;
    }

    public User getUser()
    {
        return mUser;
    }

    public void setUser(User user)
    {
        mUser = user;
    }
}
