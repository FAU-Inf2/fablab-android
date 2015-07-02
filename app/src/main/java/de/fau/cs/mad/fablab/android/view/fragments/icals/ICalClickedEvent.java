package de.fau.cs.mad.fablab.android.view.fragments.icals;

public class ICalClickedEvent {
    private final String mTitle;
    private final String mDate;
    private final String mTime;
    private final String mLocation;
    private final String mDescription;

    public ICalClickedEvent(String title, String date, String time, String location,
                            String description) {
        mTitle = title;
        mDate = date;
        mTime = time;
        mLocation = location;
        mDescription = description;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDate() {
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
}
