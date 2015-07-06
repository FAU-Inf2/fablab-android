package de.fau.cs.mad.fablab.android.view.fragments.productmap;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.view.common.fragments.BaseFragment;

public class ProductMapFragment extends BaseFragment {
    private DrawingActivity mDrawView;

    @Inject
    ProductMapFragmentViewModel mViewModel;

    public static ProductMapFragment newInstance(String location) {
        ProductMapFragment productMapFragment = new ProductMapFragment();
        Bundle args = new Bundle();
        args.putString(ProductMapFragmentViewModel.KEY_LOCATION, location);
        productMapFragment.setArguments(args);
        return productMapFragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel.initialize(getArguments());

        mDrawView.setDrawingParameter(FablabView.MAIN_ROOM, mViewModel.getMainPositionX(),
                mViewModel.getMainPositionY(), mViewModel.getLocationName(),
                mViewModel.getIdentificationCode());
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDrawView = new DrawingActivity(getActivity());
        return mDrawView;
    }
}
