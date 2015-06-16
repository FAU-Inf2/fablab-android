package de.fau.cs.mad.fablab.android.view.fragments.news.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.view.MainActivity;
import de.fau.cs.mad.fablab.android.view.fragments.news.NewsFragmentViewModel;
import de.fau.cs.mad.fablab.android.view.common.recyclerview.BaseAdapter;
import de.fau.cs.mad.fablab.rest.core.News;

public class NewsAdapter extends BaseAdapter<News, NewsViewHolder, NewsViewHolderViewModel> {
    private MainActivity activity;

    public NewsAdapter(NewsFragmentViewModel viewModel, MainActivity activity)
    {
        super(viewModel);
        this.activity = activity;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_entry, parent, false);

        NewsViewHolder newsViewHolder = new NewsViewHolder(view);
        activity.inject(newsViewHolder);

        return newsViewHolder;
    }
}