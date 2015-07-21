package de.fau.cs.mad.fablab.android.view.fragments.productmap;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.view.common.binding.ScannerViewCommandBinding;
import de.fau.cs.mad.fablab.android.view.common.fragments.BaseDialogFragment;

public class ProductMapFragment extends BaseDialogFragment
{
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_locationmap, container, false);
        //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        String locationId = "";
        try
        {
            Bundle locationMapBundle = this.getArguments();
            if (locationMapBundle != null)
            {
                //Todo: activate when server method is finished
                locationId = locationMapBundle.getString("location");
                String testurl = "http://52.28.16.59/productMap/index.html";
                String url = testurl + "?id=" + locationId;
                WebView view = (WebView) rootView.findViewById(R.id.location_webView);
                view.getSettings().setJavaScriptEnabled(true);
                view.loadUrl(url);

                return rootView;

            } else
                throw new IllegalArgumentException("no location id available");
        } catch (Exception exception)
        {
            //getActivity().getFragmentManager().beginTransaction().remove(this).commit();
            //throw exception;
        }

        return rootView;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    }
}
