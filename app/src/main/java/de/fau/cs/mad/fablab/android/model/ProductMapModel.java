package de.fau.cs.mad.fablab.android.model;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.model.events.ProductMapStatusChangedEvent;
import de.greenrobot.event.EventBus;

public class ProductMapModel {
    private static final String TAG = "ProductMapModel";

    private boolean mFileExists;
    private final String mProductMapUrl;

    private final Context mContext;
    private final OkHttpClient mHttpClient;

    public ProductMapModel(Context context, OkHttpClient httpClient) {
        String path = context.getFilesDir().getAbsolutePath() + "/productMap.html";
        mFileExists = new File(path).exists();
        mProductMapUrl = "file://" + path;

        mContext = context;
        mHttpClient = httpClient;

        loadProductMap();
    }

    private void loadProductMap() {
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... params) {
                Request request = new Request.Builder().url(mContext.getString(R.string.api_url)
                        + "/productMap/productMap.html").build();
                try {
                    Response response = mHttpClient.newCall(request).execute();

                    FileOutputStream fos = mContext.openFileOutput("productMap.html",
                            Context.MODE_PRIVATE);
                    fos.write(response.body().string().getBytes());
                    fos.close();
                } catch (IOException e) {
                    Log.e(TAG, "Failed to fetch map", e);
                    return false;
                }

                return true;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                if (result) {
                    mFileExists = true;
                    EventBus.getDefault().post(new ProductMapStatusChangedEvent(true));
                } else if (!mFileExists) {
                    EventBus.getDefault().post(new ProductMapStatusChangedEvent(false));
                }
            }
        }.execute();
    }

    public String getProductMapUrl() {
        if (!mFileExists) {
            loadProductMap();
            return null;
        }
        return mProductMapUrl;
    }
}
