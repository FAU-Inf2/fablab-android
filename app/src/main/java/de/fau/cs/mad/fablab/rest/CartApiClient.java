package de.fau.cs.mad.fablab.rest;

import android.content.Context;

import de.fau.cs.mad.fablab.rest.myapi.CartApi;

public class CartApiClient extends AbstractRestClient {
    private CartApi rest;

    public CartApiClient(Context c) {
        super(c);
        rest = restAdapter.create(CartApi.class);
    }

    public CartApi get() {
        return rest;
    }
}
