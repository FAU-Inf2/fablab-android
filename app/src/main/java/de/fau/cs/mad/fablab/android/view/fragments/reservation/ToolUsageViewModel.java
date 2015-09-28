package de.fau.cs.mad.fablab.android.view.fragments.reservation;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.fau.cs.mad.fablab.rest.core.FabTool;
import de.fau.cs.mad.fablab.rest.core.ToolUsage;

public class ToolUsageViewModel {
    private ToolUsage mToolUsage;
    private boolean mOwnToolUsage;

    public ToolUsageViewModel(ToolUsage toolUsage, boolean ownToolUsages) {
        mToolUsage = toolUsage;
        mOwnToolUsage = ownToolUsages;
    }

    public String getDuration() {
        Date date = new Date(getStartTime() + (mToolUsage.getDuration() * 1000 * 60));
        Format format = new SimpleDateFormat("HH:mm");
        return mToolUsage.getDuration() + "m (" + format.format(date) + ")";
    }

    public boolean isNow() {
        Date dateNow = new Date();
        Date dateStart = new Date(getStartTime());
        Date dateEnd = new Date(getStartTime() + (mToolUsage.getDuration() * 1000 * 60));
        return (dateStart.before(dateNow) && dateEnd.after(dateNow));
    }

    public String getUser() {
        return mToolUsage.getUser();
    }

    public String getProject() {
        return mToolUsage.getProject();
    }

    public FabTool getTool() {
        return mToolUsage.getTool();
    }

    public long getStartTime() {
        return mToolUsage.getStartTime();
    }

    public ToolUsage getToolUsageEntry() {
        return mToolUsage;
    }

    public boolean isOwnId() {
        return mOwnToolUsage;
    }
}