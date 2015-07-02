package de.fau.cs.mad.fablab.android.view.fragments.news;

import com.pedrogomez.renderers.AdapteeCollection;
import com.pedrogomez.renderers.ListAdapteeCollection;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.model.NewsModel;
import de.fau.cs.mad.fablab.android.viewmodel.common.ObservableArrayList;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.fau.cs.mad.fablab.rest.core.News;

public class NewsFragmentViewModel implements ObservableArrayList.Listener<News> {
    private NewsModel mModel;
    private Listener mListener;

    private ListAdapteeCollection<NewsViewModel> mNewsViewModelCollection;

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
        mNewsViewModelCollection = new ListAdapteeCollection<>();
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public Command<Void> getGetNewsCommand() {
        return mCommandGetNews;
    }

    @Override
    public void onItemAdded(News newItem) {
        mNewsViewModelCollection.add(new NewsViewModel(newItem));
        if (mListener != null) {
            mListener.onDataChanged();
        }
    }

    @Override
    public void onItemRemoved(News removedItem) {

    }

    public AdapteeCollection<NewsViewModel> getNewsViewModelCollection() {
        return mNewsViewModelCollection;
    }

    public void initialize() {
        if (mListener != null) {
            for (News news : mModel.getNews()) {
                mNewsViewModelCollection.add(new NewsViewModel(news));
            }
            mListener.onDataChanged();
        }
    }

    public interface Listener {
        void onDataChanged();
    }
}
