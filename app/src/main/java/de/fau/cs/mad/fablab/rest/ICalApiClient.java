package de.fau.cs.mad.fablab.rest;

import android.content.Context;

import de.fau.cs.mad.fablab.rest.myapi.ICalApi;

public class ICalApiClient extends AbstractRestClient {

    private ICalApi rest;

    public ICalApiClient(Context c)
    {
        super(c);
        rest = restAdapter.create(ICalApi.class);
    }

    public ICalApi get()
    {
        return rest;
    }
}
