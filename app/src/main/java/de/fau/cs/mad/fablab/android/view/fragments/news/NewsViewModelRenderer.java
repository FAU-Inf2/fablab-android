package de.fau.cs.mad.fablab.android.view.fragments.news;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pedrogomez.renderers.Renderer;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.view.common.binding.ViewCommandBinding;

public class NewsViewModelRenderer extends Renderer<NewsViewModel> {
    @InjectView(R.id.title_news_entry)
    TextView title_tv;
    @InjectView(R.id.text_news_entry)
    TextView text_tv;
    @InjectView(R.id.icon_news_entry)
    ImageView icon_tv;

    @Override
    protected void setUpView(View view) {
        ButterKnife.inject(this, view);
    }

    @Override
    protected void hookListeners(View view) {

    }

    @Override
    protected View inflate(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        return layoutInflater.inflate(R.layout.news_entry, viewGroup, false);
    }

    @Override
    public void render() {
        NewsViewModel viewModel = getContent();

        new ViewCommandBinding().bind(getRootView(), viewModel.getShowDialogCommand());

        title_tv.setText(viewModel.getTitle());
        text_tv.setText(viewModel.getDescriptionShort());

        if (viewModel.getLinkToPreviewImage() != null) {
            Picasso.with(icon_tv.getContext()).load(viewModel.getLinkToPreviewImage()).into(
                    icon_tv);
        }
    }
}
