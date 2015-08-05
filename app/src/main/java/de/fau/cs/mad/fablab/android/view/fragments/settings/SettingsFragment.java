package de.fau.cs.mad.fablab.android.view.fragments.settings;

import android.os.Bundle;

import com.github.machinarius.preferencefragment.PreferenceFragment;

import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.view.activities.MainActivity;

/**
 * Simple Settings/Preferences Fragment to provide some user configurable options.
 */
public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.settings);
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity activity = (MainActivity) getActivity();
        activity.enableNavigationDrawer(true);
        activity.setNavigationDrawerSelection(R.id.drawer_item_settings);
        activity.showFloatingActionButton(false);
        activity.showCartSlidingUpPanel(false);
    }
}
