package de.fau.cs.mad.fablab.android.view.fragments.icals;

import android.app.Activity;
import android.app.Application;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.net.Uri;

import java.security.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.greenrobot.event.EventBus;

public class ICalDetailsDialogFragmentViewModel {
    public static final String KEY_TITLE = "key_title";
    public static final String KEY_DATE = "key_date";
    public static final String KEY_TIME = "key_time";
    public static final String KEY_LOCATION = "key_location";
    public static final String KEY_DESCRIPTION = "key_description";


    Listener mListener;

    private String mTitle;
    private String mDate;
    private String mTime;
    private String mLocation;
    private String mDescription;

    private Command<Void> dismissDialogCommand = new Command<Void>() {
        @Override
        public void execute(Void parameter) {
            if (mListener != null) {
                mListener.onDismiss();
            }
        }
    };

    private Command<Intent> addToCalendarCommand = new Command<Intent>()
    {
        @Override
        public void execute(Intent parameter)
        {
            String[] date = mDate.split("\\.");
            String[] time = mTime.split(" - ");
            String[] startTime = null;
            String[] stopTime = null;
            if(time.length == 2)
            {
                startTime = time[0].split(":");
                stopTime = time[1].split(":");
            }

            Calendar beginTime = Calendar.getInstance();
            if(date.length == 3 && startTime.length == 2 && stopTime.length==2)
            {
                beginTime.set(Integer.parseInt(date[2]), Integer.parseInt(date[1]), Integer.parseInt(date[0]), Integer.parseInt(startTime[0]), Integer.parseInt(startTime[1]));
            }
            else
                beginTime.set(2000, 1, 1, 12, 0);
            Calendar endTime = Calendar.getInstance();
            if(date.length == 3 && startTime.length == 2 && stopTime.length==2)
            {
                endTime.set(Integer.parseInt(date[2]), Integer.parseInt(date[1]), Integer.parseInt(date[0]), Integer.parseInt(stopTime[0]), Integer.parseInt(stopTime[1]));
            }
            else
                endTime.set(2000, 1, 1, 12, 0);

            Intent intent = new Intent(Intent.ACTION_INSERT)
                    .setData(CalendarContract.Events.CONTENT_URI)
                    .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                    .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                    .putExtra(CalendarContract.Events.TITLE, mTitle)
                    .putExtra(CalendarContract.Events.EVENT_LOCATION, mLocation)
                    .putExtra(CalendarContract.Events.DESCRIPTION, mDescription);

            if(mTime == "ganzt\u00E4gig")
                intent.putExtra(CalendarContract.Events.ALL_DAY, true);

            mListener.startCalendar(intent);
        }
    };

    @Inject
    public ICalDetailsDialogFragmentViewModel() {

    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public Command<Void> getDismissDialogCommand()
    {
        return dismissDialogCommand;
    }

    public Command<Intent> getAddToCalendarCommand() {  return addToCalendarCommand; }

    public String getTitle() {
        return mTitle;
    }

    public String getDate()
    {
        return mDate;
    }

    public String getTime() {
        return mTime;
    }

    public String getLocation() {
        return mLocation;
    }

    public String getDescription() {
        return mDescription;
    }

    public void initialize(Bundle args) {
        mTitle = args.getString(KEY_TITLE);
        mDate = args.getString(KEY_DATE);
        mTime = args.getString(KEY_TIME);
        mLocation = args.getString(KEY_LOCATION);
        mDescription = args.getString(KEY_DESCRIPTION);
    }

    public interface Listener {
        void onDismiss();
        void startCalendar(Intent intent);
    }


}
