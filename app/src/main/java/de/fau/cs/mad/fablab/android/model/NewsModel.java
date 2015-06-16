package de.fau.cs.mad.fablab.android.model;

import android.app.Activity;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.viewmodel.common.ObservableArrayList;
import de.fau.cs.mad.fablab.rest.core.News;
import de.fau.cs.mad.fablab.rest.myapi.NewsApi;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/***
 * Handles the connection to the rest server to receive news.
 * Stores the results in NewsStorage and fires events on success
 */
public class NewsModel {

    private NewsApi newsApi;
    private NewsStorage storage;
    private Activity activity;
    private int currentOffset = 0;

    private static final int ELEMENT_COUNT = 10;

    @Inject
    public NewsModel(NewsApi newsApi, NewsStorage newsStorage, Activity activity){
        this.newsApi = newsApi;
        this.storage = newsStorage;
        this.activity = activity;
        fetchNextNews();
    }

    public void fetchNextNews(){
        newsApi.find(currentOffset, ELEMENT_COUNT, newsApiCallback);
    }

    protected Callback<List<News>> newsApiCallback = new Callback<List<News>>() {
        @Override
        public void success(List<News> newses, Response response) {
            for(News n : newses){
                storage.addNews(n);
            }
            currentOffset += newses.size();
        }

        @Override
        public void failure(RetrofitError error) {
            Toast.makeText(activity, R.string.retrofit_callback_failure, Toast.LENGTH_LONG).show();
        }
    };


    public ObservableArrayList<News> getNewsList() {
        return storage.getAllNews();
    }
}
