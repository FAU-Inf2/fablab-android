package de.fau.cs.mad.fablab.android.view.fragments.checkout;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.Bind;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.view.common.binding.ScannerViewCommandBinding;
import de.fau.cs.mad.fablab.android.view.common.fragments.BaseDialogFragment;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QrCodeScannerFragment extends BaseDialogFragment
        implements QrCodeScannerFragmentViewModel.Listener {
    @Bind(R.id.scanner)
    ZXingScannerView mScannerView;

    @Inject
    QrCodeScannerFragmentViewModel mViewModel;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mViewModel.setListener(this);

        new ScannerViewCommandBinding().bind(mScannerView, mViewModel.getProcessQrCodeCommand());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
    }

    @Override
    public void onShowInvalidQrCodeMessage() {
        Toast.makeText(getActivity(), R.string.qr_code_scanner_invalid_qr_code, Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void onStartCheckout(String cartCode) {
        getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                CheckoutFragment.newInstance(cartCode)).commit();
    }
}
