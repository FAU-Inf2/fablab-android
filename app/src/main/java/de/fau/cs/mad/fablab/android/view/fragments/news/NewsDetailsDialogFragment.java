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

import javax.inject.Inject;

import butterknife.InjectView;

import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.view.common.binding.ViewCommandBinding;
import de.fau.cs.mad.fablab.android.view.common.fragments.BaseDialogFragment;

public class NewsDetailsDialogFragment extends BaseDialogFragment
        implements NewsDetailsDialogViewModel.Listener {
    @InjectView(R.id.title_news_dialog)
    TextView title_tv;
    @InjectView(R.id.news_text_news_dialog)
    TextView text_tv;
    @InjectView(R.id.image_news_dialog)
    ImageView image_iv;
    @InjectView(R.id.ok_button_news_dialog)
    Button dismiss_button;

    @Inject
    NewsDetailsDialogViewModel mViewModel;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel.setListener(this);
        mViewModel.restoreState(getArguments(), savedInstanceState);

        new ViewCommandBinding().bind(image_iv, mViewModel.getImageClickCommand());
        new ViewCommandBinding().bind(dismiss_button, mViewModel.getDismissCommand());

        text_tv.setLinksClickable(true);
        text_tv.setMovementMethod(LinkMovementMethod.getInstance());
        text_tv.setText(Html.fromHtml(mViewModel.getText()));
        title_tv.setText(mViewModel.getTitle());
        Picasso.with(image_iv.getContext()).load(mViewModel.getImageLink()).into(image_iv);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return inflater.inflate(R.layout.fragment_news_dialog, container, false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        mViewModel.saveState(outState);
    }

    @Override
    public void onImageLayoutChanged() {
        if (mViewModel.isImageZoom()) {
            image_iv.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        } else {
            image_iv.setLayoutParams(new LinearLayout.LayoutParams(
                    (int) getResources().getDimension(R.dimen.news_dialog_icon_size),
                    (int) getResources().getDimension(R.dimen.news_dialog_icon_size)));
        }
    }

    @Override
    public void onDismiss() {
        dismiss();
    }
}
