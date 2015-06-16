package de.fau.cs.mad.fablab.android.view.fragments.news;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;

import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.view.common.binding.ViewCommandBinding;
import de.fau.cs.mad.fablab.android.view.common.fragments.BaseDialogFragment;

public class NewsDetailsDialogFragment extends BaseDialogFragment implements NewsDetailsDialogViewModel.Listener {

    @InjectView(R.id.title_news_dialog)
    TextView tv_title;
    @InjectView(R.id.news_text_news_dialog)
    TextView tv_content;
    @InjectView(R.id.image_news_dialog)
    ImageView iv_image;
    @InjectView(R.id.ok_button_news_dialog)
    Button dimissButton;

    NewsDetailsDialogViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setViewModel(NewsDetailsDialogViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.setListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_news_dialog, container, false);
        ButterKnife.inject(this, v);

        new ViewCommandBinding().bind(iv_image, viewModel.getImageClickCommand());
        new ViewCommandBinding().bind(dimissButton, viewModel.getDismissCommand());

        tv_content.setLinksClickable(true);
        tv_content.setMovementMethod(LinkMovementMethod.getInstance());
        tv_content.setText(Html.fromHtml(viewModel.getText()));
        tv_title.setText(viewModel.getTitle());

        Picasso.with(iv_image.getContext()).load(viewModel.getImageLink()).into(iv_image);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return v;
    }

    @Override
    public void onImageLayoutChanged() {
        if (viewModel.isImageZoom()) {
            iv_image.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        } else {
            iv_image.setLayoutParams(new LinearLayout.LayoutParams((int) getResources().getDimension(R.dimen.news_dialog_icon_size), (int) getResources().getDimension(R.dimen.news_dialog_icon_size)));
        }
    }
}
