package de.fau.cs.mad.fablab.android.view.common.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;

import butterknife.ButterKnife;
import de.fau.cs.mad.fablab.android.view.MainActivity;

public abstract class BaseDialogFragment extends DialogFragment {
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivity()).inject(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!getShowsDialog()) {
            MainActivity activity = (MainActivity) getActivity();
            activity.enableNavigationDrawer(false);
            activity.showFloatingActionButton(false);
            activity.showCartSlidingUpPanel(false);
        }
    }
}
