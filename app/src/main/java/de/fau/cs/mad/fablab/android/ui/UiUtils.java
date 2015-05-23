package de.fau.cs.mad.fablab.android.ui;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.ImageView;

import de.fau.cs.mad.fablab.android.R;

public class UiUtils {

    private final Context context;

    public UiUtils(Context context) {
        this.context = context;
    }


    public void showSpinner(View spinnerContainerView, ImageView spinnerImageView) {
        spinnerContainerView.setVisibility(View.VISIBLE);

        spinnerImageView.setBackgroundResource(R.drawable.spinner);
        AnimationDrawable animationDrawable = (AnimationDrawable) spinnerImageView.getBackground();
        animationDrawable.start();
    }


    public void hideSpinner(View spinnerContainerView, ImageView spinnerImageView) {
        AnimationDrawable animationDrawable = (AnimationDrawable) spinnerImageView.getBackground();
        animationDrawable.stop();

        spinnerContainerView.setVisibility(View.GONE);
    }
}
