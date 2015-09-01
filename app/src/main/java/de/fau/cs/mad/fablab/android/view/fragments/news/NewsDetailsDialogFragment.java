package de.fau.cs.mad.fablab.android.view.fragments.news;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.Bind;

import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.view.common.binding.ViewCommandBinding;
import de.fau.cs.mad.fablab.android.view.common.fragments.BaseDialogFragment;

public class NewsDetailsDialogFragment extends BaseDialogFragment
        implements NewsDetailsDialogViewModel.Listener {
    @Bind(R.id.news_dialog_title)
    TextView title_tv;
    @Bind(R.id.news_dialog_webview)
    WebView webView;
    @Bind(R.id.news_dialog_image)
    ImageView image_iv;

    @Inject
    NewsDetailsDialogViewModel mViewModel;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel.setListener(this);
        mViewModel.initialize(getArguments());

        new ViewCommandBinding().bind(image_iv, mViewModel.getImageClickCommand());

        title_tv.setText(mViewModel.getTitle());
        webView.loadData(mViewModel.getText(), "text/html; charset=utf-8", "UTF-8");
        if (mViewModel.getImageLink() != null) {
            Picasso.with(image_iv.getContext()).load(mViewModel.getImageLink()).into(image_iv);
        } else {
            Picasso.with(image_iv.getContext()).load(R.drawable.news_nopicture).fit().into(image_iv);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news_dialog, container, false);
    }

    @Override
    public void onImageClicked() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(mViewModel.getImageLink()));
        getActivity().startActivity(intent);
    }
}
