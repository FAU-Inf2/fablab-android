package de.fau.cs.mad.fablab.android.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import de.fau.cs.mad.fablab.android.R;

//@ContentView(R.layout.fragment_dates)
public class DatesFragment extends Fragment {

    /*
    @InjectView(R.id.title_dates_entry_left) TextView titleLeft;
    @InjectView(R.id.title_dates_entry_right) TextView titleRight;
    @InjectView(R.id.date_dates_entry_left) TextView dateLeft;
    @InjectView(R.id.date_dates_entry_right) TextView dateRight;
    @InjectView(R.id.time_dates_entry_left) TextView timeLeft;
    @InjectView(R.id.time_dates_entry_right) TextView timeRight;
    @InjectView(R.id.icon_dates_entry_left) ImageView iconLeft;
    @InjectView(R.id.icon_dates_entry_right) ImageView iconRight;
    */

    static final String LIST= "LIST";
    static final String POSITION = "POSITION";

    private List<String> dates;
    private int position;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();

        dates = args.getStringArrayList(LIST);
        position = args.getInt(POSITION);
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
        ImageView iconLeft = (ImageView) v.findViewById(R.id.icon_dates_entry_left);
        ImageView iconRight = (ImageView) v.findViewById(R.id.icon_dates_entry_right);
        CardView cardRight = (CardView) v.findViewById(R.id.dates_card_right);

        titleLeft.setText(dates.get(position));
        dateLeft.setText("29.05.2015");
        timeLeft.setText("10.00");
        if(position + 1 < dates.size())
        {
            titleRight.setText(dates.get(position+1));
            dateRight.setText("29.05.2015");
            timeRight.setText("10.00");
        } else {
            cardRight.setVisibility(View.INVISIBLE);
        }

        return v;
    }
}
