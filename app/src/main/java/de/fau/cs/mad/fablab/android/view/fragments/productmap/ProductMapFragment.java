package de.fau.cs.mad.fablab.android.view.fragments.productmap;

import android.content.pm.ActivityInfo;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyStore;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

//import butterknife.InjectView;
import butterknife.Bind;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.view.common.fragments.BaseDialogFragment;

public class ProductMapFragment extends BaseDialogFragment implements CallBackListener
{
    public static final String KEY_LOCATION = "key_location";
    private CallBackListener callBackListener = this;

    @Bind(R.id.location_webView)
    WebView webview;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Todo: activate when server method is finished
        String productMapUrl = getString(R.string.api_url) + "/productMap/productMap.html";
        String locationString = getArguments().getString(KEY_LOCATION); //something like "fau fablab / werkstadt / ..."

        locationString = locationString.replace("_/", "/");
        locationString = locationString.replace(",_","_");
        locationString = locationString.replace("ä", "ae");
        locationString = locationString.replace("ö", "oe");
        locationString = locationString.replace("ü", "ue");
        locationString = locationString.replace("ß", "ss");

        //test with local asset
       // productMapUrl = "file:///android_asset/productMap.html?id=Werkbank";

        String url = "";
        if(locationString != "")
            url = productMapUrl + "?id=" + locationString;
        else url = productMapUrl;


        /*3 creating a WebViewClient and override the
        shouldOverrideUrlLoading method to setup a SSLSocket and pass the
        received html data to WebView.loadData. However, I think there should
        be a way to tell WebView directly which keystore and truststore it
        should use to setup SSL connections.*/

        webview.getSettings().setJavaScriptEnabled(true);

        final String finalUrl = url;
        webview.loadUrl(url);
        webview.setWebViewClient(new WebViewClient()
        {

            @Override
            public void onReceivedSslError (WebView view, SslErrorHandler handler, SslError error)
            {

                handler.proceed();
                // next release
//                try
//                {
//                    //Default type is BKS on android
//                    KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
//                    //our truststore containing the public certs
//                    InputStream inputStream = getResources().openRawResource(R.raw.fablab_dev_truststore);
//
//                    //the password used here is just a dummy as it is needed by the keystore.load method
//                    String trustStorePass = "dummypass4dev";
//                    keyStore.load(inputStream, trustStorePass.toCharArray());
//
//                    SSLContext sslContext = SSLContext.getInstance("TLS");
//                    TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
//                            TrustManagerFactory.getDefaultAlgorithm());
//                    trustManagerFactory.init(keyStore);
//                    sslContext.init(null, trustManagerFactory.getTrustManagers(), null);
//
//
//
//                    ConnectionTask connectionTask = new ConnectionTask();
//                    connectionTask.setListener(callBackListener);
//
//                    connectionTask.execute(finalUrl);
//
////                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
////
//
//                }
//                catch (android.os.NetworkOnMainThreadException e)
//                {
//                    handler.proceed();
//                }
//                catch (Exception e)
//                {
//                    e.printStackTrace();
//                }
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

    @Override
    public void callback(BufferedReader bufferedReader)
    {
        try
        {
            String htmlText = "";
            String line = "";
            while ((line = bufferedReader.readLine()) != null)
                htmlText += line;

            webview.loadData(htmlText, "text/html; charset=UTF-8", null);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    // next release
    private class ConnectionTask extends AsyncTask<String, Integer , BufferedReader>
    {
        private CallBackListener mListener;


        public void setListener(CallBackListener listener)
        {
            mListener = listener;
        }


        @Override
        protected BufferedReader doInBackground(String... urls)
        {
            BufferedReader bufferedReader;
            URL myUrl;
            HttpsURLConnection urlConnection;
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

                myUrl = new URL(urls[0]);
                urlConnection = (HttpsURLConnection) myUrl.openConnection();
                urlConnection.setSSLSocketFactory(sslContext.getSocketFactory());


                bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                return bufferedReader;
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
                return null;
            }

        }

        @Override
        protected void onPostExecute(BufferedReader bufferedReader) {
            mListener.callback(bufferedReader);
        }
    }

}
interface CallBackListener
{
    public void callback(BufferedReader bufferedReader);
}

