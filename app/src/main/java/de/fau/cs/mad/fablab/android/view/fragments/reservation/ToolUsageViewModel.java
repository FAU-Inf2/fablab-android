package de.fau.cs.mad.fablab.android.view.fragments.reservation;

import de.fau.cs.mad.fablab.rest.core.FabTool;
import de.fau.cs.mad.fablab.rest.core.ToolUsage;

public class ToolUsageViewModel {
    private ToolUsage mToolUsage;

    public ToolUsageViewModel(ToolUsage toolUsage) {
        mToolUsage = toolUsage;
    }

    public long getDuration() {
        return mToolUsage.getDuration();
    }

    public String getUser() {
        return mToolUsage.getUser();
    }

    public FabTool getTool() {
        return mToolUsage.getTool();
    }

    public long getCreationTime() {
        return mToolUsage.getCreationTime();
    }
}