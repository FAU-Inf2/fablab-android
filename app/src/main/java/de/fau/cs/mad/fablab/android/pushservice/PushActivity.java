package de.fau.cs.mad.fablab.android.pushservice;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.rest.PushApiClient;
import de.fau.cs.mad.fablab.rest.core.RegistrationId;
import de.fau.cs.mad.fablab.rest.myapi.PushApi;
import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class PushActivity extends ActionBarActivity {

    public static final String TAG = "pushservice";

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private String SENDER_ID = "605066933";

    private EditText mRegistrationIdEditText;
    private Context mContext;
    private GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();
    private String regid;


    public void onEvent(PushWarenkorpEvent event){
        Log.i(TAG,"PushWartenkorp");
    }

    public void onEvent(PushSpaceAPIEvent event){
        Log.i(TAG,"PushSpaceAPI");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push);
        Log.i(TAG,"Invoke PushActivity");
        EventBus.getDefault().register(this);

        mRegistrationIdEditText = (EditText) findViewById(R.id.pushactivity_edit_regid);
        mContext = getApplicationContext();

        // Check device for Play Services APK.

        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(this);
            regid = getRegistrationId(mContext);
            mRegistrationIdEditText.setText(regid);
            sendRegistrationIdToBackend(regid);
            if (regid.isEmpty()) {
                registerInBackground();
            }
        } else {
            Log.i(TAG, "No valid Google Play Services APK found.");
        }

    }

    private void registerInBackground() {
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(mContext);
                    }
                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;
                    Log.i(TAG,msg);
                    sendRegistrationIdToBackend(regid);
                    storeRegistrationId(mContext, regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                Log.i(TAG,"onPostExecute in AsyncTask");
                // Here was the msg as String
                mRegistrationIdEditText.append(o.toString() + "\n");
            }


        }.execute(null, null, null);
    }

    private void sendRegistrationIdToBackend(String aRegId) {

        Log.i(TAG,"SendRegistrationIdToBackend");

        PushApiClient client;
        client = new PushApiClient(getApplicationContext());

        PushApi api = client.get();

        api.addRegistrationId(new RegistrationId(aRegId), new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                Log.i(TAG,"Success " + response.getStatus() + " " + response.getHeaders());
            }

            @Override
            public void failure(RetrofitError error) {
                Log.i(TAG,"Success" + error.getMessage());
            }
        });


    }

    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }



    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    private SharedPreferences getGCMPreferences(Context aContext) {
        return aContext.getSharedPreferences(PushActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

}