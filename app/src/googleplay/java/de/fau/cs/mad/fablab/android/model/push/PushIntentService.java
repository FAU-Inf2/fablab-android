package de.fau.cs.mad.fablab.android.model.push;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

import de.fau.cs.mad.fablab.android.model.events.CartStatusPushedEvent;
import de.fau.cs.mad.fablab.android.model.events.SpaceApiStatePushedEvent;
import de.fau.cs.mad.fablab.rest.core.CartStatus;
import de.fau.cs.mad.fablab.rest.core.DoorState;
import de.greenrobot.event.EventBus;

public class PushIntentService extends IntentService {

    public PushIntentService() {
        super("PushIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);

        // has effect of unparcelling Bundle
        if (!extras.isEmpty() && GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
            String doorStateString = extras.getString("DoorState");
            if (doorStateString != null) {
                ObjectMapper mapper = new ObjectMapper();
                try {
                    DoorState doorState = mapper.readValue(doorStateString, DoorState.class);
                    EventBus.getDefault().post(new SpaceApiStatePushedEvent(doorState));
                } catch (IOException e) {
                    Log.e("PushIntentService", "mapping failed", e);
                }
                return;
            }
            String cartStatusString = extras.getString("CartStatus");
            if (cartStatusString != null) {
                ObjectMapper mapper = new ObjectMapper();
                try {
                    CartStatus cartStatus = mapper.readValue(cartStatusString, CartStatus.class);
                    EventBus.getDefault().post(new CartStatusPushedEvent(cartStatus));
                } catch (IOException e) {
                    Log.e("PushIntentService", "mapping failed", e);
                }
            }
        }

        // Release the wake lock provided by the WakefulBroadcastReceiver.
        PushBroadcastReceiver.completeWakefulIntent(intent);
    }
}