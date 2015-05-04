package de.fau.cs.mad.fablab.rest;

import android.content.Context;
import de.fau.cs.mad.fablab.common.HelloFablabResource;

/**
 * REST-Client for basket issues.
 */
public class TestClient extends  AbstractRestClient{

    private HelloFablabResource rest;

    public TestClient(Context c){
        super(c);
        // Setting the specific Interface to the RestAdapter to initialize the client
        rest = super.restAdapter.create(HelloFablabResource.class);
    }

    public HelloFablabResource get(){
        return rest;
    }

}