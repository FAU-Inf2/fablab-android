package de.fau.cs.mad.fablab.rest;

import android.content.Context;

import de.fau.cs.mad.fablab.rest.myapi.ProductApi;

public class ProductApiClient extends AbstractRestClient {
    private ProductApi rest;

    public ProductApiClient(Context c) {
        super(c);
        rest = restAdapter.create(ProductApi.class);
    }

    public ProductApi get() {
        return rest;
    }
}
