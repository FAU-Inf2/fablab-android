package de.fau.cs.mad.fablab.android.view.fragments.news;

import com.pedrogomez.renderers.AdapteeCollection;
import com.pedrogomez.renderers.ListAdapteeCollection;

import java.util.Collection;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.model.NewsModel;
import de.fau.cs.mad.fablab.android.model.events.NewsListScrollingEvent;
import de.fau.cs.mad.fablab.android.view.common.binding.RecyclerViewDeltaCommandBinding;
import de.fau.cs.mad.fablab.android.viewmodel.common.BaseViewModel;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.fau.cs.mad.fablab.rest.core.News;
import de.greenrobot.event.EventBus;

public class NewsFragmentViewModel extends BaseViewModel<News> {
    private NewsModel mModel;
    private Listener mListener;
    private EventBus mEventBus = EventBus.getDefault();

    private ListAdapteeCollection<NewsViewModel> mNewsViewModelCollection;

    private Command<Void> mCommandGetNews = new Command<Void>() {
        @Override
        public void execute(Void parameter) {
            mModel.fetchNextNews();
        }
    };
    private Command<RecyclerViewDeltaCommandBinding.RecyclerViewDelta> mNewsScrollingCommand = new Command<RecyclerViewDeltaCommandBinding.RecyclerViewDelta>() {
        @Override
        public void execute(RecyclerViewDeltaCommandBinding.RecyclerViewDelta parameter) {
            mEventBus.post(new NewsListScrollingEvent(parameter));
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

    public Command<RecyclerViewDeltaCommandBinding.RecyclerViewDelta> getNewsScrollingCommand() {
        return mNewsScrollingCommand;
    }

    @Override
    public void onItemAdded(News newItem) {
        mNewsViewModelCollection.add(new NewsViewModel(newItem));
        if (mListener != null) {
            mListener.onDataChanged();
        }
    }

    @Override
    public void onAllItemsAdded(Collection<? extends News> collection) {
        for (News newItem : collection) {
            mNewsViewModelCollection.add(new NewsViewModel(newItem));
            if (mListener != null) {
                mListener.onDataChanged();
            }
        }
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
