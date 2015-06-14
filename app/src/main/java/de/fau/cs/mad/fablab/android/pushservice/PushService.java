package de.fau.cs.mad.fablab.android.pushservice;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

import de.fau.cs.mad.fablab.rest.PushApiClient;
import de.fau.cs.mad.fablab.rest.core.RegistrationId;
import de.fau.cs.mad.fablab.rest.myapi.PushApi;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class PushService {


    private static final String PROPERTY_REG_ID = PushService.class.toString();
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final String SENDER_ID = "12124638422";
    private static final String TAG = "pushservice";


    private GoogleCloudMessaging mGoogleCloudMessaging;
    private Context mContext;

    public PushService(Context aContext){
        mContext = aContext;
    }

    public void registerDeviceToGCM() throws PushException{
        RegistrationId registrationId = new RegistrationId();
        if(!checkPlayServices()){
            throw new PushException("Service is not available");
        }
        if(alreadyRegistToGCM()){
            registrationId.setRegistrationid(getRegistrationIdAsString());
        }
        else{
            registerInBackground();
        }
    }

    private void registerInBackground() {
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                String msg;
                String regid;
                try {
                    if (mGoogleCloudMessaging == null) {
                        mGoogleCloudMessaging = GoogleCloudMessaging.getInstance(mContext);
                    }
                    regid = mGoogleCloudMessaging.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;
                    Log.i(TAG,msg);
                    sendRegistrationIdToBackend(regid);
                    storeRegistrationId(mContext, regid);
                } catch (IOException ex) {
                    Log.i(TAG,"Error :" + ex.getMessage());
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                Log.i(TAG,"Device registered");
            }
        }.execute(null, null, null);
    }

    private void sendRegistrationIdToBackend(String aRegId) {
        PushApiClient client;
        client = new PushApiClient(mContext);
        PushApi api = client.get();
        api.addRegistrationId(new RegistrationId(aRegId), new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                Log.i(TAG,"Success: " + response.getStatus());
            }
            @Override
            public void failure(RetrofitError error) {
                Log.i(TAG,"Failure: " + error.getMessage());
            }
        });
    }

    private RegistrationId getRegistrationId() throws PushException{
        if(alreadyRegistToGCM()){
            RegistrationId registrationId = new RegistrationId();
            registrationId.setRegistrationid(getRegistrationIdAsString());
            return registrationId;
        }
        throw new PushException("Not registered");
    }

    private String getRegistrationIdAsString(){
        final SharedPreferences prefs = getGCMPreferences(mContext);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(mContext);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    public boolean alreadyRegistToGCM(){
        if(getRegistrationIdAsString().isEmpty()){
            return false;
        }
        return true;
    }


    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(mContext);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                Log.i(TAG, "Service is not available");
            } else {
                Log.i(TAG, "This device is not supported.");
            }
            return false;
        }
        return true;
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

    private SharedPreferences getGCMPreferences(Context aContext) {
        return aContext.getSharedPreferences(PushActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
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

}