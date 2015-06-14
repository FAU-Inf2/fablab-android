package de.fau.cs.mad.fablab.android.pushservice;


import android.util.Log;

public class PushWarenkorpEvent {

    private static final String TAG = "PushWarenkorpEvent";

    private String mMessage;

    public PushWarenkorpEvent(final String aMessage){
        Log.i(TAG,aMessage);
        mMessage = aMessage;
    }

    public String getMessage() {
        return mMessage;
    }
}
