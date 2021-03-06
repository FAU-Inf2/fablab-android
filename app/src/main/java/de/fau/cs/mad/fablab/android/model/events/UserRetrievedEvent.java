package de.fau.cs.mad.fablab.android.model.events;

import android.support.annotation.NonNull;

import de.fau.cs.mad.fablab.rest.core.User;

public class UserRetrievedEvent {

    private User mUser;

    public UserRetrievedEvent(@NonNull User user)
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
