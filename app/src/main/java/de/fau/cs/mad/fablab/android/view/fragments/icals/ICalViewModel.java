package de.fau.cs.mad.fablab.android.view.fragments.icals;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;

import java.util.Calendar;

import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.fau.cs.mad.fablab.rest.core.ICal;
import de.greenrobot.event.EventBus;

public class ICalViewModel {
    private ICal mICal;

    private Command<Void> mShowDialogCommand = new Command<Void>() {
        @Override
        public void execute(Void parameter) {
            EventBus.getDefault().post(new ICalClickedEvent(getTitle(), getDate(), getTime(),
                    getLocation(), mICal.getDescription()));
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

    public String getTime() {
        if (mICal.isAllday()) {
            return "ganzt\u00E4gig";
        } else {
            Calendar calStart = Calendar.getInstance();
            calStart.setTime(mICal.getDtstartAsDate());
            Calendar calEnd = Calendar.getInstance();
            calEnd.setTime(mICal.getEndAsDate());
            return Integer.toString(calStart.get(Calendar.HOUR_OF_DAY)) + ":" + Integer.toString(calStart.get(Calendar.MINUTE) + 100).substring(1)
                    + " - " + Integer.toString(calEnd.get(Calendar.HOUR_OF_DAY)) + ":" + Integer.toString(calEnd.get(Calendar.MINUTE) + 100).substring(1);

        }
    }


    public String getLocation() {
        return mICal.getLocation();
    }




}
