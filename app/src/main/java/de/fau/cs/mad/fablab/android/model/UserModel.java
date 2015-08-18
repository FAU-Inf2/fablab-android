package de.fau.cs.mad.fablab.android.model;

import de.fau.cs.mad.fablab.rest.core.User;
import de.fau.cs.mad.fablab.rest.myapi.UserApi;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class UserModel {

    private UserApi mUserApi;
    private User mUser;

    private Callback<User> mUserCallback = new Callback<User>() {
        @Override
        public void success(User user, Response response) {
            System.out.println("USER CALLBACK SUCCESS");
            mUser = user;
        }

        @Override
        public void failure(RetrofitError error) {
            System.out.println("USER CALLBACK FAILED");
        }
    };

    public UserModel(UserApi userApi)
    {
        mUser = new User();
        mUserApi = userApi;
        mUserApi.getUserInfo(mUser, mUserCallback);
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
