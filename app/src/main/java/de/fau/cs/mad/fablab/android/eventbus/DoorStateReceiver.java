package de.fau.cs.mad.fablab.android.eventbus;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import de.greenrobot.event.EventBus;

public class DoorStateReceiver extends WakefulBroadcastReceiver {
    private EventBus eventBus = EventBus.getDefault();
    @Override
    public void onReceive(Context context, Intent intent) {
        // DoorEvent event = new DoorEvent("", null, false);
    }
}
