package de.fau.cs.mad.fablab.android.view.fragments.news.list;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.model.StorageFragment;
import de.fau.cs.mad.fablab.android.view.fragments.news.list.NewsViewHolderViewModel;
import de.fau.cs.mad.fablab.android.view.list.BaseViewHolder;
import de.fau.cs.mad.fablab.rest.core.News;

public class NewsViewHolder extends BaseViewHolder<News> {

    @InjectView(R.id.title_news_entry)
    TextView titleView;
    @InjectView(R.id.text_news_entry)
    TextView subTitleView;
    @InjectView(R.id.icon_news_entry)
    ImageView iconView;

    NewsViewHolderViewModel viewModel;

    public NewsViewHolder(View view, NewsViewHolderViewModel viewModel) {
        super(view);
        ButterKnife.inject(this, view);

        this.viewModel = viewModel;
        bindViewToCommand(view, viewModel.getShowDialogCommand());
    }

    @Override
    public void setData(News data) {
        viewModel.setNews(data);

        titleView.setText(data.getTitle());
        subTitleView.setText(data.getDescriptionShort());

        if(data.getLinkToPreviewImage() != null) {
            Picasso.with(iconView.getContext()).load(data.getLinkToPreviewImage()).into(iconView);
        }
    }
}