package de.fau.cs.mad.fablab.android.view.fragments.news;


import java.util.List;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.model.NewsModel;
import de.fau.cs.mad.fablab.android.viewmodel.BaseAdapterViewModel;
import de.fau.cs.mad.fablab.android.viewmodel.common.Command;
import de.fau.cs.mad.fablab.rest.core.News;

public class NewsFragmentViewModel extends BaseAdapterViewModel<News>{

    private NewsModel model;
    private Listener listener;

    private Command commandGetNews = new Command() {
        @Override
        public void execute(Object parameter) {
            model.fetchNextNews();
        }
    };

    public void setListener(Listener listener){
        super.setListener(listener);
        this.listener = listener;
    }

    @Inject
    public NewsFragmentViewModel(){
        this.model = new NewsModel();
        model.getNewsList().setListener(this);
    }

    public Command getNewsCommand(){
        return commandGetNews;
    }

    @Override
    public List<News> getData() {
        return model.getNewsList();
    }

    public interface Listener extends BaseAdapterViewModel.Listener{

    }
}
