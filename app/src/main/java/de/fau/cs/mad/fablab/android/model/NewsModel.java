package de.fau.cs.mad.fablab.android.model;

import java.util.List;

import de.fau.cs.mad.fablab.android.viewmodel.common.ObservableArrayList;
import de.fau.cs.mad.fablab.rest.core.News;
import de.fau.cs.mad.fablab.rest.myapi.NewsApi;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Handles the connection to the rest server to receive news.
 * Stores the results and fires events on success.
 */
public class NewsModel {
    private static final int ELEMENT_COUNT = 10;

    private ObservableArrayList<News> mNews;
    private NewsApi mNewsApi;
    private boolean mNewsRequested;

    private Callback<List<News>> mNewsApiCallback = new Callback<List<News>>() {
        @Override
        public void success(List<News> news, Response response) {
            mNews.addAll(news);
            mNewsRequested = false;
        }

        @Override
        public void failure(RetrofitError error) {
            mNewsRequested = false;
        }
    };

    public NewsModel(NewsApi newsApi) {
        mNewsApi = newsApi;
        mNews = new ObservableArrayList<>();
        mNewsRequested = false;
        fetchNextNews();
    }

    public void fetchNextNews() {
        if (!mNewsRequested) {
            mNewsRequested = true;
            mNewsApi.find(mNews.size(), ELEMENT_COUNT, mNewsApiCallback);
        }
    }

    public ObservableArrayList<News> getNews() {
        return mNews;
    }
}
