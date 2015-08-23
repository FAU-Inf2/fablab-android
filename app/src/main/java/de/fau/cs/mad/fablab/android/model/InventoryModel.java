package de.fau.cs.mad.fablab.android.model;

import java.util.List;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.model.events.InventoryDeletedEvent;
import de.fau.cs.mad.fablab.android.model.events.InventoryNotDeletedEvent;
import de.fau.cs.mad.fablab.android.viewmodel.common.ObservableArrayList;
import de.fau.cs.mad.fablab.rest.core.InventoryItem;
import de.fau.cs.mad.fablab.rest.myapi.InventoryApi;
import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class InventoryModel {

    private InventoryApi mInventoryApi;
    private EventBus mEventBus = EventBus.getDefault();
    private ObservableArrayList<InventoryItem> mItems;

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

    private Callback<List<InventoryItem>> mGetCallback = new Callback<List<InventoryItem>>() {
        @Override
        public void success(List<InventoryItem> items, Response response) {
            mItems.clear();
            mItems.addAll(items);
        }

        @Override
        public void failure(RetrofitError error) {

        }
    };

    @Inject
    public InventoryModel(InventoryApi api){
        mInventoryApi = api;
        mItems = new ObservableArrayList<>();
    }

    public void sendInventoryItem(InventoryItem item)
    {
        mInventoryApi.add(item, mAddCallback);
    }

    public void deleteInventory()
    {
        mInventoryApi.deleteAll(mDeleteCallback);
    }

    public void getInventory()
    {
        mInventoryApi.getAll(mGetCallback);
    }

    public ObservableArrayList<InventoryItem> getInventoryItems()
    {
        return mItems;
    }
}
