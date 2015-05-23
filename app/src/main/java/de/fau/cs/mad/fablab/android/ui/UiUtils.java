package de.fau.cs.mad.fablab.android.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.view.WindowManager;
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

    public void disableActivityInput(Activity activity)
    {
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public void enableActivityInput(Activity activity)
    {
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }
}
