package de.fau.cs.mad.fablab.rest;

import android.content.Context;

import de.fau.cs.mad.fablab.rest.myapi.SpaceApi;


/**
 * REST-Client for basket issues.
 */
public class SpaceApiClient extends  AbstractRestClient{

    private SpaceApi rest;

    public SpaceApiClient(Context c){
        super(c);
        // Setting the specific Interface to the RestAdapter to initialize the client
        rest = super.restAdapter.create(SpaceApi.class);
    }

    public SpaceApi get(){
        return rest;
    }

}