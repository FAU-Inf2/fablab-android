package de.fau.cs.mad.fablab.android.model;

import java.util.List;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.viewmodel.common.ObservableArrayList;
import de.fau.cs.mad.fablab.rest.core.ICal;
import de.fau.cs.mad.fablab.rest.myapi.ICalApi;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/***
 * Handles the connection to the rest server to receive ICals.
 * Stores the results and fires events on success
 */
public class ICalModel {
    private static final int ELEMENT_COUNT = 10;

    private ObservableArrayList<ICal> mICals;
    private ICalApi mICalApi;
    private boolean mICalsRequested;

    private Callback<List<ICal>> iCalApiCallback = new Callback<List<ICal>>() {
        @Override
        public void success(List<ICal> iCals, Response response) {
            mICals.addAll(iCals);
            mICalsRequested = false;
        }

        @Override
        public void failure(RetrofitError error) {
            mICalsRequested = false;
        }
    };

    @Inject
    public ICalModel(ICalApi api){
        mICalApi = api;
        mICals = new ObservableArrayList<>();
        mICalsRequested = false;
        fetchNextICals();
    }

    public void fetchNextICals(){
        if (!mICalsRequested) {
            mICalsRequested = true;
            mICalApi.find(mICals.size(), ELEMENT_COUNT, iCalApiCallback);
        }
    }


    public ObservableArrayList<ICal> getICalsList() {
        return mICals;
    }
}
