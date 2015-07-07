package de.fau.cs.mad.fablab.android.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import de.fau.cs.mad.fablab.android.R;

public class UiUtils {

    public static void showSpinner(View spinnerContainerView, ImageView spinnerImageView) {
        spinnerContainerView.setVisibility(View.VISIBLE);

        AnimationDrawable animationDrawable = (AnimationDrawable) spinnerImageView.getBackground();
        animationDrawable.start();
    }


    public static void hideSpinner(View spinnerContainerView, ImageView spinnerImageView) {
        AnimationDrawable animationDrawable = (AnimationDrawable) spinnerImageView.getBackground();
        animationDrawable.stop();

        spinnerContainerView.setVisibility(View.GONE);
    }

    public static void hideKeyboard(Activity activity) {
        if (activity.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                    0);
        }
    }

    public static void changeDialogTitleColor(Dialog dialog)
    {
        int dividerId = dialog.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider = dialog.findViewById(dividerId);
        if (divider != null) {
            divider.setBackgroundColor(dialog.getContext().getResources().getColor(R.color.colorPrimary));
        }
        int textViewId = dialog.getContext().getResources().getIdentifier("android:id/alertTitle", null, null);
        TextView tv = (TextView) dialog.findViewById(textViewId);
        tv.setTextColor(dialog.getContext().getResources().getColor(R.color.colorPrimary));
    }
}
