package de.fau.cs.mad.fablab.android.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

import de.fau.cs.mad.fablab.rest.core.PlatformType;
import de.fau.cs.mad.fablab.rest.core.PushToken;
import de.fau.cs.mad.fablab.rest.myapi.PushApi;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class PushModel {
    private static final String TAG = "PushModel";
    private static final String KEY_PREF_ENABLE_PUSH = "enable_push";
    private static final String PROPERTY_REG_ID = "reg_id";
    private static final String PROPERTY_APP_VERSION = "app_version";
    private static final String SENDER_ID = "605066933";

    private Context mContext;
    private PushApi mPushApi;

    private String mPushId = "0";

    public PushModel(Context context, PushApi pushApi) {
        mContext = context;
        mPushApi = pushApi;

        if (getPreferences().getBoolean(KEY_PREF_ENABLE_PUSH, true)) {
            registerDeviceToGcm();
        }
    }

    public String getPushId() {
        return mPushId;
    }

    public void registerDeviceToGcm() {
        if (!checkPlayServices()) {
            return;
        }

        if (isRegisteredToGcm()) {
            mPushId = getPreferences().getString(PROPERTY_REG_ID, "");
        } else {
            registerInBackground();
        }
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(mContext);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                Log.i(TAG, "Service is not available");
            } else {
                Log.i(TAG, "This device is not supported");
            }
            return false;
        }
        return true;
    }

    private boolean isRegisteredToGcm() {
        SharedPreferences prefs = getPreferences();

        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return false;
        }

        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, -1);
        int currentVersion = getAppVersion();
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return false;
        }

        return true;
    }

    private SharedPreferences getPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    private int getAppVersion() {
        try {
            PackageInfo packageInfo = mContext.getPackageManager().getPackageInfo(
                    mContext.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    private void registerInBackground() {
        new AsyncTask<Context, Void, String>() {
            @Override
            protected String doInBackground(Context... params) {
                String regId = null;
                try {
                    GoogleCloudMessaging mGoogleCloudMessaging = GoogleCloudMessaging.getInstance(
                            params[0]);
                    regId = mGoogleCloudMessaging.register(SENDER_ID);
                    Log.i(TAG, "Device registered, registration ID=" + regId);
                } catch (IOException e) {
                    Log.i(TAG, "Error while registering", e);
                }
                return regId;
            }

            @Override
            protected void onPostExecute(String regId) {
                super.onPostExecute(regId);
                if (regId != null) {
                    sendRegistrationIdToBackend(regId);
                    storeRegistrationId(regId);
                    mPushId = regId;
                }
            }
        }.execute(mContext);
    }

    private void sendRegistrationIdToBackend(String regId) {
        PushToken pushToken = new PushToken();
        pushToken.setToken(regId);
        pushToken.setPlatformType(PlatformType.ANDROID);
        mPushApi.subscribeDoorOpensNextTime(pushToken, new Callback<Boolean>() {
            @Override
            public void success(Boolean status, Response response) {
                Log.i(TAG, "Success: " + status);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.i(TAG, "Failure: " + error.getMessage());
            }
        });
    }

    private void storeRegistrationId(String regId) {
        int appVersion = getAppVersion();

        Log.i(TAG, "Saving regId on app version " + appVersion);

        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.apply();
    }

    public void unregisterDeviceFromGcm() {
        if (!mPushId.equals("0")) {
            unregisterInBackground();
        }
    }

    private void unregisterInBackground() {
        new AsyncTask<Context, Void, Void>() {
            @Override
            protected Void doInBackground(Context... params) {
                try {
                    GoogleCloudMessaging mGoogleCloudMessaging = GoogleCloudMessaging.getInstance(
                            params[0]);
                    mGoogleCloudMessaging.unregister();
                    Log.i(TAG, "Device unregistered");
                } catch (IOException e) {
                    Log.i(TAG, "Error", e);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                deleteRegistrationIdFromBackend();
                deleteRegistrationId();
                mPushId = "0";
            }
        }.execute(mContext);
    }

    private void deleteRegistrationIdFromBackend() {
        PushToken pushToken = new PushToken();
        pushToken.setToken(mPushId);
        pushToken.setPlatformType(PlatformType.ANDROID);
        mPushApi.unsubscribeDoorOpensNextTime(pushToken, new Callback<Boolean>() {
            @Override
            public void success(Boolean status, Response response) {
                Log.i(TAG, "Success: " + status);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.i(TAG, "Failure: " + error.getMessage());
            }
        });
    }

    private void deleteRegistrationId() {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.remove(PROPERTY_REG_ID);
        editor.remove(PROPERTY_APP_VERSION);
        editor.apply();
    }
}
