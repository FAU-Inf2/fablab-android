package de.fau.cs.mad.fablab.android.view.fragments.icals;

import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;

import javax.inject.Inject;

import butterknife.Bind;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.view.common.binding.ViewCommandBinding;
import de.fau.cs.mad.fablab.android.view.common.fragments.BaseDialogFragment;
import de.greenrobot.event.EventBus;

public class ICalDetailsDialogFragment extends BaseDialogFragment
        implements ICalDetailsDialogFragmentViewModel.Listener {
    @Bind(R.id.title_dates_dialog)
    TextView title_tv;
    @Bind(R.id.date_dates_dialog)
    TextView date_tv;
    @Bind(R.id.time_dates_dialog)
    TextView time_tv;
    @Bind(R.id.location_dates_dialog)
    TextView location_tv;
    @Bind(R.id.date_description_dates_dialog)
    TextView description_tv;
    @Bind(R.id.ok_button_dates_dialog)
    Button dismiss_button;
    @Bind(R.id.calendar_button_dates_dialog)
    Button calendar_button;

    @Inject
    ICalDetailsDialogFragmentViewModel mViewModel;

    private EventBus mEventBus = EventBus.getDefault();


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel.setListener(this);
        mViewModel.initialize(getArguments());

        new ViewCommandBinding().bind(dismiss_button, mViewModel.getDismissDialogCommand());
        new ViewCommandBinding().bind(calendar_button, mViewModel.getAddToCalendarCommand());

        title_tv.setText(mViewModel.getTitle());
        date_tv.setText(getString(R.string.dates_date) + " " + mViewModel.getDate());
        if(mViewModel.getTime().equals(" - ")) {
            time_tv.setVisibility(View.GONE);
        } else {
            time_tv.setText(getString(R.string.dates_time) + " " + mViewModel.getTime());
        }

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
    public void onPause() {
        super.onPause();
        mEventBus.unregister(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        mEventBus.register(this);
    }

    @Override
    public void onDismiss() {
        dismiss();
    }


    public void onEvent(AddToCalendarEvent event)
    {
        int[] startDate = event.getStartDate();
        int[] endDate = event.getEndDate();

        Calendar calendarStartTime = Calendar.getInstance();
        int[] startTime = event.getStartTime();
        calendarStartTime.set(startDate[2], startDate[1], startDate[0], startTime[0], startTime[1]);

        Calendar calendarEndTime = Calendar.getInstance();
        int[] endTime = event.getEndTime();
        calendarEndTime.set(endDate[2], endDate[1], endDate[0], endTime[0], endTime[1]);


        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, calendarStartTime.getTimeInMillis())
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, calendarEndTime.getTimeInMillis())
                .putExtra(CalendarContract.Events.TITLE, event.getTitle())
                .putExtra(CalendarContract.Events.EVENT_LOCATION, event.getLocation())
                .putExtra(CalendarContract.Events.DESCRIPTION, event.getDescription());

        if(event.isAllday())
            intent.putExtra(CalendarContract.Events.ALL_DAY, event.isAllday());

        startActivity(intent);
    }

}
