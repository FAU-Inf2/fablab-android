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

import butterknife.InjectView;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.view.common.fragments.BaseDialogFragment;

public class ProductMapFragment extends BaseDialogFragment {
    public static final String KEY_LOCATION = "key_location";
    public static final String TRUSTSTORE_KEY = "gWWB5-P5eiVL6evqC cI274Kq-bjYc-P719gaYrp-1D_zRw4pA _7KsPsRFW4ivjib5i7bW3fvnXvwLgtkGPU88ob_C_0fvXV_RN";

    @InjectView(R.id.location_webView)
    WebView webview;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        String locationId = getArguments().getString(KEY_LOCATION);
        //Todo: activate when server method is finished
        String testurl = "https://52.28.16.59:4433/productMap/index.html";
        String url = "";
        if(locationId != "")
            url = testurl + "?id=" + locationId;
        else url = testurl;

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
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
