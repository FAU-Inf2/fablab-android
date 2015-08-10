package de.fau.cs.mad.fablab.android.view.fragments.icals;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import de.fau.cs.mad.fablab.android.model.ICalModel;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.fau.cs.mad.fablab.rest.core.ICal;
import de.greenrobot.event.EventBus;
import de.fau.cs.mad.fablab.android.util.CalendarHelper;

/***
 * Is the ViewModel of a single Ical
 */

public class ICalViewModel
{
    private ICal mICal;
    private ICalModel model;

    private Command<Void> mShowDialogCommand = new Command<Void>()
    {
        @Override
        public void execute(Void parameter)
        {
            EventBus.getDefault().post(new ICalClickedEvent(getTitle(), getCalendarStartDate(), getCalendarEndDate(), getCalendarStartTime(), getCalendarEndTime(),
                    getLocation(), mICal.getDescription(), mICal.isAllday()));
        }
    };


    public ICalViewModel(ICal iCal)
    {
        mICal = iCal;
    }

    public Command<Void> getShowDialogCommand()
    {
        return mShowDialogCommand;
    }


    public String getTitle()
    {
        return mICal.getSummery();
    }

    public String getDate()
    {
        Calendar calStart = Calendar.getInstance();
        calStart.setTime(mICal.getDtstartAsDate());

        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(mICal.getEndAsDate());

        String result;

        // event goes longer than a day
        if (CalendarHelper.isSameDay(calStart, calEnd))
            result = CalendarHelper.buildDateString(calStart);
        else
            result = CalendarHelper.buildDateString(calStart) +
                    " - " +
                    CalendarHelper.buildDateString(calEnd);

        return result;
    }

    public int[] getCalendarStartDate()
    {
        //Calendar is zero-based (eg january = 0 and not 1)
        // don't increase month + 1 because number will be put in Calendar object again
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(mICal.getDtstartAsDate());

        int[] date = {0, 0, 0};
        date[0] = startCal.get(Calendar.DAY_OF_MONTH);
        date[1] = startCal.get(Calendar.MONTH);
        date[2] = startCal.get(Calendar.YEAR);
        return date;

    }

    public int[] getCalendarEndDate()
    {
        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(mICal.getEndAsDate());

        int[] date = {0, 0, 0};
        date[0] = calEnd.get(Calendar.DAY_OF_MONTH);
        date[1] = calEnd.get(Calendar.MONTH);
        date[2] = calEnd.get(Calendar.YEAR);

        return date;
    }

    public int[] getCalendarStartTime()
    {
        //add the difference to utc time to correct calendar time
        TimeZone timeZone = TimeZone.getDefault();
        Date now = new Date();
        int offsetFromUTC = timeZone.getOffset(now.getTime());

        int[] startTime = {0, 0};
        if (mICal.isAllday())
            return startTime;
        else
        {
            Calendar calStart = Calendar.getInstance();
            calStart.setTime(mICal.getDtstartAsDate());
            calStart.add(Calendar.MILLISECOND, offsetFromUTC);

            startTime[0] = calStart.get(Calendar.HOUR_OF_DAY);
            startTime[1] = calStart.get(Calendar.MINUTE);

            return startTime;
        }
    }

    public int[] getCalendarEndTime()
    {
        //add the difference to utc time to correct calendar time
        TimeZone timeZone = TimeZone.getDefault();
        Date now = new Date();
        int offsetFromUTC = timeZone.getOffset(now.getTime());

        int[] endTime = {23, 59};
        if (mICal.isAllday())
            return endTime;
        else
        {
            Calendar calEnd = Calendar.getInstance();
            calEnd.setTime(mICal.getEndAsDate());
            calEnd.add(Calendar.MILLISECOND, offsetFromUTC);

            endTime[0] = calEnd.get(Calendar.HOUR_OF_DAY);
            endTime[1] = calEnd.get(Calendar.MINUTE);

            return endTime;
        }
    }


    public String getTime()
    {
        //add the difference to utc time to correct calendar time
        TimeZone timeZone = TimeZone.getDefault();
        Date now = new Date();
        int offsetFromUTC = timeZone.getOffset(now.getTime());

        if (mICal.isAllday())
            return "ganzt\u00E4gig";
        else
        {
            Calendar calStart = Calendar.getInstance();
            calStart.setTime(mICal.getDtstartAsDate());
            calStart.add(Calendar.MILLISECOND, offsetFromUTC);

            Calendar calEnd = Calendar.getInstance();
            calEnd.setTime(mICal.getEndAsDate());
            calEnd.add(Calendar.MILLISECOND, offsetFromUTC);

            if(CalendarHelper.isSameTime(calStart, calEnd))
                return "ab " + CalendarHelper.buildTimeString(calStart);
            return CalendarHelper.buildTimeString(calStart) +
                    " - " +
                    CalendarHelper.buildTimeString(calEnd);
        }
    }

    public String getLocation()
    {
        return mICal.getLocation();
    }


}
