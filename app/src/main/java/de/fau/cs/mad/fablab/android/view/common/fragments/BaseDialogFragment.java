package de.fau.cs.mad.fablab.android.view.common.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;

import butterknife.ButterKnife;
import de.fau.cs.mad.fablab.android.view.activities.MainActivity;

public abstract class BaseDialogFragment extends DialogFragment {
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivity()).inject(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    public void setDisplayOptions(int options) {
        ((MainActivity) getActivity()).setDisplayOptions(options);
    }

    public void setNavigationDrawerSelection(int menuItemId) {
        ((MainActivity) getActivity()).setNavigationDrawerSelection(menuItemId);
    }

    public void setTitle(int titleId) {
        getActivity().setTitle(titleId);
    }
}
