package de.fau.cs.mad.fablab.android.model;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.model.events.InventoryDeletedEvent;
import de.fau.cs.mad.fablab.android.model.events.InventoryNotDeletedEvent;
import de.fau.cs.mad.fablab.rest.core.InventoryItem;
import de.fau.cs.mad.fablab.rest.myapi.InventoryApi;
import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class InventoryModel {

    private InventoryApi mInventoryApi;
    private EventBus mEventBus = EventBus.getDefault();

    private Callback<InventoryItem> mAddCallback = new Callback<InventoryItem>() {
        @Override
        public void success(InventoryItem item, Response response) {
        }

        @Override
        public void failure(RetrofitError error) {

        }
    };

    private Callback<Boolean> mDeleteCallback = new Callback<Boolean>() {

        @Override
        public void success(Boolean success, Response response) {
            mEventBus.post(new InventoryDeletedEvent());
        }

        @Override
        public void failure(RetrofitError error) {
            mEventBus.post(new InventoryNotDeletedEvent());
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

    public void deleteInventory()
    {
        mInventoryApi.deleteAll(mDeleteCallback);
    }
}
