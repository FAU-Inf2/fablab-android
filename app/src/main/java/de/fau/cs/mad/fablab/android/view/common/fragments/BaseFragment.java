package de.fau.cs.mad.fablab.android.view.common.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import butterknife.ButterKnife;
import de.fau.cs.mad.fablab.android.view.MainActivity;

/***
 * Base class for all Fragments
 */
public abstract class BaseFragment extends Fragment{
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).inject(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);
    }
}
