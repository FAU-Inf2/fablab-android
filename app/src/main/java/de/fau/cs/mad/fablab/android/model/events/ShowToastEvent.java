package de.fau.cs.mad.fablab.android.model.events;

public class ShowToastEvent {
    private String mMsg;
    private int mLength;

    public ShowToastEvent(String msg, int length) {
        mMsg = msg;
        mLength = length;
    }

    public String getMsg() {
        return mMsg;
    }

    public int getLength() {
        return mLength;
    }
}
