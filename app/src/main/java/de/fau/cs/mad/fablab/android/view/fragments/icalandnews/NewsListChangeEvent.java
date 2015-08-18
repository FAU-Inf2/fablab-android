package de.fau.cs.mad.fablab.android.view.fragments.icalandnews;

public class NewsListChangeEvent {
    int size;

    public NewsListChangeEvent(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }
}
