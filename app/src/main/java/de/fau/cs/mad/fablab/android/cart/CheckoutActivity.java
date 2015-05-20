package de.fau.cs.mad.fablab.android.cart;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import java.util.regex.Pattern;

import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.ScannerFragment;
import eu.livotov.zxscan.ScannerView;

public class CheckoutActivity extends ActionBarActivity implements ScannerView.ScannerViewEventListener {
    private static final String TAG = CheckoutActivity.class.getSimpleName();
    private ScannerFragment scannerFragment;
    private Pattern qrCodePattern = Pattern.compile("(-)?\\d{1,19}");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);

        if (savedInstanceState == null) {
            if (scannerFragment == null) {
                scannerFragment = new ScannerFragment();
                scannerFragment.setScannerViewEventListener(this);
            }

            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,
                    scannerFragment).commit();
        }
    }

    @Override
    public boolean onCodeScanned(String data) {
        if (qrCodePattern.matcher(data).matches()) {
            long cartId;
            try {
                cartId = Long.parseLong(data);
            } catch (NumberFormatException e) {
                return false;
            }
            Log.i(TAG, "cartId: " + cartId);
            return true;
        }
        return false;
    }

    @Override
    public void onScannerFailure(int cameraError) {

    }

    @Override
    public void onScannerReady() {

    }
}
