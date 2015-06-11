package de.fau.cs.mad.fablab.android.model;

import de.fau.cs.mad.fablab.android.viewmodel.common.ObservableArrayList;
import de.fau.cs.mad.fablab.rest.core.News;

/***
 * This class acts as a storage for our fetched news
 * //TODO persistence!
 */
public class NewsStorage {

    private ObservableArrayList<News> news;

    public NewsStorage(){
        news = new ObservableArrayList<>();
    }

    public News getNews(int position){
        return news.get(position);
    }

    public void addNews(News news){
        this.news.add(news);
    }

    public ObservableArrayList<News> getAllNews(){
        return news;
    }
}
