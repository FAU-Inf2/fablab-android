package de.fau.cs.mad.fablab.android.model;

import java.util.ArrayList;
import java.util.List;

import de.fau.cs.mad.fablab.rest.core.News;

/***
 * This class acts as a storage for our fetched news
 */
public class NewsStorage {

    private ArrayList<News> news;
    private int currentOffset = 0;

    public NewsStorage(){
        news = new ArrayList<>();
    }

    public int getCurrentOffset(){
        return currentOffset;
    }

    public void setCurrentOffset(int newOffset){
        this.currentOffset = newOffset;
    }

    public News getNews(int position){
        return news.get(position);
    }

    public void addNews(News news){
        this.news.add(news);
    }

    public List<News> getAllNews(){
        return news;
    }
}
