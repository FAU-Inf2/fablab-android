package de.fau.cs.mad.fablab.android.view.fragments.news.recyclerview;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dagger.ObjectGraph;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.view.fragments.news.NewsFragmentViewModel;
import de.fau.cs.mad.fablab.android.view.common.recyclerview.BaseAdapter;
import de.fau.cs.mad.fablab.android.viewmodel.dependencyinjection.ViewModelModule;
import de.fau.cs.mad.fablab.rest.core.News;

public class NewsAdapter extends BaseAdapter<News, NewsViewHolder, NewsViewHolderViewModel> {

    public NewsAdapter(NewsFragmentViewModel viewModel)
    {
        super(viewModel);
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_entry, parent, false);

        ObjectGraph objectGraph = ObjectGraph.create(new ViewModelModule((Activity) parent.getContext()));
        viewHolderViewModel = objectGraph.get(NewsViewHolderViewModel.class);

        return new NewsViewHolder(view);
    }
}