package de.fau.cs.mad.fablab.android.model;


import de.fau.cs.mad.fablab.rest.myapi.DataApi;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class FablabMailModel {

    private static String mMailAddress = "";
    private static DataApi mDataApi;

    private static Callback<String> mMailAddressCallback = new Callback<String>() {
        @Override
        public void success(String address, Response response) {
            mMailAddress = address;
        }

        @Override
        public void failure(RetrofitError error) {
        }
    };

    public FablabMailModel(DataApi dataApi) {
        mDataApi = dataApi;
        mDataApi.getFeedbackMailAddress(mMailAddressCallback);
    }

    public String getMailAddress()
    {
        return mMailAddress;
    }
}
