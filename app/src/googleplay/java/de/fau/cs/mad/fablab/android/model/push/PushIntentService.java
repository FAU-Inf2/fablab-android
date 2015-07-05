package de.fau.cs.mad.fablab.android.model.push;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class PushIntentService extends IntentService {

    public PushIntentService() {
        super("PushIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            // TODO send events
        }

        // Release the wake lock provided by the WakefulBroadcastReceiver.
        PushBroadcastReceiver.completeWakefulIntent(intent);
    }
}