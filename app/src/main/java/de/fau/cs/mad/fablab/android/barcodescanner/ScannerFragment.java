package de.fau.cs.mad.fablab.android.barcodescanner;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerFragment extends Fragment {
    private ZXingScannerView mScannerView;
    private ZXingScannerView.ResultHandler mResultHandler;

    public static ScannerFragment newInstance(String title) {
        ScannerFragment scannerFragment = new ScannerFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        scannerFragment.setArguments(bundle);
        return scannerFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mResultHandler = (ZXingScannerView.ResultHandler) getActivity();
        } catch (ClassCastException e) {
            throw new RuntimeException(activity.toString() + " must implement "
                    + "ZXingScannerView.ResultHandler");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ActionBar actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(getArguments().getString("title"));
        actionBar.setDisplayHomeAsUpEnabled(false);

        mScannerView = new ZXingScannerView(getActivity());
        return mScannerView;
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(mResultHandler);
        mScannerView.startCamera();
    }

    public void startCamera() {
        mScannerView.startCamera();
    }
}
