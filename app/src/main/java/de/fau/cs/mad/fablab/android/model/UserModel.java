package de.fau.cs.mad.fablab.android.model;

import android.util.Base64;

import de.fau.cs.mad.fablab.android.model.events.UserRetrievedEvent;
import de.fau.cs.mad.fablab.rest.core.User;
import de.fau.cs.mad.fablab.rest.myapi.UserApi;
import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class UserModel {

    private UserApi mUserApi;
    private RestAdapter.Builder mRestAdapterBuilder;

    EventBus mEventBus = EventBus.getDefault();

    private Callback<User> mUserCallback = new Callback<User>() {
        @Override
        public void success(User user, Response response) {
            mEventBus.post(new UserRetrievedEvent(user));
        }

        @Override
        public void failure(RetrofitError error) {
            mEventBus.post(new UserRetrievedEvent(new User()));
        }
    };

    public UserModel(RestAdapter.Builder restAdapterBuilder)
    {
        mRestAdapterBuilder = restAdapterBuilder;
    }

    public void getUser(String username, String password)
    {
        final String credentials = username + ":" + password;
        mRestAdapterBuilder.setRequestInterceptor(new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                // create Base64 encodet string
                String string = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                request.addHeader("Authorization", string);
                request.addHeader("Accept", "application/json");
            }
        });
        mUserApi = mRestAdapterBuilder.build().create(UserApi.class);
        mUserApi.getUserInfo(mUserCallback);
    }

}
