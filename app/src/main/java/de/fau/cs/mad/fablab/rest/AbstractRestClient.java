package de.fau.cs.mad.fablab.rest;


import android.content.Context;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;

import java.io.InputStream;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import de.fau.cs.mad.fablab.android.BuildConfig;
import de.fau.cs.mad.fablab.android.R;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

/** Super class for the individual clients
 * that implements common methods and features.
 * - API_URL
 * - General RestAdapter that later implements the REST Interface
 */
public abstract class AbstractRestClient{

    protected final String LOG_TAG = "AbstractRestClient";

    protected RestAdapter restAdapter;
    protected Context mContext;

    protected AbstractRestClient(Context context){

        mContext = context;
        final String API_URL = mContext.getResources().getString(R.string.API_URL);

        OkHttpClient httpClient = new OkHttpClient();
        httpClient.setSslSocketFactory(getPinnedCertSslSocketFactory());

        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(API_URL)
                .setClient(new OkClient(httpClient))
                .setLogLevel(RestAdapter.LogLevel.FULL);

        restAdapter = builder.build();
    }

    /***
     * Creates and returns a SSLSocketFactory which will trust our selfsigned certificates in
     * fablab_dev_truststore. The truststore only contains the public certs.
     *
     * @return a SSLSocketFactory trusting our selfsigned certs.
     */
    protected SSLSocketFactory getPinnedCertSslSocketFactory() {
        try {
            //Default type is BKS on android
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            //our truststore containing the public certs
            InputStream inputStream = mContext.getResources().openRawResource(R.raw.fablab_dev_truststore);

            //the password used here is just a dummy as it is needed by the keystore.load method
            String trustStorePass = mContext.getString(R.string.development_truststore_pass);
            keyStore.load(inputStream, trustStorePass.toCharArray());

            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                    TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            sslContext.init(null, trustManagerFactory.getTrustManagers(), null);

            return sslContext.getSocketFactory();
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
        }
        return null;
    }
}
