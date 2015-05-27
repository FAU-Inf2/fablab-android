package de.fau.cs.mad.fablab.android;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ScannerFragment extends eu.livotov.zxscan.ScannerFragment {
    private String lastBarcodeScanned;
    private long lastTimeScanned;

    public static ScannerFragment newInstance(String title) {
        ScannerFragment scannerFragment = new ScannerFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        scannerFragment.setArguments(bundle);
        return scannerFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((ActionBarActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(getArguments().getString(
                "title"));
        ((ActionBarActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
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
}
