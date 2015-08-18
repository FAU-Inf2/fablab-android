package de.fau.cs.mad.fablab.android.util;

import java.util.Calendar;

/**
 * helper class for
 * calendar comparing and
 * string building
 */
public class CalendarHelper
{
    public static boolean isSameDay(Calendar calendarDate1, Calendar calendarDate2)
    {
        if (calendarDate1.get(Calendar.YEAR) == calendarDate2.get(Calendar.YEAR) &&
                calendarDate1.get(Calendar.MONTH) == calendarDate2.get(Calendar.MONTH) &&
                calendarDate1.get(Calendar.DAY_OF_MONTH) == calendarDate2.get(Calendar.DAY_OF_MONTH))
            return true;
        else
            return false;
    }

    public static boolean isSameDay(int[] date1, int[] date2)
    {
        if (date1[0] == date2[0] &&
                date1[1] == date2[1] &&
                date1[2] == date2[2])
            return true;
        else
            return false;
    }

    public static boolean isSameTime(Calendar startTime, Calendar endTime)
    {
        if(startTime.get(Calendar.HOUR) == endTime.get(Calendar.HOUR) &&
                startTime.get(Calendar.MINUTE) == endTime.get(Calendar.MINUTE))
            return true;
        else
            return false;
    }

    public static boolean isCurrentDay(int[] date)
    {
        Calendar currentCal = Calendar.getInstance();
        if (date[0] == currentCal.get(Calendar.DAY_OF_MONTH) &&
                date[1] == currentCal.get(Calendar.MONTH) &&
                date[2] == currentCal.get(Calendar.YEAR))
            return true;
        else
            return false;
    }

    public static boolean isCurrentDay(Calendar calendarDate)
    {
        Calendar currentDate = Calendar.getInstance();
        return isSameDay(currentDate, calendarDate);
    }

    public static boolean isMoreDays(Calendar startTime, Calendar endTime)
    {
        return !isSameDay(startTime, endTime);
    }

    public static boolean isMoreDays(int[] date1, int[] date2)
    {
        if (date1[0] != date2[0] ||
                date1[1] != date2[1] ||
                date1[2] != date2[2])
            return true;
        else
            return false;
    }

    public static String buildDateString(Calendar calendarDate)
    {
        //month+1, because Calendar is zero-based (eg january = 0 and not 1)
        if (isCurrentDay(calendarDate))
            return "Heute";
        else
            return Integer.toString(calendarDate.get(Calendar.DAY_OF_MONTH)) + "." + (Integer.toString(calendarDate.get(Calendar.MONTH) + 1) + "." + Integer.toString(calendarDate.get(Calendar.YEAR)));
    }

    public static String buildDateString(int[] calendar)
    {
        //month+1, because Calendar is zero-based (eg january = 0 and not 1)
        if (isCurrentDay(calendar))
            return "Heute";
        else
            return Integer.toString(calendar[0]) + "." + Integer.toString((calendar[1]) + 1) + "." + Integer.toString(calendar[2]);
    }

    public static String buildTimeString(int[] time)
    {
        // Minute + 100 to display HH::mm instead of H:m
        return Integer.toString(time[0]) + ":" + Integer.toString((time[1]) + 100).substring(1);
    }

    public static String buildTimeString(Calendar calendarTime)
    {
        // Minute + 100 to display HH::mm instead of H:m
        return Integer.toString(calendarTime.get(Calendar.HOUR_OF_DAY)) + ":" + Integer.toString(calendarTime.get(Calendar.MINUTE) + 100).substring(1);
    }
}
