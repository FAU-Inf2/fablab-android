package de.fau.cs.mad.fablab.android.model;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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
    private long mTimeStampLastUpdate;
    private Date mDateLastDisplayedNews;
    private boolean mEndReached;

    private Callback<List<News>> mNewsApiCallback = new Callback<List<News>>() {
        @Override
        public void success(List<News> news, Response response) {
            mNews.addAll(news);
            mNewsRequested = false;

            for(News n : news)
            {
                createIfNotExists(mNewsDao, n);
            }

            mDateLastDisplayedNews = mNews.get(mNews.size()-1).getPubDate();

        }

        @Override
        public void failure(RetrofitError error) {
            mNewsRequested = false;
            if(error.getMessage().equals("404 Not Found"))
            {
                mEndReached = true;
            }
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
            mDateLastDisplayedNews = new Date(System.currentTimeMillis());
            mEndReached = false;
            fetchNextNews();
            mTimeStampLastUpdate = System.currentTimeMillis();
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
        mTimeStampLastUpdate = 0;
        mDateLastDisplayedNews = new Date(System.currentTimeMillis());
        mEndReached = false;
        fetchNextNews();
    }

    public void fetchNextNews() {
        //check whether to get news from database or server
        if (!mNewsRequested && mNews.size() + ELEMENT_COUNT > mNewsDao.countOf() && !mEndReached)
        {
            mNewsRequested = true;
            mNewsApi.find(mNews.size(), ELEMENT_COUNT, mNewsApiCallback);
        }
        else if (!mNewsRequested && !mEndReached)
        {
            mNewsRequested = true;
            List<News> fetchedNews = new ArrayList<>();
            //get next Element_count elements from database
            QueryBuilder<News, Long> queryBuilder = mNewsDao.queryBuilder();
            //sort elements in descending order according to pubdate and only return
            //ELEMENT_COUNT news
            queryBuilder.orderBy("pubDate", false).limit(ELEMENT_COUNT);
            try {
                queryBuilder.where().lt("pubDate", mDateLastDisplayedNews);
                fetchedNews = mNewsDao.query(queryBuilder.prepare());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            mNews.addAll(fetchedNews);

            mDateLastDisplayedNews = mNews.get(mNews.size()-1).getPubDate();
            mNewsRequested = false;
        }
    }

    public ObservableArrayList<News> getNews() {
        return mNews;
    }

    public void checkForUpdates()
    {
        mNewsApi.findNewsSince(mTimeStampLastUpdate, mNewsApiCallbackUpdate);
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