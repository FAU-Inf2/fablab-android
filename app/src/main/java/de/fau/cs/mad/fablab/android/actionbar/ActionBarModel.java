package de.fau.cs.mad.fablab.android.actionbar;

public class ActionBarModel {
    private boolean opened;
    private ActionBarTime time;

    public void setData(boolean opened, ActionBarTime time) {
        this.opened = opened;
        this.time = time;
    }
}
