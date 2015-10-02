package de.fau.cs.mad.fablab.android.model;

import de.fau.cs.mad.fablab.rest.core.MailAddresses;
import de.fau.cs.mad.fablab.rest.myapi.DataApi;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MailModel {
    private String mFeedbackMailAddress = "";
    private String mFablabMailAddress = "";

    public MailModel(DataApi dataApi) {
        dataApi.getMailAddresses(new Callback<MailAddresses>() {
            @Override
            public void success(MailAddresses mailAddresses, Response response) {
                mFeedbackMailAddress = mailAddresses.getFeedbackMail();
                mFablabMailAddress = mailAddresses.getFabLabMail();
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    public String getFeedbackMailAddress() {
        return mFeedbackMailAddress;
    }

    public String getFablabMailAddress() {
        return mFablabMailAddress;
    }
}
