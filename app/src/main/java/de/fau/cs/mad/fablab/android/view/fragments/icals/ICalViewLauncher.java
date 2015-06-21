package de.fau.cs.mad.fablab.android.view.fragments.icals;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.view.fragments.icals.viewpager.ICalDetailFragment;
import de.fau.cs.mad.fablab.android.view.fragments.icals.viewpager.ICalDetailFragmentViewModel;

public class ICalViewLauncher {

    @Inject
    FragmentManager fragmentManager;
    private ICalDetailFragment fragment;

    public void showDialog(ICalDetailFragmentViewModel viewModel, String title, String date, String time, String location) {
        //create dialog with bundle
        Bundle args = new Bundle();
        args.putString(ICalConstants.TITLE, title);
        args.putString(ICalConstants.DATE, date);
        args.putString(ICalConstants.TIME, time);
        args.putString(ICalConstants.LOCATION, location);
        fragment = new ICalDetailFragment();
        fragment.setViewModel(viewModel);
        fragment.setArguments(args);
        fragment.show(fragmentManager, "ICalDialog");
    }

    public void dismissDialog()
    {
        fragment.dismiss();
    }
}
