package de.fau.cs.mad.fablab.android.view.fragments.news.recyclerview;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.view.fragments.news.NewsDetailsDialogViewModel;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.fau.cs.mad.fablab.android.viewmodel.common.BaseViewModel;
import de.fau.cs.mad.fablab.rest.core.News;

public class NewsViewHolderViewModel extends BaseViewModel<News>{

    News news;
    @Inject
    NewsDetailsDialogViewModel detailsDialogViewModel;

    private Command<Void> showDialogCommand = new Command<Void>() {
        @Override
        public void execute(Void parameter) {
            detailsDialogViewModel.show();
        }
    };

    public void setData(News news){
        this.news = news;
        this.detailsDialogViewModel.setData(news.getTitle(), news.getDescription(), news.getLinkToPreviewImage());
    }

    public Command<Void> getShowDialogCommand(){
        return showDialogCommand;
    }

    public News getNews() {
        return news;
    }

    public String getTitle() {
        return news.getTitle();
    }

    public String getDescriptionShort() {
        return news.getDescriptionShort();
    }

    public String getPreviewImageLink(){
        return news.getLinkToPreviewImage();
    }

    public interface NewsViewHolderListener extends Listener{

    }
}
