package de.fau.cs.mad.fablab.android.view.fragments.productmap;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.view.common.fragments.BaseDialogFragment;

public class ProductMapFragment extends BaseDialogFragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_locationmap, container, false);

        String locationId = "";
        try
        {
            Bundle bundle = this.getArguments();
            if (bundle != null)
            {
                //Todo: activate when server method is finished
                locationId = bundle.getString("location");
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
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
