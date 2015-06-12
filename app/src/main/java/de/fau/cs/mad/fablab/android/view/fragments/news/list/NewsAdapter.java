package de.fau.cs.mad.fablab.android.view.fragments.news.list;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dagger.ObjectGraph;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.view.fragments.news.NewsFragmentViewModel;
import de.fau.cs.mad.fablab.android.view.list.BaseAdapter;
import de.fau.cs.mad.fablab.android.viewmodel.dependencyinjection.ViewModelModule;

public class NewsAdapter extends BaseAdapter<NewsViewHolder> {

    public NewsAdapter(NewsFragmentViewModel viewModel)
    {
        super(viewModel);
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_entry, parent, false);

        ObjectGraph objectGraph = ObjectGraph.create(new ViewModelModule((Activity) parent.getContext()));
        NewsViewHolderViewModel viewModel = objectGraph.get(NewsViewHolderViewModel.class);

        return new NewsViewHolder(view, viewModel);
    }
}