package de.fau.cs.mad.fablab.android.view.fragments.news;


import java.util.List;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.model.NewsModel;
import de.fau.cs.mad.fablab.android.viewmodel.common.BaseAdapterViewModel;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.fau.cs.mad.fablab.rest.core.News;

public class NewsFragmentViewModel extends BaseAdapterViewModel<News>{

    private NewsModel model;
    private Listener listener;

    private Command<Void> commandGetNews = new Command<Void>() {
        @Override
        public void execute(Void parameter) {
            model.fetchNextNews();
        }
    };

    public void setListener(Listener listener){
        super.setListener(listener);
        this.listener = listener;
    }

    @Inject
    public NewsFragmentViewModel(NewsModel model){
        this.model = model;
        model.getNewsList().setListener(this);
    }

    @Override
    public void setData(Object data) {

    }

    public Command<Void> getNewsCommand(){
        return commandGetNews;
    }

    @Override
    public List<News> getData() {
        return model.getNewsList();
    }

    public interface Listener extends BaseAdapterViewModel.Listener{

    }
}
