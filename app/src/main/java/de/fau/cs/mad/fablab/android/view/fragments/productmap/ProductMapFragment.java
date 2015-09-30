package de.fau.cs.mad.fablab.android.view.fragments.productmap;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Toast;

<<<<<<< HEAD
import javax.inject.Inject;
=======
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyStore;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
>>>>>>> change productmap

import butterknife.Bind;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.view.activities.MainActivity;
import de.fau.cs.mad.fablab.android.view.common.fragments.BaseDialogFragment;

<<<<<<< HEAD
public class ProductMapFragment extends BaseDialogFragment
        implements ProductMapFragmentViewModel.Listener {
=======
public class ProductMapFragment extends BaseDialogFragment implements CallBackListener
{
    private static final String LOG_TAG = "ProductMap";
    public static final String KEY_LOCATION = "key_location";
    private CallBackListener callBackListener = this;
    String FILENAME = "productMap.html";
>>>>>>> change productmap

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
        webview.setWebChromeClient(new WebChromeClient());

        //final String testUrl = "file:///android_asset/productMap.html?id=Werkbank";



        //save
        //new ConnectionTask().execute(productMapUrl);

        //load
        final String testUrl = "file:///" + getActivity().getFilesDir() + FILENAME;



        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setBuiltInZoomControls(true);
<<<<<<< HEAD

        mViewModel.setListener(this);
        mViewModel.initialize(getArguments());
=======
        webview.setWebChromeClient(new WebChromeClient());

        try{
        webview.loadUrl(testUrl);
    } catch (Exception e)
        {
            e.printStackTrace();
        }

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




>>>>>>> change productmap
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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
<<<<<<< HEAD
    public void onLoadMap(String url) {
        webview.loadUrl(url);
=======
    public void callback(String htmlText) {
        try {
            webview.loadData(htmlText, "text/html; charset=UTF-8", null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // next release
    private class ConnectionTask extends AsyncTask<String, Integer, String> {
        private CallBackListener mListener;


        public void setListener(CallBackListener listener) {
            mListener = listener;
        }


        @Override
        protected String doInBackground(String... urls) {
            BufferedReader bufferedReader;
            URL myUrl;
            HttpsURLConnection urlConnection;
            try {

                myUrl = new URL(urls[0]);
                urlConnection = (HttpsURLConnection) myUrl.openConnection();
                urlConnection.setSSLSocketFactory(getPinnedCertSslSocketFactory());

                bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                String htmlText = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null)
                    htmlText += line;




                return htmlText;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;


        }

        @Override
        protected void onPostExecute(String htmlText)
        {
            try
            {
                FileOutputStream fos = getActivity().openFileOutput(FILENAME, Context.MODE_PRIVATE);
                fos.write(htmlText.getBytes());
                fos.close();
            } catch (FileNotFoundException e)
            {
                e.printStackTrace();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
>>>>>>> change productmap
    }

    @Override
    public void onShowErrorMessage() {
        Toast.makeText(getActivity(), getString(R.string.product_map_download_failed),
                Toast.LENGTH_LONG).show();
    }
}
