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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.greenrobot.event.EventBus;


public class ICalDetailsDialogFragmentViewModel {
    public static final String KEY_TITLE = "key_title";
    public static final String KEY_DATE = "key_date";
    public static final String KEY_START_TIME = "key_start_time";
    public static final String KEY_END_TIME = "key_end_time";
    public static final String KEY_LOCATION = "key_location";
    public static final String KEY_DESCRIPTION = "key_description";
    public static final String KEY_ALLDAY = "key_allday";
//    public static final String KEY_DESCRIPTION = "key_description";


    Listener mListener;

    private String mTitle;
    private int[] mDate;
    private int[] mStartTime;
    private int[] mEndTime;
    private String mLocation;
    private String mDescription;
    private boolean mIsAllday;


    /**
     * Commands for the two dialog buttons
     */
    private Command<Void> dismissDialogCommand = new Command<Void>() {
        @Override
        public void execute(Void parameter) {
            if (mListener != null) {
                mListener.onDismiss();
            }
        }
    };

    private Command<Void> addToCalendarCommand = new Command<Void>()
    {
        @Override
        public void execute(Void parameter)
        {
            EventBus.getDefault().post(new AddToCalendarEvent(mTitle, mDate, mStartTime, mEndTime,
                    mLocation, mDescription, mIsAllday));
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

    public Command<Void> getAddToCalendarCommand(){ return addToCalendarCommand; }


    public String getTitle() {
        return mTitle;
    }

    public String getDate()
    {
        //month+1, because Calendar is zero-based (eg january = 0 and not 1)
        Calendar currentCal = Calendar.getInstance();

        // check if ical date is current date
        if (mDate[0] == currentCal.get(Calendar.DAY_OF_MONTH) &&
                mDate[1] == currentCal.get(Calendar.MONTH) &&
                mDate[2] == currentCal.get(Calendar.YEAR))
            return "Heute";
        else
            return Integer.toString(mDate[0]) + "." + Integer.toString((mDate[1]) + 1) + "." + Integer.toString(mDate[2]);
    }

    public String getTime() {

        if (mIsAllday)
            return "ganzt\\u00E4gig";
        else
            return Integer.toString(mStartTime[0]) + ":" + Integer.toString((mStartTime[1]) + 100).substring(1) + " - "
                    + Integer.toString(mEndTime[0]) + ":" + Integer.toString((mEndTime[1]) + 100).substring(1);
        // Minute + 100 to display HH::mm instead of H:m

    }


    public String getLocation() {
        return mLocation;
    }

    public String getDescription() {
        return mDescription;
    }

    public void initialize(Bundle args) {
        mTitle = args.getString(KEY_TITLE);
        mDate = args.getIntArray(KEY_DATE);
        mStartTime = args.getIntArray(KEY_START_TIME);
        mEndTime = args.getIntArray(KEY_END_TIME);
        mLocation = args.getString(KEY_LOCATION);
        mDescription = args.getString(KEY_DESCRIPTION);
        mIsAllday = args.getBoolean(KEY_ALLDAY);
    }

    public interface Listener {
        void onDismiss();
    }


}
