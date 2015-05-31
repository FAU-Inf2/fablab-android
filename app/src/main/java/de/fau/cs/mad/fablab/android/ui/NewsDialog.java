package de.fau.cs.mad.fablab.android.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import de.fau.cs.mad.fablab.android.R;

public class NewsDialog extends DialogFragment {

    static final String IMAGE = "IMAGE";
    static final String TITLE = "TITLE";
    static final String TEXT = "TEXT";

    private Bitmap image;
    private String title;
    private String text;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();

        image = args.getParcelable(IMAGE);
        title = args.getString(TITLE);
        text = args.getString(TEXT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_news_dialog, container, false);

        TextView title_TV = (TextView) v.findViewById(R.id.title_news_dialog);
        TextView text_TV = (TextView) v.findViewById((R.id.news_text_news_dialog));
        text_TV.setLinksClickable(true);
        text_TV.setMovementMethod(LinkMovementMethod.getInstance());
        ImageView image_IV = (ImageView) v.findViewById((R.id.image_news_dialog));

        text_TV.setText(Html.fromHtml(text));
        title_TV.setText(title);
        image_IV.setImageBitmap(image);

        Button okButton = (Button) v.findViewById(R.id.ok_button_news_dialog);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return v;
    }
}
