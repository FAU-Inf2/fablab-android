package de.fau.cs.mad.fablab.android.view.fragments.barcodescanner;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.InjectView;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.view.common.binding.ScannerViewCommandBinding;
import de.fau.cs.mad.fablab.android.view.common.fragments.BaseFragment;
import de.fau.cs.mad.fablab.android.view.fragments.cart.AddToCartDialogFragment;
import de.greenrobot.event.EventBus;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class BarcodeScannerFragment extends BaseFragment
        implements BarcodeScannerFragmentViewModel.Listener {
    @InjectView(R.id.scanner)
    ZXingScannerView mScannerView;

    @Inject
    BarcodeScannerFragmentViewModel mViewModel;

    private EventBus mEventBus = EventBus.getDefault();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel.setListener(this);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        new ScannerViewCommandBinding().bind(mScannerView, mViewModel.getProcessBarcodeCommand());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_scanner, container, false);

    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        mEventBus.unregister(this);
        mViewModel.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        mEventBus.register(this);
        mViewModel.resume();

        setDisplayOptions(R.id.drawer_item_scanner, false, false);
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

    @SuppressWarnings("unused")
    public void onEvent(ProductFoundEvent event) {
        getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                AddToCartDialogFragment.newInstance(event.getProduct())).addToBackStack(null)
                .commit();
    }
}
