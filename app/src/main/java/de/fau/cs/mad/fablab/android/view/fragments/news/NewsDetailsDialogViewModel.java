package de.fau.cs.mad.fablab.android.view.fragments.news;

import android.os.Bundle;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;

public class NewsDetailsDialogViewModel {
    public static final String KEY_TITLE = "title";
    public static final String KEY_TEXT = "text";
    public static final String KEY_IMAGE_LINK = "image_link";

    private String mTitle;
    private String mText;
    private String mImageLink;

    private Listener mListener;

    private final Command<Void> mImageClickCommand = new Command<Void>() {
        @Override
        public void execute(Void parameter) {
            if (mListener != null && mImageLink != null) {
                mListener.onImageClicked();
            }
        }
    };

    @Inject
    public NewsDetailsDialogViewModel() {

    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public Command<Void> getImageClickCommand() {
        return mImageClickCommand;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getText() {
        return mText;
    }

    public String getImageLink() {
        return mImageLink;
    }

    public void initialize(Bundle arguments) {
        mTitle = arguments.getString(KEY_TITLE);
        mText = arguments.getString(KEY_TEXT);
        mImageLink = arguments.getString(KEY_IMAGE_LINK);
    }

    public interface Listener {
        void onImageClicked();
    }
}
