package de.fau.cs.mad.fablab.android.model;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;

import java.io.InputStream;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import de.fau.cs.mad.fablab.android.BuildConfig;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.rest.core.Format;
import de.fau.cs.mad.fablab.rest.myapi.CartApi;
import de.fau.cs.mad.fablab.rest.myapi.ICalApi;
import de.fau.cs.mad.fablab.rest.myapi.NewsApi;
import de.fau.cs.mad.fablab.rest.myapi.ProductApi;
import de.fau.cs.mad.fablab.rest.myapi.PushApi;
import de.fau.cs.mad.fablab.rest.myapi.SpaceApi;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

public class RestClient {
    private static final String LOG_TAG = "RestClient";

    private RestAdapter mRestAdapter;
    private Context mContext;

    public RestClient(Context context) {
        mContext = context;
        final String API_URL = mContext.getString(R.string.api_url);

        OkHttpClient httpClient = new OkHttpClient();
        httpClient.setSslSocketFactory(getPinnedCertSslSocketFactory());

        Gson gson = new GsonBuilder()
                .setDateFormat(Format.DATE_FORMAT)
                .create();

        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(API_URL)
                .setClient(new OkClient(httpClient))
                .setConverter(new GsonConverter(gson))
                .setLogLevel(BuildConfig.DEBUG ? RestAdapter.LogLevel.FULL
                        : RestAdapter.LogLevel.NONE);

        mRestAdapter = builder.build();
    }

    /**
     * Creates and returns a SSLSocketFactory which will trust our selfsigned certificates in
     * fablab_dev_truststore. The truststore only contains the public certs.
     *
     * @return a SSLSocketFactory trusting our selfsigned certs.
     */
    private SSLSocketFactory getPinnedCertSslSocketFactory() {
        try {
            //Default type is BKS on android
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            //our truststore containing the public certs
            InputStream inputStream = mContext.getResources().openRawResource(
                    R.raw.fablab_dev_truststore);

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

    public CartApi getCartApi() {
        return mRestAdapter.create(CartApi.class);
    }

    public ICalApi getICalApi() {
        return mRestAdapter.create(ICalApi.class);
    }

    public NewsApi getNewsApi() {
        return mRestAdapter.create(NewsApi.class);
    }

    public ProductApi getProductApi() {
        return mRestAdapter.create(ProductApi.class);
    }

    public PushApi getPushApi() {
        return mRestAdapter.create(PushApi.class);
    }

    public SpaceApi getSpaceApi() {
        return mRestAdapter.create(SpaceApi.class);
    }
}
