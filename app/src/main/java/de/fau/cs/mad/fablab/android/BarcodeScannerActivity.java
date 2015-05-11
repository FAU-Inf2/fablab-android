package de.fau.cs.mad.fablab.android;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import eu.livotov.zxscan.ScannerView;


public class BarcodeScannerActivity extends ActionBarActivity
        implements ScannerView.ScannerViewEventListener {
    private ScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcodescanner);
        mScannerView = (ScannerView) findViewById(R.id.scanner);
        mScannerView.setScannerViewEventListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mScannerView.startScanner();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mScannerView.stopScanner();
    }

    @Override
    public boolean onCodeScanned(final String data) {
        Toast.makeText(this, data, Toast.LENGTH_LONG).show();
        return true;
    }

    @Override
    public void onScannerFailure(int cameraError) {

    }

    @Override
    public void onScannerReady() {

    }
}
