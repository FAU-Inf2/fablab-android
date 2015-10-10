package de.fau.cs.mad.fablab.android.view.fragments.news;

import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.Bind;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.view.activities.MainActivity;
import de.fau.cs.mad.fablab.android.view.common.binding.ViewCommandBinding;
import de.fau.cs.mad.fablab.android.view.common.fragments.BaseDialogFragment;
import de.fau.cs.mad.fablab.android.viewmodel.common.ObservableWebView;

public class NewsDetailsDialogFragment extends BaseDialogFragment
        implements NewsDetailsDialogViewModel.Listener {
    @Bind(R.id.news_dialog_title)
    TextView title_tv;
    @Bind(R.id.news_dialog_webview)
    ObservableWebView webView;
    @Bind(R.id.news_dialog_image)
    ImageView image_iv;
    @Bind(R.id.news_dialog_link_tv)
    WebView link_tv;

    LinearLayout title_ll;
    LinearLayout header_ll;

    @Inject
    NewsDetailsDialogViewModel mViewModel;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel.setListener(this);
        mViewModel.initialize(getArguments());

        new ViewCommandBinding().bind(image_iv, mViewModel.getImageClickCommand());

        title_ll = (LinearLayout) getActivity().findViewById(R.id.news_dialog_title_ll);
        header_ll = (LinearLayout) getActivity().findViewById(R.id.news_dialog_header_ll);

        title_tv.setText(mViewModel.getNews().getTitle());

        String stylesheet = "<link rel=\"stylesheet\" type=\"text/css\" href=\"news_dialog_stylesheet.css\" /> ";
        String htmlData = stylesheet + "<div class=\"webview_content\">" + mViewModel.getNews().getDescription() + "</div>";

        webView.loadDataWithBaseURL("file:///android_asset/", htmlData, "text/html", "UTF-8", null);
        if (mViewModel.getNews().getLinkToPreviewImage() != null) {
            Picasso.with(image_iv.getContext()).load(mViewModel.getNews().getLinkToPreviewImage()).into(image_iv);
        } else {
            Picasso.with(image_iv.getContext()).load(R.drawable.news_nopicture).fit().into(image_iv);
        }

        header_ll.bringToFront();

        webView.setOnScrollChangedCallback(new ObservableWebView.OnScrollChangedCallback() {
            @Override
            public void onScroll(int l, int t) {
                if(t < 180) {
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    lp.setMargins(0, 0, 0, t);
                    title_ll.setLayoutParams(lp);
                }
            }
        });

        String link = stylesheet +
                "<html><body class=\"link\"><a href=" +  mViewModel.getNews().getLink() + ">" + mViewModel.getNews().getLink() + "</a></body></html>";
        link_tv.loadDataWithBaseURL("file:///android_asset/", link, "text/html", "UTF-8", null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        return inflater.inflate(R.layout.fragment_news_dialog, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        setDisplayOptions(MainActivity.DISPLAY_LOGO);
    }

    @Override
    public void onImageClicked() {
        final Dialog builder = new Dialog(getActivity());
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ImageView imageView = new ImageView(getActivity());
        Picasso.with(imageView.getContext()).load(mViewModel.getNews().getLinkToPreviewImage()).into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.dismiss();
            }
        });
        builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        builder.show();
    }
}
