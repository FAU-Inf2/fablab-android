package de.fau.cs.mad.fablab.android.view.fragments.news;

public class NewsClickedEvent {
    private final String mTitle;
    private final String mText;
    private final String mImageLink;

    public NewsClickedEvent(String title, String text, String imageLink) {
        mTitle = title;
        mText = text;
        mImageLink = imageLink;
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
}
