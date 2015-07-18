package de.fau.cs.mad.fablab.android.view.fragments.productsearch;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.InjectView;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.view.common.binding.ViewCommandBinding;
import de.fau.cs.mad.fablab.android.view.common.fragments.BaseDialogFragment;
import de.fau.cs.mad.fablab.android.view.fragments.cart.AddToCartDialogFragment;
import de.fau.cs.mad.fablab.android.view.fragments.productmap.ProductMapFragment;

public class ProductDialogFragment extends BaseDialogFragment implements ProductDialogFragmentViewModel.Listener{

    @Inject
    ProductDialogFragmentViewModel mViewModel;

    @InjectView(R.id.product_dialog_title)
    TextView mDialogTitle;
    @InjectView(R.id.product_dialog_cart)
    Button mCartButton;
    @InjectView(R.id.product_dialog_location)
    Button mLocationButton;
    @InjectView(R.id.product_dialog_report)
    Button mReportButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return inflater.inflate(R.layout.product_dialog, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel.setListener(this);
        mViewModel.restoreState(getArguments(), savedInstanceState);

        mDialogTitle.setText(mViewModel.getProductName());
        mLocationButton.setEnabled(mViewModel.hasLocation());

        if(mViewModel.isProductZeroPriced()){
            mCartButton.setEnabled(false);
            mCartButton.setClickable(false);
            mReportButton.setEnabled(false);
            mReportButton.setClickable(false);

        }else{
            new ViewCommandBinding().bind(mCartButton, mViewModel.getAddToCartCommand());
            new ViewCommandBinding().bind(mReportButton, mViewModel.getReportOutOfStockCommand());
        }

        if(mViewModel.hasLocation()){
            //Todo: activate and choose correct getLocation method
            new ViewCommandBinding().bind(mLocationButton, mViewModel.getShowLocationCommand());
        }else{
            mLocationButton.setEnabled(false);
        }

        mViewModel.setListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("location", "088/0815");
        mViewModel.saveState(outState);
    }

    @Override
    public void onAddToCart() {
        dismiss();
        getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                AddToCartDialogFragment.newInstance(mViewModel.getProduct())).addToBackStack(null)
                .commit();
    }

    @Override
    public void onOutOfStock() {
        //TODO
    }

    @Override
    public void onShowLocation() {
        dismiss();
        Bundle bundle = new Bundle();

        bundle.putString("location", "0820/0815");
        ProductMapFragment pm = new ProductMapFragment();
        pm.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                pm).addToBackStack(null).commit();
    }
}
