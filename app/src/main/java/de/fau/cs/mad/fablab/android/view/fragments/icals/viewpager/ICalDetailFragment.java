package de.fau.cs.mad.fablab.android.view.fragments.icals.viewpager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import butterknife.InjectView;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.view.common.binding.ViewCommandBinding;
import de.fau.cs.mad.fablab.android.view.common.fragments.BaseDialogFragment;
import de.fau.cs.mad.fablab.android.view.fragments.icals.ICalConstants;

public class ICalDetailFragment extends BaseDialogFragment {

    ICalDetailFragmentViewModel viewModel;

    @InjectView(R.id.title_dates_dialog)
    TextView titleTextView;
    @InjectView(R.id.date_dates_dialog)
    TextView dateTextView;
    @InjectView(R.id.time_dates_dialog)
    TextView timeTextView;
    @InjectView(R.id.location_dates_dialog)
    TextView locationTextView;
    @InjectView(R.id.ok_button_news_dialog)
    Button dismissButton;

    String title;
    String date;
    String time;
    String location;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        title = args.getString(ICalConstants.TITLE);
        date = args.getString(ICalConstants.DATE);
        time = args.getString(ICalConstants.TIME);
        location = args.getString(ICalConstants.LOCATION);
    }

    public void setViewModel(ICalDetailFragmentViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dates_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        new ViewCommandBinding().bind(dismissButton, viewModel.getDismissDialogCommand());

        titleTextView.setText(title);
        dateTextView.setText(date);
        timeTextView.setText(time);
        locationTextView.setText(location);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    }
    /*
    public ICalDetailFragment()
    {

    }

    public void setViewModel(ICalDetailFragmentViewModel viewModel){
        this.viewModel = viewModel;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        title = args.getString(ICalConstants.TITLE);
        date = args.getString(ICalConstants.DATE);
        time = args.getString(ICalConstants.TIME);
        location = args.getString(ICalConstants.LOCATION);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_dates_dialog, container, false);
        ButterKnife.inject(this, rootView);

        titleTextView.setText(title);
        dateTextView.setText(date);
        timeTextView.setText(time);
        locationTextView.setText(location);

        bindViewToCommand(okButton, viewModel.getDismissDialogCommand());

        return rootView;
    }
    */
}
