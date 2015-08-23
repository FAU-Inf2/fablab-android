package de.fau.cs.mad.fablab.android.model;

import android.util.Base64;

import java.util.List;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.model.events.InventoryDeletedEvent;
import de.fau.cs.mad.fablab.android.model.events.InventoryNotDeletedEvent;
import de.fau.cs.mad.fablab.android.viewmodel.common.ObservableArrayList;
import de.fau.cs.mad.fablab.rest.core.InventoryItem;
import de.fau.cs.mad.fablab.rest.myapi.InventoryApi;
import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class InventoryModel {

    private InventoryApi mInventoryApi;
    private RestAdapter.Builder mRestAdapterBuilder;
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
    public InventoryModel(RestAdapter.Builder restAdapterBuilder){
        mRestAdapterBuilder = restAdapterBuilder;
        mItems = new ObservableArrayList<>();
    }

    public void sendInventoryItem(InventoryItem item, String username, String password)
    {
        final String credentials = username + ":" + password;
        mRestAdapterBuilder.setRequestInterceptor(new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                // create Base64 encodet string
                String string = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                request.addHeader("Authorization", string);
                request.addHeader("Accept", "application/json");
            }
        });
        mInventoryApi = mRestAdapterBuilder.build().create(InventoryApi.class);
        mInventoryApi.add(item, mAddCallback);
    }

    public void deleteInventory(String username, String password)
    {
        final String credentials = username + ":" + password;
        mRestAdapterBuilder.setRequestInterceptor(new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                // create Base64 encodet string
                String string = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                request.addHeader("Authorization", string);
                request.addHeader("Accept", "application/json");
            }
        });
        mInventoryApi = mRestAdapterBuilder.build().create(InventoryApi.class);
        mInventoryApi.deleteAll(mDeleteCallback);
    }

    public void getInventory()
    {
        mInventoryApi = mRestAdapterBuilder.build().create(InventoryApi.class);
        mInventoryApi.getAll(mGetCallback);
    }

    public ObservableArrayList<InventoryItem> getInventoryItems()
    {
        return mItems;
    }
}
