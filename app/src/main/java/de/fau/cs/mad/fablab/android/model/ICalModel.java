package de.fau.cs.mad.fablab.android.model;

import android.app.Activity;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.viewmodel.common.ObservableArrayList;
import de.fau.cs.mad.fablab.rest.core.ICal;
import de.fau.cs.mad.fablab.rest.myapi.ICalApi;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/***
 * Handles the connection to the rest server to receive ICals.
 * Stores the results in ICalStorage and fires events on success
 */
public class ICalModel {

    private ICalApi iCalApi;
    private ICalStorage storage;
    private Activity activity;
    private int currentOffset = 0;

    private static final int ELEMENT_COUNT = 10;

    @Inject
    public ICalModel(ICalApi api, ICalStorage storage, Activity activity){
        this.iCalApi = api;
        this.storage = storage;
        this.activity = activity;
        fetchNextICals();
    }

/*
    public void fetchNextICals(){
        iCalApi.find(currentOffset, ELEMENT_COUNT, iCalApiCallback);
    }
*/

    public void fetchNextICals(){
        iCalApi.findAll(iCalApiCallback);
    }


    protected Callback<List<ICal>> iCalApiCallback = new Callback<List<ICal>>() {
        @Override
        public void success(List<ICal> iCals, Response response) {
            for(ICal i : iCals){
                storage.addICal(i);
            }
            currentOffset += iCals.size();
        }

        @Override
        public void failure(RetrofitError error) {
            Toast.makeText(activity, R.string.retrofit_callback_failure, Toast.LENGTH_LONG).show();
        }
    };


    public ObservableArrayList<ICal> getICalsList() {
        return storage.getAllICals();
    }
}
