package de.fau.cs.mad.fablab.android.view.fragments.icalandnews;

import de.fau.cs.mad.fablab.android.view.common.binding.RecyclerViewDeltaCommandBinding;

public class NewsListScrollingEvent {
    RecyclerViewDeltaCommandBinding.RecyclerViewDelta mDelta;
    int maxPixelHeight;

    public NewsListScrollingEvent(RecyclerViewDeltaCommandBinding.RecyclerViewDelta delta, int maxPixelHeight) {
        this.mDelta = delta;
        this.maxPixelHeight = maxPixelHeight;
    }

    public RecyclerViewDeltaCommandBinding.RecyclerViewDelta getDelta() {
        return mDelta;
    }

    public int getMaxPixelHeight() {
        return maxPixelHeight;
    }
}
