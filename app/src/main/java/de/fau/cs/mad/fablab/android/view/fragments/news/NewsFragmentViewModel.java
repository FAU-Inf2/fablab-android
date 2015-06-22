package de.fau.cs.mad.fablab.android.view.fragments.news;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.model.NewsModel;
import de.fau.cs.mad.fablab.android.viewmodel.common.ObservableArrayList;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.fau.cs.mad.fablab.rest.core.News;

public class NewsFragmentViewModel implements ObservableArrayList.Listener<News> {
    private NewsModel mModel;
    private Listener mListener;

    private Command<Void> mCommandGetNews = new Command<Void>() {
        @Override
        public void execute(Void parameter) {
            mModel.fetchNextNews();
        }
    };

    @Inject
    public NewsFragmentViewModel(NewsModel model) {
        mModel = model;
        model.getNews().setListener(this);
    }

    public void setListener(Listener listener) {
        mListener = listener;

        List<NewsViewModel> viewModels = new ArrayList<>();
        for (News news : mModel.getNews()) {
            viewModels.add(new NewsViewModel(news));
        }
        mListener.onDataPrepared(viewModels);
    }

    public Command<Void> getGetNewsCommand() {
        return mCommandGetNews;
    }

    @Override
    public void onItemAdded(News newItem) {
        mListener.onItemAdded(new NewsViewModel(newItem));
    }

    @Override
    public void onItemRemoved(News removedItem) {

    }

    public interface Listener {
        void onItemAdded(NewsViewModel viewModel);

        void onDataPrepared(List<NewsViewModel> viewModels);
    }
}
