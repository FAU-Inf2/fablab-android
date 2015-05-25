package de.fau.cs.mad.fablab.android.eventbus;

import java.util.Date;

public class DoorEvent {
    private String message;
    private Date time;
    private boolean state;

    public DoorEvent(String message, Date time, boolean state) {
        this.message = message;
        this.time = time;
        this.state = state;
    }


    public String getMessage() {
        return message;
    }

    public Date getTime() {
        return time;
    }

    public boolean isOpened() {
        return state;
    }
}
