package de.fau.cs.mad.fablab.android.view.fragments.checkout;

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
import de.greenrobot.event.EventBus;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QrCodeScannerFragment extends BaseFragment
        implements QrCodeScannerFragmentViewModel.Listener {
    @InjectView(R.id.scanner)
    ZXingScannerView mScannerView;

    @Inject
    QrCodeScannerFragmentViewModel mViewModel;

    private EventBus mEventBus = EventBus.getDefault();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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
        mEventBus.unregister(this);
        mViewModel.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mEventBus.register(this);
        mViewModel.resume();
    }

    @Override
    public void onShowInvalidQrCodeMessage() {
        Toast.makeText(getActivity(), R.string.qr_code_scanner_invalid_qr_code, Toast.LENGTH_LONG)
                .show();
    }

    @SuppressWarnings("unused")
    public void onEvent(QrCodeScannedEvent event) {
        String qrCodeText = event.getQrCodeText();
        // TODO start checkout
    }
}