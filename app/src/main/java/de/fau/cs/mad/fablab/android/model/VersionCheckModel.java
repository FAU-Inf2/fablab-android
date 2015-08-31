package de.fau.cs.mad.fablab.android.model;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import de.fau.cs.mad.fablab.android.model.events.NoUpdateAvailableEvent;
import de.fau.cs.mad.fablab.android.model.events.UpdateAvailableEvent;
import de.fau.cs.mad.fablab.rest.core.PlatformType;
import de.fau.cs.mad.fablab.rest.core.UpdateStatus;
import de.fau.cs.mad.fablab.rest.myapi.VersionCheckApi;
import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class VersionCheckModel {
    private static final String TAG = "VersionCheckModel";

    private VersionCheckApi mVersionCheckApi;
    private int mVersionCode;
    private boolean mVersionCheckRequested;

    private final Callback<UpdateStatus> mCheckVersionCallback = new Callback<UpdateStatus>() {
        @Override
        public void success(UpdateStatus updateStatus, Response response) {
            EventBus eventBus = EventBus.getDefault();
            switch (updateStatus.getUpdateAvailable()) {
                case NotAvailable:
                    Log.i(TAG, "no update available");
                    eventBus.post(new NoUpdateAvailableEvent());
                    break;
                case Optional:
                    Log.i(TAG, "optional update to version " + updateStatus.getLatestVersion()
                            + " available");
                    eventBus.post(new UpdateAvailableEvent(false,
                            updateStatus.getUpdateMessage()));
                    break;
                case Required:
                    Log.i(TAG, "required update to version " + updateStatus.getLatestVersion()
                            + " available");
                    eventBus.post(new UpdateAvailableEvent(true,
                            updateStatus.getUpdateMessage()));
                    break;
            }
            mVersionCheckRequested = false;
        }

        @Override
        public void failure(RetrofitError error) {
            Log.w(TAG, "version check failed: " + error.getMessage());
            mVersionCheckRequested = false;
        }
    };

    public VersionCheckModel(VersionCheckApi versionCheckApi, Context context) {
        mVersionCheckApi = versionCheckApi;

        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            mVersionCode = pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }

        checkVersion();
    }

    public void checkVersion() {
        if (mVersionCheckRequested) {
            return;
        }
        mVersionCheckRequested = true;
        mVersionCheckApi.checkVersion(PlatformType.ANDROID, mVersionCode, mCheckVersionCallback);
    }
}
