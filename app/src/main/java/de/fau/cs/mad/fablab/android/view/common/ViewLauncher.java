package de.fau.cs.mad.fablab.android.view.common;

import android.app.Activity;

public abstract class ViewLauncher {

    protected final Activity activity;

    protected ViewLauncher(Activity activity) {
        this.activity = activity;
    }
}
