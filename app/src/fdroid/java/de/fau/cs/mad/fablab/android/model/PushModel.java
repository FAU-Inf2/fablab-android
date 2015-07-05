package de.fau.cs.mad.fablab.android.model;

import android.content.Context;

import de.fau.cs.mad.fablab.rest.myapi.PushApi;

public class PushModel {
    @SuppressWarnings("unused")
    public PushModel(Context context, PushApi pushApi) {

    }

    public String getPushId() {
        return "0";
    }
}
