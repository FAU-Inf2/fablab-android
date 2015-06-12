package de.fau.cs.mad.fablab.android.view.fragments.news.list;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.view.fragments.news.NewsDetailsDialogViewModel;
import de.fau.cs.mad.fablab.android.viewmodel.common.Command;
import de.fau.cs.mad.fablab.android.viewmodel.BaseViewModel;
import de.fau.cs.mad.fablab.rest.core.News;

public class NewsViewHolderViewModel extends BaseViewModel{

    News news;
    NewsDetailsDialogViewModel detailsDialogViewModel;

    private Command showDialogCommand = new Command() {
        @Override
        public void execute(Object parameter) {
            detailsDialogViewModel.show();
        }
    };

    @Inject
    public NewsViewHolderViewModel(NewsDetailsDialogViewModel detailsDialogViewModel){
        this.detailsDialogViewModel = detailsDialogViewModel;
    }

    public void setNews(News news){
        this.news = news;
        this.detailsDialogViewModel.setNews(news);
    }

    public Command getShowDialogCommand(){
        return showDialogCommand;
    }

    public News getNews() {
        return news;
    }

    public String getTitle() {
        return news.getTitle();
    }

    public String getDescription() {
        return news.getDescription();
    }

    public interface NewsViewHolderListener extends Listener{

    }
}
