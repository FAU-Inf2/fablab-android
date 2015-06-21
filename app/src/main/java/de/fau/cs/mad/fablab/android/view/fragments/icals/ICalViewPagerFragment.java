package de.fau.cs.mad.fablab.android.view.fragments.icals;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import butterknife.InjectView;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.view.common.binding.ViewPagerCommandBinding;
import de.fau.cs.mad.fablab.android.view.common.fragments.BaseFragment;
import de.fau.cs.mad.fablab.android.view.fragments.icals.viewpager.ICalFragmentViewModel;
import de.fau.cs.mad.fablab.android.view.fragments.icals.viewpager.ICalViewPagerAdapter;

public class ICalViewPagerFragment extends BaseFragment implements ICalViewPagerFragmentViewModel.Listener{

    @InjectView(R.id.dates_view_pager)
    ViewPager datesViewPager;

    @Inject
    ICalViewPagerFragmentViewModel viewModel;
    @Inject
    ICalFragmentViewModel viewModelFragment;

    public ICalViewPagerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel.setListener(this);

        datesViewPager.setAdapter(new ICalViewPagerAdapter(getFragmentManager(), viewModel, viewModelFragment));
        new ViewPagerCommandBinding().bind(datesViewPager, viewModel.getGetICalCommand());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dates_view_pager, container, false);
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onListDataChanged() {
        datesViewPager.getAdapter().notifyDataSetChanged();
    }
}
