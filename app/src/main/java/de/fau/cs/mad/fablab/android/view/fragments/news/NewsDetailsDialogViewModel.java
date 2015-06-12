package de.fau.cs.mad.fablab.android.view.fragments.news;


import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.viewmodel.common.BaseViewModel;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;

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

    public void setData(String title, String content, String imageLink) {
        this.title = title;
        this.text = content;
        this.imageLink = imageLink;
    }

    public Command getImageClickCommand() {
        return imageClickCommand;
    }

    public Command getDismissCommand() {
        return dismissCommand;
    }

    @Override
    public void setData(Object data) {

    }

    public interface Listener extends BaseViewModel.Listener{
        void onImageLayoutChanged();
    }
}
