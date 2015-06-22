package de.fau.cs.mad.fablab.android.view.fragments.icals.viewpager;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.view.common.binding.ViewCommandBinding;
import de.fau.cs.mad.fablab.android.view.common.fragments.BaseFragment;

public class ICalFragment extends BaseFragment{

    @Inject
    ICalFragmentViewModel viewModel;

    @InjectView(R.id.dates_card_left)
    CardView cardLeft;
    @InjectView(R.id.title_dates_entry_left)
    TextView titleLeft;
    @InjectView(R.id.date_dates_entry_left)
    TextView dateLeft;
    @InjectView(R.id.time_dates_entry_left)
    TextView timeLeft;
    @InjectView(R.id.location_dates_entry_left)
    TextView locationLeft;
    @InjectView(R.id.dates_card_right)
    CardView cardRight;
    @InjectView(R.id.title_dates_entry_right)
    TextView titleRight;
    @InjectView(R.id.date_dates_entry_right)
    TextView dateRight;
    @InjectView(R.id.time_dates_entry_right)
    TextView timeRight;
    @InjectView(R.id.location_dates_entry_right)
    TextView locationRight;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setViewModel(ICalFragmentViewModel viewModel)
    {
        this.viewModel = viewModel;
    }

    public ICalFragmentViewModel getViewModel()
    {
        return this.viewModel;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_dates, container, false);
        ButterKnife.inject(this, rootView);

        if(viewModel.getLeftItem() != null) {
            new ViewCommandBinding().bind(cardLeft, viewModel.getShowDetailsDialogLeftCommand());
            cardLeft.setVisibility(View.VISIBLE);
            titleLeft.setText(viewModel.getLeftItem().getSummery());
            Calendar cal = Calendar.getInstance();
            cal.setTime(viewModel.getLeftItem().getDtstartAsDate());
            dateLeft.setText(Integer.toString(cal.get(Calendar.DAY_OF_MONTH)) + "." + Integer.toString(cal.get(Calendar.MONTH)+1) + "." + Integer.toString(cal.get(Calendar.YEAR)));
            timeLeft.setText(Integer.toString(cal.get(Calendar.HOUR_OF_DAY)) + ":" + Integer.toString(cal.get(Calendar.MINUTE) + 100).substring(1));
            locationLeft.setText(viewModel.getLeftItem().getLocation());
        }


        if(viewModel.getRightItem() != null)
        {
            new ViewCommandBinding().bind(cardRight, viewModel.getShowDetailsDialogRightCommand());
            cardRight.setVisibility(View.VISIBLE);
            titleRight.setText(viewModel.getRightItem().getSummery());
            Calendar cal = Calendar.getInstance();
            cal.setTime(viewModel.getRightItem().getDtstartAsDate());
            dateRight.setText(Integer.toString(cal.get(Calendar.DAY_OF_MONTH)) + "." + Integer.toString(cal.get(Calendar.MONTH)+1) + "." + Integer.toString(cal.get(Calendar.YEAR)));
            timeRight.setText(Integer.toString(cal.get(Calendar.HOUR_OF_DAY)) + ":" + Integer.toString(cal.get(Calendar.MINUTE) + 100).substring(1));
            locationRight.setText(viewModel.getRightItem().getLocation());
        }

        return rootView;
    }

}
