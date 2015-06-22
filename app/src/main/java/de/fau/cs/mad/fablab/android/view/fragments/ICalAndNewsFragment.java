package de.fau.cs.mad.fablab.android.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.view.common.fragments.BaseFragment;

public class ICalAndNewsFragment extends BaseFragment {
    @Inject
    public ICalAndNewsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ical_and_news, container, false);
    }
}
