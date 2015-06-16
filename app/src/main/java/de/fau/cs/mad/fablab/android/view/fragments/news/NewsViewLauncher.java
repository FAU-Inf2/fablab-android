package de.fau.cs.mad.fablab.android.view.fragments.news;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import javax.inject.Inject;

public class NewsViewLauncher {

    @Inject
    FragmentManager fragmentManager;
    private NewsDetailsDialogFragment dialog;

    public void showNewsDialogFragment(NewsDetailsDialogViewModel viewModel, String title, String content, String imageLink){
        Bundle args = new Bundle();
        args.putString(NewsConstants.TITLE, title);
        args.putString(NewsConstants.CONTENT, content);
        args.putString(NewsConstants.IMAGE, imageLink);
        dialog = new NewsDetailsDialogFragment();
        dialog.setViewModel(viewModel);
        dialog.setArguments(args);
        dialog.show(fragmentManager, "NewsDialog");
    }

    public void dismissNewsDialogFragmend(){
        dialog.dismiss();
    }
}
