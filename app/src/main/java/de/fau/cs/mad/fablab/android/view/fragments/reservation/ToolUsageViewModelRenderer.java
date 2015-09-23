package de.fau.cs.mad.fablab.android.view.fragments.reservation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pedrogomez.renderers.Renderer;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.fau.cs.mad.fablab.android.R;

public class ToolUsageViewModelRenderer extends Renderer<ToolUsageViewModel> {
    @Bind(R.id.user_toolusage_entry)
    TextView user_tv;

    @Override
    protected void setUpView(View view) {
        ButterKnife.bind(this, view);
    }

    @Override
    protected void hookListeners(View view) {

    }

    @Override
    protected View inflate(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        return layoutInflater.inflate(R.layout.toolusage_entry, viewGroup, false);
    }

    @Override
    public void render() {
        ToolUsageViewModel viewModel = getContent();

        user_tv.setText(viewModel.getUser());
        /*title_tv.setText(viewModel.getTitle());
        text_tv.setText(viewModel.getDescriptionShort());

        if (viewModel.getLinkToPreviewImage() != null) {
            Picasso.with(icon_iv.getContext()).load(viewModel.getLinkToPreviewImage()).fit()
                    .centerCrop().into(icon_iv);
        } else {
            Picasso.with(icon_iv.getContext()).load(R.drawable.news_nopicture).fit().into(icon_iv);
        }*/
    }
}
