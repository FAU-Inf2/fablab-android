package de.fau.cs.mad.fablab.android.view.fragments.news;

import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.fau.cs.mad.fablab.rest.core.News;
import de.greenrobot.event.EventBus;

public class NewsViewModel {
    private News mNews;

    private Command<Void> mShowDialogCommand = new Command<Void>() {
        @Override
        public void execute(Void parameter) {
            EventBus.getDefault().post(new NewsClickedEvent(mNews.getTitle(),
                    mNews.getDescription(), mNews.getLinkToPreviewImage()));
        }
    };

    public NewsViewModel(News news) {
        mNews = news;
    }

    public Command<Void> getShowDialogCommand() {
        return mShowDialogCommand;
    }

    public String getTitle() {
        return mNews.getTitle();
    }

    public String getDescriptionShort() {
        return mNews.getDescriptionShort();
    }

    public String getLinkToPreviewImage() {
        return mNews.getLinkToPreviewImage();
    }
}
