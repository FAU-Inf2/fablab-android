package de.fau.cs.mad.fablab.android.view.fragments.icals;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import de.fau.cs.mad.fablab.android.model.ICalModel;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.fau.cs.mad.fablab.rest.core.ICal;
import de.greenrobot.event.EventBus;

/***
 * Is the ViewModel of a single Ical
 */

public class ICalViewModel {
    private ICal mICal;
    private ICalModel model;

    private Command<Void> mShowDialogCommand = new Command<Void>() {
        @Override
        public void execute(Void parameter) {
            EventBus.getDefault().post(new ICalClickedEvent(getTitle(), getCalendarDate(), getCalendarStartTime(), getCalendarEndTime(),
                    getLocation(), mICal.getDescription(), mICal.isAllday()));
        }
    };



    public ICalViewModel(ICal iCal) {
        mICal = iCal;
    }

    public Command<Void> getShowDialogCommand() {
        return mShowDialogCommand;
    }


    public String getTitle() {
        return mICal.getSummery();
    }

    public String getDate()
    {
        //month+1, because Calendar is zero-based (eg january = 0 and not 1)
        Calendar cal = Calendar.getInstance();
        Calendar currentCal = Calendar.getInstance();
        cal.setTime(mICal.getDtstartAsDate());

        // check if ical date is current date
        if (cal.get(Calendar.DAY_OF_YEAR) == currentCal.get(Calendar.DAY_OF_YEAR) && cal.get(Calendar.YEAR) == currentCal.get(Calendar.YEAR))
            return "Heute";
        else
            return Integer.toString(cal.get(Calendar.DAY_OF_MONTH)) + "." + Integer.toString(cal.get(Calendar.MONTH) + 1) + "." + Integer.toString(cal.get(Calendar.YEAR));
    }

    public int[] getCalendarDate()
    {
        //Calendar is zero-based (eg january = 0 and not 1)
        // don't increase month + 1 because number will be put in Calendar object again
        Calendar cal = Calendar.getInstance();
        cal.setTime(mICal.getDtstartAsDate());

        int[] date = {0, 0, 0};
        date[0] = cal.get(Calendar.DAY_OF_MONTH);
        date[1] = cal.get(Calendar.MONTH) ;
        date[2] = cal.get(Calendar.YEAR);

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


    public String getTime() {
        //add the difference to utc time to correct calendar time
        TimeZone timeZone = TimeZone.getDefault();
        Date now = new Date();
        int offsetFromUTC = timeZone.getOffset(now.getTime());

        Calendar calStart = Calendar.getInstance();
        calStart.setTime(mICal.getDtstartAsDate());
        calStart.add(Calendar.MILLISECOND, offsetFromUTC);

        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(mICal.getEndAsDate());
        calEnd.add(Calendar.MILLISECOND, offsetFromUTC);

        // TODO Move to server if 00:00 and 23:xx to trigger also allday
        if (mICal.isAllday() || (calStart.get(Calendar.HOUR_OF_DAY) == 0 && calStart.get(Calendar.MINUTE) == 0 && calEnd.get(Calendar.HOUR_OF_DAY) == 23)) {
            return "ganzt\u00E4gig";
        } else {

            // Minute + 100 to display HH:mm instead of HH:m
            return Integer.toString(calStart.get(Calendar.HOUR_OF_DAY)) + ":" + Integer.toString(calStart.get(Calendar.MINUTE) + 100).substring(1)
                    + " - " + Integer.toString(calEnd.get(Calendar.HOUR_OF_DAY)) + ":" + Integer.toString(calEnd.get(Calendar.MINUTE) + 100).substring(1);
        }
    }

    public String getLocation() {
        return mICal.getLocation();
    }




}
