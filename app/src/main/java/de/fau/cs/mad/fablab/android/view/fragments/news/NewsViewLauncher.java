package de.fau.cs.mad.fablab.android.view.fragments.news;

import android.app.Activity;
import android.os.Bundle;

import de.fau.cs.mad.fablab.android.view.common.ViewLauncher;

public class NewsViewLauncher extends ViewLauncher {

    private NewsDetailsDialogFragment dialog;

    public NewsViewLauncher(Activity activity) {
        super(activity);
    }

    public void showNewsDialogFragment(NewsDetailsDialogViewModel viewModel, String title, String content, String imageLink){
        Bundle args = new Bundle();
        args.putString(NewsConstants.TITLE, title);
        args.putString(NewsConstants.CONTENT, content);
        args.putString(NewsConstants.IMAGE, imageLink);
        dialog = new NewsDetailsDialogFragment();
        dialog.setViewModel(viewModel);
        dialog.setArguments(args);
        dialog.show(activity.getFragmentManager(), "NewsDialog");
    }

    public void dismissNewsDialogFragmend(){
        dialog.dismiss();
    }
}
