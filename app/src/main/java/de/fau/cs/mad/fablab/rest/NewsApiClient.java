package de.fau.cs.mad.fablab.rest;

import android.content.Context;

import de.fau.cs.mad.fablab.rest.myapi.NewsApi;

public class NewsApiClient extends AbstractRestClient {

    private NewsApi rest;

    public NewsApiClient(Context c) {
        super(c);
        rest = restAdapter.create(NewsApi.class);
    }

    public NewsApi get() {
        return rest;
    }

}
