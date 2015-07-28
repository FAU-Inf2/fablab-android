package de.fau.cs.mad.fablab.android.model.util;

import retrofit.Callback;

public abstract class CancellableCallback<T> implements Callback<T> {
    private boolean mIsCancelled = false;

    public boolean isCancelled() {
        return mIsCancelled;
    }

    public void cancel() {
        mIsCancelled = true;
    }
}