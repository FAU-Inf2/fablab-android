package de.fau.cs.mad.fablab.android.model;

import com.j256.ormlite.dao.RuntimeExceptionDao;

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
    private RuntimeExceptionDao<News, Long> mNewsDao;
    private long timeStampLastUpdate = 0;

    private Callback<List<News>> mNewsApiCallback = new Callback<List<News>>() {
        @Override
        public void success(List<News> news, Response response) {
            mNews.addAll(news);
            mNewsRequested = false;

            for(News n : news)
            {
                createIfNotExists(mNewsDao, n);
            }

        }

        @Override
        public void failure(RetrofitError error) {
            mNewsRequested = false;
        }
    };

    private Callback<List<News>> mNewsApiCallbackUpdate = new Callback<List<News>>() {
        @Override
        public void success(List<News> news, Response response) {
            mNewsRequested = false;

            for(News n : news)
            {
                createIfNotExists(mNewsDao, n);
            }

            mNews.clear();
            fetchNextNews();
            timeStampLastUpdate = System.currentTimeMillis();
        }

        @Override
        public void failure(RetrofitError error) {
            mNewsRequested = false;
        }
    };

    public NewsModel(NewsApi newsApi, RuntimeExceptionDao<News, Long> newsDao) {
        mNewsApi = newsApi;
        mNewsDao = newsDao;
        mNews = new ObservableArrayList<>();
        mNewsRequested = false;
        fetchNextNews();
    }

    public void fetchNextNews() {
        //check whether to get news from database or server
        if (!mNewsRequested)// && mNews.size() + ELEMENT_COUNT > mNewsDao.countOf())
        {
            mNewsRequested = true;
            mNewsApi.find(mNews.size(), ELEMENT_COUNT, mNewsApiCallback);
        }
        else
        {
            //get next Element_count elements from database
        }
    }

    public ObservableArrayList<News> getNews() {
        return mNews;
    }

    public void newsModelUpdate()
    {
        mNewsApi.findNewsSince(timeStampLastUpdate, mNewsApiCallbackUpdate);
    }

    private void createIfNotExists(RuntimeExceptionDao<News, Long> newsDao, News news)
    {
        List<News> retrievedNews = newsDao.queryForEq("title", news.getTitle());

        if(retrievedNews.size() == 0)
        {
            newsDao.create(news);
        }
    }
}