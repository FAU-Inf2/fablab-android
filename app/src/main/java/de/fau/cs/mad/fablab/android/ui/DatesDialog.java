package de.fau.cs.mad.fablab.android.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import de.fau.cs.mad.fablab.android.R;

public class DatesDialog extends DialogFragment{

    static final String IMAGE = "IMAGE";
    static final String TITLE = "TITLE";
    static final String DATE = "DATE";
    static final String TIME = "TIME";
    static final String LOCATION = "LOCATION";
    static final String DESCRIPTION = "DESCRIPTION";

    private String title;
    private String date;
    private String time;
    private String location;
    private Bitmap image;
    private String description;

    public static DatesFragment newInstance() {
        DatesFragment f = new DatesFragment();
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();

        image = args.getParcelable(IMAGE);
        title = args.getString(TITLE);
        date = args.getString(DATE);
        time = args.getString(TIME);
        location = args.getString(LOCATION);
        description = args.getString(DESCRIPTION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dates_dialog, container, false);

        TextView title_TV = (TextView) v.findViewById(R.id.title_dates_dialog);
        TextView date_TV = (TextView) v.findViewById((R.id.date_dates_dialog));
        TextView time_TV = (TextView) v.findViewById((R.id.time_dates_dialog));
        TextView location_TV = (TextView) v.findViewById((R.id.location_dates_dialog));
        ImageView image_IV = (ImageView) v.findViewById((R.id.image_dates_dialog));
        TextView description_TV = (TextView) v.findViewById(R.id.date_description_dates_dialog);

        title_TV.setText(title);
        date_TV.setText("Datum: " + date);
        time_TV.setText("Uhrzeit: " + time);
        if(!location.isEmpty()) {
            location_TV.setText("Ort: " + location);
        }
        image_IV.setImageBitmap(image);
        description_TV.setText(description);

        Button okButton = (Button) v.findViewById(R.id.ok_button_dates_dialog);
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
