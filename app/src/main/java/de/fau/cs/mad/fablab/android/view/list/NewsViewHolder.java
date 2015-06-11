package de.fau.cs.mad.fablab.android.view.list;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.model.StorageFragment;
import de.fau.cs.mad.fablab.android.ui.NewsDialog;
import de.fau.cs.mad.fablab.rest.core.News;

public class NewsViewHolder extends BaseViewHolder<News> {

    static final String IMAGE = "IMAGE";
    static final String TITLE = "TITLE";
    static final String TEXT = "TEXT";

    private final View view;
    private final TextView titleView, subTitleView;
    private final ImageView iconView;

    private String description;
    private String title;
    private Bitmap image;

    public NewsViewHolder(View view) {
        super(view);
        this.view = view;
        this.titleView = (TextView) view.findViewById(R.id.title_news_entry);
        this.subTitleView = (TextView) view.findViewById(R.id.text_news_entry);
        this.iconView = (ImageView) view.findViewById(R.id.icon_news_entry);
    }

    @Override
    void setData(News data) {
        this.titleView.setText(data.getTitle());
        this.subTitleView.setText(data.getDescriptionShort());
        if(data.getLinkToPreviewImage() != null) {
            Picasso.with(StorageFragment.getContext()).load(data.getLinkToPreviewImage()).into(iconView);
        }
        title = data.getTitle();
        description = data.getDescription();

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                image = ((BitmapDrawable) iconView.getDrawable()).getBitmap();
                args.putString(TITLE, title);
                args.putString(TEXT, description);
                args.putParcelable(IMAGE, image);
                NewsDialog dialog = new NewsDialog();
                dialog.setArguments(args);
                dialog.show(StorageFragment.getActivityFragmentManager(), "News Entry");
            }
        });
    }
}