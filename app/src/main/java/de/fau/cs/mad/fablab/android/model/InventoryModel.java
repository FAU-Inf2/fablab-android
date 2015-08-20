package de.fau.cs.mad.fablab.android.model;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.rest.core.InventoryItem;
import de.fau.cs.mad.fablab.rest.myapi.InventoryApi;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class InventoryModel {

    private InventoryApi mInventoryApi;

    private Callback<InventoryItem> mAddCallback = new Callback<InventoryItem>() {
        @Override
        public void success(InventoryItem item, Response response) {
        }

        @Override
        public void failure(RetrofitError error) {

        }
    };

    @Inject
    public InventoryModel(InventoryApi api){
        mInventoryApi = api;
    }

    public void sendInventoryItem(InventoryItem item)
    {
        mInventoryApi.add(item, mAddCallback);
    }
}
