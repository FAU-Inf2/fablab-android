package de.fau.cs.mad.fablab.android.view.fragments.icals;

public class AddToCalendarEvent
{
    private final String mTitle;
    private final String mLocation;
    private final String mDescription;

    private final int[] mStartDate;
    private final int[] mEndDate;
    private final int[] mStartTime;
    private final int[] mEndTime;

    private final boolean mIsAllday;


    public AddToCalendarEvent(String title, int[] startDate, int[] EndDate, int[] startTime, int[] endTime, String location,
                            String description, boolean isAllday) {
        mTitle = title;
        mStartDate = startDate;
        mEndDate = EndDate;
        mStartTime = startTime;
        mEndTime = endTime;
        mLocation = location;
        mDescription = description;
        mIsAllday = isAllday;
    }

    public String getTitle() { return mTitle; }

    public int[] getStartDate() {
        return mStartDate;
    }

    public int[] getEndDate() { return  mEndDate; }

    public int[] getStartTime() {
        return mStartTime;
    }

    public int[] getEndTime() {
        return mEndTime;
    }

    public String getLocation() {
        return mLocation;
    }

    public String getDescription() {
        return mDescription;
    }

    public boolean isAllday() {return mIsAllday; }

}
