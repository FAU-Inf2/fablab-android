package de.fau.cs.mad.fablab.android.view.fragments.barcodescanner;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.Bind;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.view.activities.MainActivity;
import de.fau.cs.mad.fablab.android.view.common.binding.ScannerViewCommandBinding;
import de.fau.cs.mad.fablab.android.view.common.fragments.BaseFragment;
import de.fau.cs.mad.fablab.android.view.fragments.cart.AddToCartDialogFragment;
import de.fau.cs.mad.fablab.android.view.fragments.checkout.CheckoutFragment;
import de.fau.cs.mad.fablab.rest.core.Product;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class BarcodeScannerFragment extends BaseFragment
        implements BarcodeScannerFragmentViewModel.Listener {
    @Bind(R.id.scanner)
    protected ZXingScannerView mScannerView;

    @Inject
    protected BarcodeScannerFragmentViewModel mViewModel;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel.setListener(this);

        new ScannerViewCommandBinding().bind(mScannerView, mViewModel.getProcessBarcodeCommand());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        return inflater.inflate(R.layout.fragment_scanner, container, false);

    }

    @Override
    public void onPause() {
        super.onPause();
        mViewModel.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.resume();

        setDisplayOptions(MainActivity.DISPLAY_TITLE | MainActivity.DISPLAY_NAVDRAWER);
        setNavigationDrawerSelection(R.id.drawer_item_scanner);
        setTitle(getString(R.string.title_scan_product));
    }

    @Override
    public void onShowAddToCartDialog(Product product) {
        getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                AddToCartDialogFragment.newInstance(product)).addToBackStack(null)
                .commit();
    }

    @Override
    public void onShowProductNotFoundMessage() {
        Toast.makeText(getActivity(), R.string.barcode_scanner_product_not_found, Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void onShowInvalidBarcodeMessage() {
        Toast.makeText(getActivity(), R.string.barcode_scanner_invalid_barcode, Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void onStartCheckout(String cartCode) {
        getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                CheckoutFragment.newInstance(cartCode)).commit();
    }
}
