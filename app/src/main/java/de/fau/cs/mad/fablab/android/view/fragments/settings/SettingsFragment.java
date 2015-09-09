package de.fau.cs.mad.fablab.android.view.fragments.settings;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.machinarius.preferencefragment.PreferenceFragment;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.model.events.NoUpdateAvailableEvent;
import de.fau.cs.mad.fablab.android.view.activities.MainActivity;
import de.greenrobot.event.EventBus;

/**
 * Simple Settings/Preferences Fragment to provide some user configurable options.
 */
public class SettingsFragment extends PreferenceFragment {
    @Inject
    SettingsFragmentViewModel mViewModel;

    private EventBus mEventBus = EventBus.getDefault();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivity()).inject(this);

        mViewModel.initialize(getPreferenceScreen(), getPreferenceManager().getSharedPreferences());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.settings);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
        mEventBus.unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity activity = (MainActivity) getActivity();
        activity.enableNavigationDrawer(true);
        activity.showTitle(true);
        activity.setNavigationDrawerSelection(R.id.drawer_item_settings);
        activity.showFloatingActionButton(false);
        activity.showCartSlidingUpPanel(false);

        mEventBus.register(this);
    }

    @SuppressWarnings("unused")
    public void onEvent(NoUpdateAvailableEvent event) {
        Toast.makeText(getActivity(), R.string.version_check_no_update_available, Toast.LENGTH_LONG)
                .show();
    }
}
