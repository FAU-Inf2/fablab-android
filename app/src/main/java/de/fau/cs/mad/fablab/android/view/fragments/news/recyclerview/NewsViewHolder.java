package de.fau.cs.mad.fablab.android.view.fragments.news.recyclerview;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.view.common.recyclerview.BaseViewHolder;
import de.fau.cs.mad.fablab.rest.core.News;

public class NewsViewHolder extends BaseViewHolder<NewsViewHolderViewModel> {

    View view;

    @InjectView(R.id.title_news_entry)
    TextView titleView;
    @InjectView(R.id.text_news_entry)
    TextView subTitleView;
    @InjectView(R.id.icon_news_entry)
    ImageView iconView;

    NewsViewHolderViewModel viewModel;

    public NewsViewHolder(View view) {
        super(view);
        ButterKnife.inject(this, view);
        this.view = view;
    }

    @Override
    public void setViewModel(NewsViewHolderViewModel viewModel) {
        this.viewModel = viewModel;
        bindViewToCommand(view, viewModel.getShowDialogCommand());

        titleView.setText(viewModel.getTitle());
        subTitleView.setText(viewModel.getDescriptionShort());

        if(viewModel.getPreviewImageLink() != null) {
            Picasso.with(iconView.getContext()).load(viewModel.getPreviewImageLink()).into(iconView);
        }
    }
}