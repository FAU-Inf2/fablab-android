package de.fau.cs.mad.fablab.android.pushservice;


import android.util.Log;

public class PushSpaceAPIEvent {


    private static final String TAG = "PushSpaceAPIEvent";

    private String mMessage;

    public PushSpaceAPIEvent(final String aMessage){
        Log.i(TAG,aMessage);
        mMessage = aMessage;
    }

    public String getMessage() {
        return mMessage;
    }
}
