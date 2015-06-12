package de.fau.cs.mad.fablab.android.view.fragments.news;


import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.viewmodel.BaseViewModel;
import de.fau.cs.mad.fablab.android.viewmodel.common.Command;
import de.fau.cs.mad.fablab.rest.core.News;

public class NewsDetailsDialogViewModel extends BaseViewModel {

    Listener listener;

    private String imageLink;
    private String title;
    private String text;

    private boolean imageZoom = false;

    private NewsViewLauncher viewLauncher;

    private Command imageClickCommand = new Command() {
        @Override
        public void execute(Object parameter) {
            if(listener != null){
                setImageZoom(!isImageZoom());
                listener.onImageLayoutChanged();
            }
        }
    };

    private Command dismissCommand = new Command() {
        @Override
        public void execute(Object parameter) {
            viewLauncher.dismissNewsDialogFragmend();
        }
    };

    @Inject
    public NewsDetailsDialogViewModel(NewsViewLauncher viewLauncher){
        this.viewLauncher = viewLauncher;
    }

    public void setListener(Listener listener){
        this.listener = listener;
    }

    public void show(){
        this.viewLauncher.showNewsDialogFragment(this, getTitle(), getText(), getImageLink());
    }

    public String getImageLink() {
        return imageLink;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public boolean isImageZoom() {
        return imageZoom;
    }

    public void setImageZoom(boolean imageZoom) {
        this.imageZoom = imageZoom;
    }

    public void setNews(News news) {
        this.title = news.getTitle();
        this.text = news.getDescription();
        this.imageLink = news.getLinkToPreviewImage();
    }

    public Command getImageClickCommand() {
        return imageClickCommand;
    }

    public Command getDismissCommand() {
        return dismissCommand;
    }

    public interface Listener extends BaseViewModel.Listener{
        void onImageLayoutChanged();
    }
}
