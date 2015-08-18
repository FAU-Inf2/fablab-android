package de.fau.cs.mad.fablab.android.view.fragments.icals;

import android.os.Bundle;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.util.CalendarHelper;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.greenrobot.event.EventBus;


public class ICalDetailsDialogFragmentViewModel {
    public static final String KEY_TITLE = "key_title";
    public static final String KEY_START_DATE = "key_start_date";
    public static final String KEY_END_DATE = "key_end_date";
    public static final String KEY_START_TIME = "key_start_time";
    public static final String KEY_END_TIME = "key_end_time";
    public static final String KEY_LOCATION = "key_location";
    public static final String KEY_DESCRIPTION = "key_description";
    public static final String KEY_ALLDAY = "key_allday";
//    public static final String KEY_DESCRIPTION = "key_description";


    Listener mListener;

    private String mTitle;
    private int[] mStartDate;
    private int[] mEndDate;
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
            EventBus.getDefault().post(new AddToCalendarEvent(mTitle, mStartDate, mEndDate, mStartTime, mEndTime,
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
        String result = "";

        //check if event goes more than one day
        if(CalendarHelper.isSameDay(mStartDate, mEndDate))
            result = CalendarHelper.buildDateString(mStartDate);
        else
            result = CalendarHelper.buildDateString(mStartDate) +
                    " - " +
                    CalendarHelper.buildDateString(mEndDate);

        return  result;

    }

    public String getTime() {

        if (mIsAllday)
            return "ganzt\u00E4gig";
        else if(CalendarHelper.isMoreDays(mStartDate, mEndDate))
            return " - ";
        else if(CalendarHelper.isSameDay(mStartDate, mEndDate))
            return "ab " + CalendarHelper.buildTimeString(mStartTime);
        else
            return CalendarHelper.buildTimeString(mStartTime) +
                    " - " +
                    CalendarHelper.buildTimeString(mEndTime);

    }


    public String getLocation() {
        return mLocation;
    }

    public String getDescription() {
        return mDescription;
    }

    public void initialize(Bundle args) {
        mTitle = args.getString(KEY_TITLE);
        mStartDate = args.getIntArray(KEY_START_DATE);
        mEndDate = args.getIntArray(KEY_END_DATE);
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
