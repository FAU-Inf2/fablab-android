package de.fau.cs.mad.fablab.android.view.common.viewpager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import de.fau.cs.mad.fablab.android.viewmodel.common.BaseAdapterViewModel;

public abstract class BaseViewPagerAdapter<ContentType> extends FragmentStatePagerAdapter{

    protected final BaseAdapterViewModel<ContentType> viewModel;

    public BaseViewPagerAdapter(FragmentManager fm) {
        super(fm);
        viewModel = null;
    }

    /***
     * We only want derived classes
     */
    protected BaseViewPagerAdapter(FragmentManager fm, BaseAdapterViewModel viewModel) {
        super(fm);
        this.viewModel = viewModel;
    }

    @Override
    public abstract Fragment getItem(int position);

    @Override
    public abstract int getCount();


}
