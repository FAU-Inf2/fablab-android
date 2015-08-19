package de.fau.cs.mad.fablab.android.model.util;

import android.content.Context;
import android.util.Log;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.OkHttpClient;

import java.io.InputStream;
import java.security.KeyStore;
import java.text.SimpleDateFormat;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import de.fau.cs.mad.fablab.android.BuildConfig;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.rest.core.Format;
import de.fau.cs.mad.fablab.rest.myapi.CartApi;
import de.fau.cs.mad.fablab.rest.myapi.DataApi;
import de.fau.cs.mad.fablab.rest.myapi.DrupalApi;
import de.fau.cs.mad.fablab.rest.myapi.ICalApi;
import de.fau.cs.mad.fablab.rest.myapi.NewsApi;
import de.fau.cs.mad.fablab.rest.myapi.ProductApi;
import de.fau.cs.mad.fablab.rest.myapi.PushApi;
import de.fau.cs.mad.fablab.rest.myapi.SpaceApi;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.JacksonConverter;

public class RestClient {
    private static final String LOG_TAG = "RestClient";

    private RestAdapter mRestAdapter;
    private Context mContext;
    private RestAdapter.Builder mRestAdapterBuilder;

    public RestClient(Context context, boolean string) {
        mContext = context;
        final String API_URL = mContext.getString(R.string.api_url);

        OkHttpClient httpClient = new OkHttpClient();
        httpClient.setSslSocketFactory(getPinnedCertSslSocketFactory());

        // extra converter for Strings needed
        if(!string) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            mapper.setDateFormat(new SimpleDateFormat(Format.DATE_FORMAT));

            mRestAdapterBuilder = new RestAdapter.Builder()
                    .setEndpoint(API_URL)
                    .setClient(new OkClient(httpClient))
                    .setConverter(new JacksonConverter(mapper))
                    .setLogLevel(BuildConfig.DEBUG ? RestAdapter.LogLevel.FULL
                            : RestAdapter.LogLevel.NONE);

            mRestAdapter = mRestAdapterBuilder.build();
        }
        else
        {
            mRestAdapterBuilder = new RestAdapter.Builder()
                    .setEndpoint(API_URL)
                    .setClient(new OkClient(httpClient))
                    .setConverter(new StringConverter())
                    .setLogLevel(BuildConfig.DEBUG ? RestAdapter.LogLevel.FULL
                            : RestAdapter.LogLevel.NONE);

            mRestAdapter = mRestAdapterBuilder.build();
        }
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

    public DataApi getDataApi(){
        return mRestAdapter.create(DataApi.class);
    }

    public DrupalApi getDrupalApi()
    {
        return mRestAdapter.create(DrupalApi.class);
    }

    public RestAdapter.Builder getRestAdapterBuilder()
    {
        return mRestAdapterBuilder;
    }
}
