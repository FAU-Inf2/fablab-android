package de.fau.cs.mad.fablab.android;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ScannerFragment extends eu.livotov.zxscan.ScannerFragment {
    private String lastBarcodeScanned;
    private long lastTimeScanned;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public boolean onCodeScanned(final String data) {
        if ((data.equals(lastBarcodeScanned) && (System.currentTimeMillis() - lastTimeScanned)
                < 5000)) {
            return false;
        }
        lastBarcodeScanned = data;
        lastTimeScanned = System.currentTimeMillis();

        return scannerViewEventListener != null && scannerViewEventListener.onCodeScanned(data);
    }

    public void startDecoding() {
        scanner.setScannerViewEventListener(this);
    }

    public void stopDecoding() {
        scanner.setScannerViewEventListener(null);
    }
}
