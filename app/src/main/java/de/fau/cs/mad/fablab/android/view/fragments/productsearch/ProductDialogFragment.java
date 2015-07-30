package de.fau.cs.mad.fablab.android.view.fragments.productsearch;

import android.content.Intent;
import android.net.Uri;
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
import de.fau.cs.mad.fablab.rest.core.Product;

public class ProductDialogFragment extends BaseDialogFragment
        implements ProductDialogFragmentViewModel.Listener {

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
        mViewModel.initialize(getArguments());

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
            new ViewCommandBinding().bind(mLocationButton, mViewModel.getShowLocationCommand());
        }else{
            mLocationButton.setEnabled(false);
        }

        mViewModel.setListener(this);
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
        dismiss();
        Product outProduct = mViewModel.getProduct();
        Intent sendIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", mViewModel.getMailAddress(), null));
        String subject = getActivity().getString(R.string.outOfStock_messaging_subject);
        String body = "Product is out of stock:" + "\n";
        body += "product name: " + outProduct.getName() + "\n";
        body += "product id: " + outProduct.getProductId() + "\n";

        sendIntent.putExtra(Intent.EXTRA_TEXT, body);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, subject);

        getActivity().startActivity(Intent.createChooser(sendIntent, getActivity().getString(R.string.outOfStock_messaging_chooser_title)));
    }

    @Override
    public void onShowLocation() {
        dismiss();

        ProductMapFragment productMapFragment = new ProductMapFragment();
        Bundle arguments = new Bundle();
        arguments.putString(ProductMapFragment.KEY_LOCATION, mViewModel.getProductLocation());
        productMapFragment.setArguments(arguments);
        getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                productMapFragment).addToBackStack(null).commit();
    }
}
