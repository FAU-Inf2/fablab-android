package de.fau.cs.mad.fablab.android.view.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.rest.core.News;

public class NewsAdapter extends BaseAdapter<News, NewsViewHolder>{

    public NewsAdapter()
    {
        super();
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_entry, parent, false);
        return new NewsViewHolder(view);
    }
}