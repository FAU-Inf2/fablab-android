package de.fau.cs.mad.fablab.android.model;

import android.content.Context;
import android.widget.Toast;

import java.util.List;

import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.model.events.NewsUpdateEvent;
import de.fau.cs.mad.fablab.rest.NewsApiClient;
import de.fau.cs.mad.fablab.rest.core.News;
import de.fau.cs.mad.fablab.rest.myapi.NewsApi;
import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/***
 * Handles the connection to the rest server to receive news.
 * Stores the results in NewsStorage and fires events on success
 */
public class NewsModel {

    private Context context;
    private NewsApi newsApi;
    private NewsStorage storage;

    private static final int ELEMENT_COUNT = 10;

    public NewsModel(Context c){
        this.context = c;
        this.newsApi = new NewsApiClient(context).get();
        this.storage = StorageFragment.getNewsStorage();
        newsApi.find(storage.getCurrentOffset(), ELEMENT_COUNT, newsApiCallback);
    }

    public void fetchNextNews(){
        newsApi.find(storage.getCurrentOffset(), ELEMENT_COUNT, newsApiCallback);
    }

    protected Callback<List<News>> newsApiCallback = new Callback<List<News>>() {
        @Override
        public void success(List<News> newses, Response response) {
            for(News n : newses){
                storage.addNews(n);
                EventBus.getDefault().post(new NewsUpdateEvent());
            }
            storage.setCurrentOffset(storage.getCurrentOffset()+newses.size());
        }

        @Override
        public void failure(RetrofitError error) {
            Toast.makeText(context, R.string.retrofit_callback_failure, Toast.LENGTH_LONG).show();
        }
    };


}
