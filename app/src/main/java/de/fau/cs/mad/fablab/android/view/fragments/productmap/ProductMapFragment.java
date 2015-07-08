package de.fau.cs.mad.fablab.android.view.fragments.productMap;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import de.fau.cs.mad.fablab.android.R;

public class ProductMapFragment extends Fragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_locationmap, container, false);

        String locationId = "";
        try
        {
            if(savedInstanceState != null)
            {
                locationId = savedInstanceState.getString("locationID");
                String testurl = "http://google.com";
                //String url = "" + locationId;
                WebView view = (WebView) rootView.findViewById(R.id.location_webView);
                view.getSettings().setJavaScriptEnabled(true);
                view.loadUrl(testurl);

                return rootView;

            }
            else
                throw new IllegalArgumentException("no location id available");
        }
        catch (Exception exception)
        {
            getActivity().getFragmentManager().beginTransaction().remove(this).commit();
            throw exception;
        }



    }
}
