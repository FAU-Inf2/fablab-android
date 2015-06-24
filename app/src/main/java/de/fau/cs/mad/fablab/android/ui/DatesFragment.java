package de.fau.cs.mad.fablab.android.ui;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.rest.core.ICal;

public class DatesFragment extends Fragment {

    static final String ICAL1 = "ICAL1";
    static final String ICAL2 = "ICAL2";

    static final String IMAGE = "IMAGE";
    static final String TITLE = "TITLE";
    static final String DATE = "DATE";
    static final String TIME = "TIME";
    static final String LOCATION = "LOCATION";

    private ICal iCal1;
    private ICal iCal2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();

        iCal1 = (ICal) args.getSerializable(ICAL1);
        iCal2 = (ICal) args.getSerializable(ICAL2);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dates, container, false);

        TextView titleLeft = (TextView) v.findViewById(R.id.title_dates_entry_left);
        TextView titleRight = (TextView) v.findViewById(R.id.title_dates_entry_right);
        TextView dateLeft = (TextView) v.findViewById(R.id.date_dates_entry_left);
        TextView dateRight = (TextView) v.findViewById(R.id.date_dates_entry_right);
        TextView timeLeft = (TextView) v.findViewById(R.id.time_dates_entry_left);
        TextView timeRight = (TextView) v.findViewById(R.id.time_dates_entry_right);
        TextView locationLeft = (TextView) v.findViewById(R.id.location_dates_entry_left);
        TextView locationRight = (TextView) v.findViewById(R.id.location_dates_entry_right);
        ImageView iconLeft = (ImageView) v.findViewById(R.id.icon_dates_entry_left);
        ImageView iconRight = (ImageView) v.findViewById(R.id.icon_dates_entry_right);
        CardView cardRight = (CardView) v.findViewById(R.id.dates_card_right);
        CardView cardLeft = (CardView) v.findViewById(R.id.dates_card_left);

        if(iCal1 != null)
        {
            if(iCal1.getSummery().trim().toLowerCase().equals("openlab")) {
                cardLeft.setCardBackgroundColor(getResources().getColor(R.color.cardOpenlab));
            } else if(iCal1.getSummery().trim().toLowerCase().equals("selflab")) {
                cardLeft.setCardBackgroundColor(getResources().getColor(R.color.cardSelflab));
            } else {
                cardLeft.setCardBackgroundColor(getResources().getColor(R.color.cardDefault));
            }

            titleLeft.setText(iCal1.getSummery());
            dateLeft.setText(getDate(iCal1.getDtstartAsDate()));
            timeLeft.setText(getTime(iCal1.getDtstartAsDate()));
            locationLeft.setText(iCal1.getLocation());

            cardLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView title = (TextView) v.findViewById(R.id.title_dates_entry_left);
                    TextView date = (TextView) v.findViewById(R.id.date_dates_entry_left);
                    TextView time = (TextView) v.findViewById(R.id.time_dates_entry_left);
                    TextView location = (TextView) v.findViewById(R.id.location_dates_entry_left);
                    ImageView image = (ImageView) v.findViewById(R.id.icon_dates_entry_left);

                    Bundle args = new Bundle();
                    args.putString(TITLE, title.getText().toString());
                    args.putString(DATE, date.getText().toString());
                    args.putString(TIME, time.getText().toString());
                    args.putString(LOCATION, location.getText().toString());
                    args.putParcelable(IMAGE, ((BitmapDrawable) image.getDrawable()).getBitmap());
                    DatesDialog dialog = new DatesDialog();
                    dialog.setArguments(args);
                    dialog.show(getFragmentManager(), "dates dialog");
                }
            });
        }
        else
        {
            cardLeft.setVisibility(View.INVISIBLE);
        }

        if(iCal2 != null)
        {
            if(iCal2.getSummery().trim().toLowerCase().equals("openlab")) {
                cardRight.setCardBackgroundColor(getResources().getColor(R.color.cardOpenlab));
            } else if(iCal2.getSummery().trim().toLowerCase().equals("selflab")) {
                cardRight.setCardBackgroundColor(getResources().getColor(R.color.cardSelflab));
            } else {
                cardRight.setCardBackgroundColor(getResources().getColor(R.color.cardDefault));
            }

            titleRight.setText(iCal2.getSummery());
            dateRight.setText(getDate(iCal2.getDtstartAsDate()));
            timeRight.setText(getTime(iCal2.getDtstartAsDate()));
            locationRight.setText(iCal2.getLocation());

            cardRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView title = (TextView) v.findViewById(R.id.title_dates_entry_right);
                    TextView date = (TextView) v.findViewById(R.id.date_dates_entry_right);
                    TextView time = (TextView) v.findViewById(R.id.time_dates_entry_right);
                    TextView location = (TextView) v.findViewById(R.id.location_dates_entry_right);
                    ImageView image = (ImageView) v.findViewById(R.id.icon_dates_entry_right);

                    Bundle args = new Bundle();
                    args.putString(TITLE, title.getText().toString());
                    args.putString(DATE, date.getText().toString());
                    args.putString(TIME, time.getText().toString());
                    args.putString(LOCATION, location.getText().toString());
                    args.putParcelable(IMAGE, ((BitmapDrawable) image.getDrawable()).getBitmap());
                    DatesDialog dialog = new DatesDialog();
                    dialog.setArguments(args);
                    dialog.show(getFragmentManager(), "dates dialog");
                }
            });
        } else {
            cardRight.setVisibility(View.INVISIBLE);
        }

        return v;
    }

    private String getDate(Date date)
    {
        //month+1, because Calendar is zero-based (eg january = 0 and not 1)
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return Integer.toString(cal.get(Calendar.DAY_OF_MONTH)) + "." + Integer.toString(cal.get(Calendar.MONTH)+1) + "." + Integer.toString(cal.get(Calendar.YEAR));
    }

    private String getTime(Date date)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return Integer.toString(cal.get(Calendar.HOUR_OF_DAY)) + ":" + Integer.toString(cal.get(Calendar.MINUTE) + 100).substring(1);
    }

}
