package de.fau.cs.mad.fablab.android.view.fragments.icalandnews;

import de.fau.cs.mad.fablab.android.view.common.binding.RecyclerViewDeltaCommandBinding;

public class NewsListScrollingEvent {
    RecyclerViewDeltaCommandBinding.RecyclerViewDelta mDelta;

    public NewsListScrollingEvent(RecyclerViewDeltaCommandBinding.RecyclerViewDelta delta){
        this.mDelta = delta;
    }

    public RecyclerViewDeltaCommandBinding.RecyclerViewDelta getDelta(){
        return mDelta;
    }
}