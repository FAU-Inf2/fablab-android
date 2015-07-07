package de.fau.cs.mad.fablab.android.view.fragments.icals;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.InjectView;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.view.common.binding.ViewCommandBinding;
import de.fau.cs.mad.fablab.android.view.common.fragments.BaseDialogFragment;

public class ICalDetailsDialogFragment extends BaseDialogFragment
        implements ICalDetailsDialogFragmentViewModel.Listener {
    @InjectView(R.id.title_dates_dialog)
    TextView title_tv;
    @InjectView(R.id.date_dates_dialog)
    TextView date_tv;
    @InjectView(R.id.time_dates_dialog)
    TextView time_tv;
    @InjectView(R.id.location_dates_dialog)
    TextView location_tv;
    @InjectView(R.id.date_description_dates_dialog)
    TextView description_tv;
    @InjectView(R.id.ok_button_dates_dialog)
    Button dismiss_button;

    @Inject
    ICalDetailsDialogFragmentViewModel mViewModel;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel.setListener(this);
        mViewModel.initialize(getArguments());

        new ViewCommandBinding().bind(dismiss_button, mViewModel.getDismissDialogCommand());

        title_tv.setText(mViewModel.getTitle());
        date_tv.setText(getString(R.string.dates_date) + " " + mViewModel.getDate());
        time_tv.setText(getString(R.string.dates_time) + " " + mViewModel.getTime());
        if (mViewModel.getLocation() != null) {
            location_tv.setText(getString(R.string.dates_location) + " "
                    + mViewModel.getLocation());
        } else {
            location_tv.setVisibility(View.GONE);
        }
        description_tv.setText(mViewModel.getDescription());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return inflater.inflate(R.layout.fragment_dates_dialog, container, false);
    }

    @Override
    public void onDismiss() {
        dismiss();
    }
}
