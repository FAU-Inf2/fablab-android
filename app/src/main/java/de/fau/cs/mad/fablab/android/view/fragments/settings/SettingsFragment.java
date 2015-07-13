package de.fau.cs.mad.fablab.android.view.fragments.settings;

import android.os.Bundle;

import com.github.machinarius.preferencefragment.PreferenceFragment;

import de.fau.cs.mad.fablab.android.R;

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

    }
