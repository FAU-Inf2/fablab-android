package de.fau.cs.mad.fablab.android.view.fragments.productmap;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import butterknife.InjectView;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.view.common.fragments.BaseDialogFragment;

public class ProductMapFragment extends BaseDialogFragment {
    public static final String KEY_LOCATION = "key_location";

    @InjectView(R.id.location_webView)
    WebView webview;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String locationId = getArguments().getString(KEY_LOCATION);
        //Todo: activate when server method is finished
        String testurl = "https://52.28.16.59:4433/productMap/index.html";
        String url = "";
        if(locationId != "")
            url = testurl + "?id=" + locationId;
        else url = testurl;
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl(url);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_locationmap, container, false);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
    }
}
