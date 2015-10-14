package de.fau.cs.mad.fablab.android.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationPublisher extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        boolean remove = intent.getBooleanExtra("remove", false);
        int id = intent.getIntExtra("notification_id", 7890);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if(!remove) {
            Notification notification = intent.getParcelableExtra("notification");
            notification.defaults |= Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;
            notificationManager.notify(id, notification);
        } else {
            notificationManager.cancel(id);
        }
    }
}
