package de.fau.cs.mad.fablab.android.viewmodel;

import de.fau.cs.mad.fablab.android.model.NewsModel;
import de.fau.cs.mad.fablab.android.viewmodel.common.ObservableArrayList;
import de.fau.cs.mad.fablab.rest.core.News;

public class NewsFragmentViewModel{

    private NewsModel model;

    private Command commandGetNews = new Command() {
        @Override
        public void execute(Object parameter) {
            model.fetchNextNews();
        }
    };

    public NewsFragmentViewModel(){
        this.model = new NewsModel();
    }

    public Command getNewsCommand(){
        return commandGetNews;
    }

    public ObservableArrayList<News> getNewsList(){
        return model.getNewsList();
    }
}
