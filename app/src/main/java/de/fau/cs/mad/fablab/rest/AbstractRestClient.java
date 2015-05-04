package de.fau.cs.mad.fablab.rest;


import android.content.Context;

import com.squareup.okhttp.OkHttpClient;

import de.fau.cs.mad.fablab.android.R;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

/** Super class for the individual clients
 * that implements common methods and features.
 * - API_URL
 * - General RestAdapter that later implements the REST Interface
 */
public abstract class AbstractRestClient{

    protected RestAdapter restAdapter;

    protected AbstractRestClient(Context c){
        final String API_URL = c.getResources().getString(R.string.API_URL);
        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(API_URL)
                .setClient(new OkClient(new OkHttpClient()))
                .setLogLevel(RestAdapter.LogLevel.FULL);

        restAdapter = builder.build();
    }
}
