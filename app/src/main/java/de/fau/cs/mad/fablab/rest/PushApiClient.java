package de.fau.cs.mad.fablab.rest;

import android.content.Context;

import de.fau.cs.mad.fablab.rest.myapi.PushApi;


public class PushApiClient extends AbstractRestClient {

    private PushApi pushApi;

    public PushApiClient(Context context) {
        super(context);
        pushApi = super.restAdapter.create(PushApi.class);
    }

    public PushApi get(){
        return pushApi;
    }
}
