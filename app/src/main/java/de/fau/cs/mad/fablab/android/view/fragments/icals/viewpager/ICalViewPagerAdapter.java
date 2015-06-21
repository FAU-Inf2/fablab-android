package de.fau.cs.mad.fablab.android.view.fragments.icals.viewpager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import de.fau.cs.mad.fablab.android.view.common.viewpager.BaseViewPagerAdapter;
import de.fau.cs.mad.fablab.android.view.fragments.icals.ICalViewPagerFragmentViewModel;
import de.fau.cs.mad.fablab.rest.core.ICal;

public class ICalViewPagerAdapter extends BaseViewPagerAdapter<ICal> {

    ICalViewPagerFragmentViewModel viewModelViewPager;
    ICalFragmentViewModel viewModelFragment;

    public ICalViewPagerAdapter(FragmentManager fm, ICalViewPagerFragmentViewModel viewModelViewPager, ICalFragmentViewModel viewModelFragment) {
        super(fm);
        this.viewModelViewPager = viewModelViewPager;
        this.viewModelFragment = viewModelFragment;
    }

    @Override
    public Fragment getItem(int position) {

        int arrayPosition = position * 2;
        ICalFragment fragment = new ICalFragment();

        if(viewModelViewPager.getData().size() > 0) {

            if (arrayPosition + 1 < viewModelViewPager.getData().size()) {
                viewModelFragment.setItems(viewModelViewPager.getData().get(arrayPosition), viewModelViewPager.getData().get(arrayPosition + 1));
            } else {
                viewModelFragment.setItems(viewModelViewPager.getData().get(arrayPosition), null);
            }

        }
        fragment.setViewModel(viewModelFragment);

        return fragment;
    }

    @Override
    public int getCount() {
        return (viewModelViewPager.getData().size()+1)/2;
    }
}
