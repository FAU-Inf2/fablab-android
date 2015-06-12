package de.fau.cs.mad.fablab.android.view.fragments.news;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;

import de.fau.cs.mad.fablab.android.view.common.ViewLauncher;

public class NewsViewLauncher extends ViewLauncher {

    static final String IMAGE = "IMAGE";
    static final String TITLE = "TITLE";
    static final String TEXT = "TEXT";

    public NewsViewLauncher(Activity activity) {
        super(activity);
    }

    public void showNewsDialogFragment(String title, String content, String imageLink){
        Bundle args = new Bundle();
        args.putString(TITLE, title);
        args.putString(TEXT, content);
        args.putString(IMAGE, imageLink);
        DialogFragment dialog = new NewsDetailsDialog();
        dialog.setArguments(args);
        dialog.show(activity.getFragmentManager(), "NewsDialog");
    }
}
