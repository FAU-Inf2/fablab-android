package de.fau.cs.mad.fablab.android.ui;

import android.app.DialogFragment;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.model.StorageFragment;

public class NewsDialog extends DialogFragment {

    static final String IMAGE = "IMAGE";
    static final String TITLE = "TITLE";
    static final String TEXT = "TEXT";

    private String imageLink;
    private String title;
    private String text;

    private boolean imageZoom = false;

    @InjectView(R.id.title_news_dialog)
    TextView tv_title;
    @InjectView(R.id.news_text_news_dialog)
    TextView tv_content;
    @InjectView(R.id.image_news_dialog)
    ImageView iv_image;

    @OnClick(R.id.ok_button_news_dialog) void okClick(){
        dismiss();
    }

    @OnClick(R.id.image_news_dialog) void imageClick(){
        Toast.makeText(getActivity(), "Image Click!", Toast.LENGTH_LONG).show();
        if(!imageZoom){
            iv_image.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            imageZoom = true;
        }
        else{
            iv_image.setLayoutParams(new LinearLayout.LayoutParams((int)getResources().getDimension(R.dimen.news_dialog_icon_size), (int)getResources().getDimension(R.dimen.news_dialog_icon_size)));
            imageZoom = false;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        imageLink = args.getString(IMAGE);
        title = args.getString(TITLE);
        text = args.getString(TEXT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_news_dialog, container, false);
        ButterKnife.inject(this, v);

        tv_content.setLinksClickable(true);
        tv_content.setMovementMethod(LinkMovementMethod.getInstance());
        tv_content.setText(Html.fromHtml(text));
        tv_title.setText(title);

        Picasso.with(iv_image.getContext()).load(imageLink).into(iv_image);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return v;
    }
}
