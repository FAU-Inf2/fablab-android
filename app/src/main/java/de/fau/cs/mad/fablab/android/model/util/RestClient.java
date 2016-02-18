package de.fau.cs.mad.fablab.android.model.util;

import android.content.Context;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.OkHttpClient;

import java.text.SimpleDateFormat;

import de.fau.cs.mad.fablab.android.BuildConfig;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.rest.core.Format;
import de.fau.cs.mad.fablab.rest.myapi.CartApi;
import de.fau.cs.mad.fablab.rest.myapi.CategoryApi;
import de.fau.cs.mad.fablab.rest.myapi.DataApi;
import de.fau.cs.mad.fablab.rest.myapi.DrupalApi;
import de.fau.cs.mad.fablab.rest.myapi.ICalApi;
import de.fau.cs.mad.fablab.rest.myapi.InventoryApi;
import de.fau.cs.mad.fablab.rest.myapi.NewsApi;
import de.fau.cs.mad.fablab.rest.myapi.ProductApi;
import de.fau.cs.mad.fablab.rest.myapi.ProjectsApi;
import de.fau.cs.mad.fablab.rest.myapi.PushApi;
import de.fau.cs.mad.fablab.rest.myapi.SpaceApi;
import de.fau.cs.mad.fablab.rest.myapi.ToolUsageApi;
import de.fau.cs.mad.fablab.rest.myapi.VersionCheckApi;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.JacksonConverter;

public class RestClient {
    private static final String LOG_TAG = "RestClient";

    private OkHttpClient mHttpClient;
    private RestAdapter mRestAdapter;
    private RestAdapter.Builder mRestAdapterBuilder;

    public RestClient(Context context, boolean string) {

        final String API_URL = context.getString(R.string.api_url);

        mHttpClient = new OkHttpClient();

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.setDateFormat(new SimpleDateFormat(Format.DATE_FORMAT));

        if (!string) {
            mRestAdapterBuilder = new RestAdapter.Builder()
                    .setEndpoint(API_URL)
                    .setClient(new OkClient(mHttpClient))
                    .setConverter(new JacksonConverter(mapper))
                    .setLogLevel(BuildConfig.DEBUG ? RestAdapter.LogLevel.FULL
                            : RestAdapter.LogLevel.NONE);

            mRestAdapter = mRestAdapterBuilder.build();
        } else {
            mRestAdapterBuilder = new RestAdapter.Builder()
                    .setEndpoint(API_URL)
                    .setClient(new OkClient(mHttpClient))
                    .setConverter(new JsonInputStringOutputConverter(mapper))
                    .setLogLevel(BuildConfig.DEBUG ? RestAdapter.LogLevel.FULL
                            : RestAdapter.LogLevel.NONE);

            mRestAdapter = mRestAdapterBuilder.build();
        }
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

    public DataApi getDataApi() {
        return mRestAdapter.create(DataApi.class);
    }

    public DrupalApi getDrupalApi() {
        return mRestAdapter.create(DrupalApi.class);
    }

    public InventoryApi getInventoryApi() {
        return mRestAdapter.create(InventoryApi.class);
    }

    public VersionCheckApi getVersionCheckApi() {
        return mRestAdapter.create(VersionCheckApi.class);
    }

    public RestAdapter.Builder getRestAdapterBuilder() {
        return mRestAdapterBuilder;
    }

    public CategoryApi getCategoryApi()
    {
        return mRestAdapter.create(CategoryApi.class);
    }

    public ToolUsageApi getToolUsageApi() {
        return mRestAdapter.create(ToolUsageApi.class);
    }

    public ProjectsApi getProjectsApi()
    {
        return mRestAdapter.create(ProjectsApi.class);
    }

    public OkHttpClient getHttpClient() {
        return mHttpClient;
    }
}
