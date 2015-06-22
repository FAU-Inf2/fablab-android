package de.fau.cs.mad.fablab.android.view.fragments.icals.viewpager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import de.fau.cs.mad.fablab.android.view.MainActivity;
import de.fau.cs.mad.fablab.android.view.common.viewpager.BaseViewPagerAdapter;
import de.fau.cs.mad.fablab.android.view.fragments.icals.ICalViewPagerFragmentViewModel;
import de.fau.cs.mad.fablab.rest.core.ICal;

public class ICalViewPagerAdapter extends BaseViewPagerAdapter<ICal> {

    ICalViewPagerFragmentViewModel viewModelViewPager;
    MainActivity activity;

    public ICalViewPagerAdapter(MainActivity activity, FragmentManager fm, ICalViewPagerFragmentViewModel viewModelViewPager) {
        super(fm);
        this.viewModelViewPager = viewModelViewPager;
        this.activity = activity;
    }

    @Override
    public Fragment getItem(int position) {



        int arrayPosition = position * 2;
        ICalFragment fragment = new ICalFragment();
        activity.inject(fragment);

        if(viewModelViewPager.getData().size() > 0) {

            if (arrayPosition + 1 < viewModelViewPager.getData().size()) {
                fragment.getViewModel().setItems(viewModelViewPager.getData().get(arrayPosition), viewModelViewPager.getData().get(arrayPosition + 1));
            } else {
                fragment.getViewModel().setItems(viewModelViewPager.getData().get(arrayPosition), null);
            }

        }

        return fragment;
    }

    @Override
    public int getCount() {
        return (viewModelViewPager.getData().size()+1)/2;
    }
}
