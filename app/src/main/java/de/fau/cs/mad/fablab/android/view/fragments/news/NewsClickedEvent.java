package de.fau.cs.mad.fablab.android.view.fragments.news;

import de.fau.cs.mad.fablab.rest.core.News;

public class NewsClickedEvent {
    private final News mNews;

    public NewsClickedEvent(News news) {
        mNews = news;
    }

    public News getNews()
    {
        return mNews;
    }
}
