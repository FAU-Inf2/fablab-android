package de.fau.cs.mad.fablab.android.view.fragments.icals;

public class AddToCalendarEvent
{
    private final int[] date;
    private final int[] startTime;
    private final int[] endTime;
    private final String title;
    private final String location;
    private final String description;
    private final boolean isAllday;

    public AddToCalendarEvent(int[] date, int[] startTime, int[] endTime, String title, String location, String description, boolean isAllday)
    {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.title = title;
        this.location = location;
        this.description = description;
        this.isAllday = isAllday;
    }

    public int[] getDate(){return date; }

    public int[] getStartTime() { return startTime; }

    public int[] getEndTime() { return endTime; }

    public String getTitle() { return title; }

    public String getLocation() { return location; }

    public String getDescription() { return description; }

    public boolean isAllday() { return isAllday; }

}
