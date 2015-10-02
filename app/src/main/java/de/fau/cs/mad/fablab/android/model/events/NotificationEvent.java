package de.fau.cs.mad.fablab.android.model.events;

import java.util.Date;

public class NotificationEvent {
    private String mNotificationTitle;
    private String mNotificationText;
    private Date mShowTime;
    private int mId;
    private boolean mRemove;

    public NotificationEvent(String notificationTitle, String notificationText, Date showTime, int id) {
        mNotificationTitle = notificationTitle;
        mNotificationText = notificationText;
        mShowTime = showTime;
        mId = id;
        mRemove = false;
    }

    public void setRemove() {
        mRemove = true;
    }

    public String getNotificationTitle() {
        return mNotificationTitle;
    }

    public String getNotificationText() {
        return mNotificationText;
    }

    public int getId() {
        return mId;
    }

    public long getShowTime() {
        return mShowTime.getTime();
    }

    public boolean getRemove() {
        return mRemove;
    }
}
