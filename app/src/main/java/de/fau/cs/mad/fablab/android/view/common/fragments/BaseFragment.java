package de.fau.cs.mad.fablab.android.view.common.fragments;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import butterknife.ButterKnife;
import de.fau.cs.mad.fablab.android.view.activities.MainActivity;

/**
 * Base class for all Fragments
 */
public abstract class BaseFragment extends Fragment{
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivity()).inject(this);

        //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    public void setDisplayOptions(int menuItemId, boolean showTitle, boolean showFAB,
                                  boolean showCart) {
        MainActivity activity = (MainActivity) getActivity();
        activity.enableNavigationDrawer(true);
        activity.showTitle(showTitle);
        activity.setNavigationDrawerSelection(menuItemId);
        activity.showFloatingActionButton(showFAB);
        activity.showCartSlidingUpPanel(showCart);
    }
}
