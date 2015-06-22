package de.fau.cs.mad.fablab.android.view.fragments.news;

import android.os.Bundle;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.viewmodel.common.BaseViewModel;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;

public class NewsDetailsDialogViewModel {
    public static final String KEY_TITLE = "title";
    public static final String KEY_TEXT = "text";
    public static final String KEY_IMAGE_LINK = "image_link";
    private static final String KEY_IMAGE_ZOOM = "image_zoom";

    Listener mListener;

    private String mTitle;
    private String mText;
    private String mImageLink;

    private boolean mImageZoom = false;
    private Command<Void> mImageClickCommand = new Command<Void>() {
        @Override
        public void execute(Void parameter) {
            if (mListener != null) {
                setImageZoom(!isImageZoom());
                mListener.onImageLayoutChanged();
            }
        }
    };
    private Command<Void> mDismissCommand = new Command<Void>() {
        @Override
        public void execute(Void parameter) {
            if (mListener != null) {
                mListener.onDismiss();
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

    public Command<Void> getDismissCommand() {
        return mDismissCommand;
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

    public boolean isImageZoom() {
        return mImageZoom;
    }

    public void setImageZoom(boolean imageZoom) {
        mImageZoom = imageZoom;
        if (mListener != null) {
            mListener.onImageLayoutChanged();
        }
    }

    public void restoreState(Bundle arguments, Bundle savedInstanceState) {
        mTitle = arguments.getString(KEY_TITLE);
        mText = arguments.getString(KEY_TEXT);
        mImageLink = arguments.getString(KEY_IMAGE_LINK);

        if (savedInstanceState != null && savedInstanceState.getBoolean(KEY_IMAGE_ZOOM)) {
            setImageZoom(true);
        }
    }


    public void saveState(Bundle outState) {
        outState.putBoolean(KEY_IMAGE_ZOOM, mImageZoom);
    }

    public interface Listener extends BaseViewModel.Listener {
        void onImageLayoutChanged();

        void onDismiss();
    }
}
