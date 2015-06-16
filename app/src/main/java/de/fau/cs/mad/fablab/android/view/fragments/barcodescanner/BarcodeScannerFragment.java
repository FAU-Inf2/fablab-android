package de.fau.cs.mad.fablab.android.view.fragments.barcodescanner;

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
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class BarcodeScannerFragment extends BaseFragment
        implements BarcodeScannerFragmentViewModel.Listener {
    @InjectView(R.id.scanner)
    ZXingScannerView mScannerView;

    @Inject
    BarcodeScannerFragmentViewModel mViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_barcode_scanner, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel.setListener(this);

        new ScannerViewCommandBinding().bind(mScannerView, mViewModel.getProcessBarcodeCommand());
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
}
