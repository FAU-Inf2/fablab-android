package de.fau.cs.mad.fablab.common;

import de.fau.cs.mad.fablab.rest.entities.WelcomeUser;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Resource class for the server's "/hello-fablab" uri
 * using Retrofit annotation.
 */
public interface HelloFablabResource {

    @GET("/hello-fablab")
    void getWelcomeUser(@Query("name") String userName,
                    Callback<WelcomeUser> callback);

}
