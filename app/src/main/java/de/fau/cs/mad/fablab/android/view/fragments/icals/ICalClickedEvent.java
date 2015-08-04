package de.fau.cs.mad.fablab.android.view.fragments.icals;

public class ICalClickedEvent {
    private final String mTitle;
    private final String mLocation;
    private final String mDescription;

    private final int[] mDate;
    private final int[] mStartTime;
    private final int[] mEndTime;

    private final boolean mIsAllday;


    public ICalClickedEvent(String title, int[] date, int[] startTime, int[] endTime, String location,
                            String description, boolean isAllday) {
        mTitle = title;
        mDate = date;
        mStartTime = startTime;
        mEndTime = endTime;
        mLocation = location;
        mDescription = description;
        mIsAllday = isAllday;
    }

    public String getTitle() { return mTitle; }

    public int[] getDate() {
        return mDate;
    }

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
