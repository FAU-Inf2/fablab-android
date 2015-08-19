package de.fau.cs.mad.fablab.android.view.fragments.productmap;

import android.content.pm.ActivityInfo;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import butterknife.Bind;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.view.common.fragments.BaseDialogFragment;

public class ProductMapFragment extends BaseDialogFragment {
    public static final String KEY_LOCATION = "key_location";
    //public static final String TRUSTSTORE_KEY = "gWWB5-P5eiVL6evqC cI274Kq-bjYc-P719gaYrp-1D_zRw4pA _7KsPsRFW4ivjib5i7bW3fvnXvwLgtkGPU88ob_C_0fvXV_RN";

    @Bind(R.id.location_webView)
    WebView webview;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        String locationString = getArguments().getString(KEY_LOCATION);
        //Todo: activate when server method is finished
        String productMapUrl = getString(R.string.api_url) + "/productMap/productMap.html";
        String url = "";
        if(locationString != "")
            url = productMapUrl + "?id=" + locationString;
        else url = productMapUrl;

//        try
//        {
//            KeyStore localTrustStore = KeyStore.getInstance("BKS");
//            InputStream in = getResources().openRawResource(R.raw.fablab_dev_truststore);
//            localTrustStore.load(in, TRUSTSTORE_KEY.toCharArray());
//
//            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
//            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
//            tmf.init(localTrustStore);
//
//            SSLContext context = SSLContext.getInstance("TLS");
//            context.init(null, tmf.getTrustManagers(), null);
//
//            URL newUrl = new URL(url);
//            HttpsURLConnection urlConnection = (HttpsURLConnection)newUrl.openConnection();
//            urlConnection.setSSLSocketFactory(context.getSocketFactory());
//            InputStream inputStream = urlConnection.getInputStream();
//
//            webview.getSettings().setJavaScriptEnabled(true);
//            webview.loadUrl(url);
//
//        } catch (Exception e)
//        {
//            e.printStackTrace();
//        }

        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl(url);
        webview.setWebViewClient(new WebViewClient()
        {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error)
            {
                handler.proceed();
            }
        });


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
