package de.fau.cs.mad.fablab.android.view.fragments.productmap;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.Bind;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.view.activities.MainActivity;
import de.fau.cs.mad.fablab.android.view.common.fragments.BaseDialogFragment;

public class ProductMapFragment extends BaseDialogFragment
        implements ProductMapFragmentViewModel.Listener {

    @Bind(R.id.location_webView)
    WebView webview;

    @Inject
    ProductMapFragmentViewModel mViewModel;

    public static ProductMapFragment newInstance(String location) {
        ProductMapFragment fragment = new ProductMapFragment();
        Bundle args = new Bundle();
        args.putString(ProductMapFragmentViewModel.KEY_LOCATION, location);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setBuiltInZoomControls(true);

        mViewModel.setListener(this);
        mViewModel.initialize(getArguments());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        return inflater.inflate(R.layout.fragment_locationmap, container, false);
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
        setDisplayOptions(MainActivity.DISPLAY_LOGO);
    }

    @Override
    public void onLoadMap(String url) {
        webview.loadUrl(url);
    }

    @Override
    public void onShowErrorMessage() {
        Toast.makeText(getActivity(), getString(R.string.product_map_download_failed),
                Toast.LENGTH_LONG).show();
    }
}