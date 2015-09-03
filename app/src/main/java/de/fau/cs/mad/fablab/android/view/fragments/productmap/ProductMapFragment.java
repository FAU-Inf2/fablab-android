package de.fau.cs.mad.fablab.android.view.fragments.productmap;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyStore;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

//import butterknife.InjectView;
import butterknife.Bind;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.view.common.fragments.BaseDialogFragment;

public class ProductMapFragment extends BaseDialogFragment implements CallBackListener
{
    private static final String LOG_TAG = "ProductMap";
    public static final String KEY_LOCATION = "key_location";
    private CallBackListener callBackListener = this;

    @Bind(R.id.location_webView)
    WebView webview;

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);


        //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Todo: activate when server method is finished
        String productMapUrl = getString(R.string.api_url) + "/productMap/productMap.html";
        String locationString = getArguments().getString(KEY_LOCATION); //something like "fau fablab / werkstadt / ..."

        locationString = locationString.replace("_/", "/");
        locationString = locationString.replace(",_", "_");
        locationString = locationString.replace("ä", "ae");
        locationString = locationString.replace("ö", "oe");
        locationString = locationString.replace("ü", "ue");
        locationString = locationString.replace("ß", "ss");


        String url = "";
        if (locationString != "")
            url = productMapUrl + "?id=" + locationString;
        else url = productMapUrl;


        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setBuiltInZoomControls(true);
        webview.setWebChromeClient(new WebChromeClient());

        final String finalUrl = url;
        webview.loadUrl(url);
        webview.setWebViewClient(new WebViewClient()
        {

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error)
            {

                //Method 1
                handler.proceed();

                //Method 2
                //ConnectionTask connectionTask = new ConnectionTask();
                //connectionTask.setListener(callBackListener);

                //connectionTask.execute(finalUrl);
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_locationmap, container, false);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

    }

    @Override
    public void callback(String htmlText)
    {
        try
        {
            webview.loadData(htmlText, "text/html; charset=UTF-8", null);

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    // next release
    private class ConnectionTask extends AsyncTask<String, Integer, String>
    {
        private CallBackListener mListener;


        public void setListener(CallBackListener listener)
        {
            mListener = listener;
        }


        @Override
        protected String doInBackground(String... urls)
        {
            BufferedReader bufferedReader;
            URL myUrl;
            HttpsURLConnection urlConnection;
            try
            {

                myUrl = new URL(urls[0]);
                urlConnection = (HttpsURLConnection) myUrl.openConnection();
                urlConnection.setSSLSocketFactory(getPinnedCertSslSocketFactory());

                bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                String htmlText = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null)
                    htmlText += line;

                return htmlText;

            } catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;


        }

        @Override
        protected void onPostExecute(String htmlText)
        {
            mListener.callback(htmlText);
        }
    }

    /**
     * Creates and returns a SSLSocketFactory which will trust our selfsigned certificates in
     * fablab_dev_truststore. The truststore only contains the public certs.
     *
     * @return a SSLSocketFactory trusting our selfsigned certs.
     */
    private SSLSocketFactory getPinnedCertSslSocketFactory()
    {
        try
        {
            //Default type is BKS on android
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            //our truststore containing the public certs
            InputStream inputStream = getResources().openRawResource(R.raw.fablab_dev_truststore);

            //the password used here is just a dummy as it is needed by the keystore.load method
            String trustStorePass = "dummypass4dev";
            keyStore.load(inputStream, trustStorePass.toCharArray());

            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                    TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            sslContext.init(null, trustManagerFactory.getTrustManagers(), null);

            return sslContext.getSocketFactory();

        } catch (Exception e)
        {
            Log.e(LOG_TAG, e.getMessage());
        }
        return null;
    }
}

interface CallBackListener
{
    void callback(String htmlText);
}

