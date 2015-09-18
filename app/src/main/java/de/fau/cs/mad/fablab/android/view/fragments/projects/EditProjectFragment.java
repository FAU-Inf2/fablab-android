package de.fau.cs.mad.fablab.android.view.fragments.projects;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.view.activities.MainActivity;
import de.fau.cs.mad.fablab.android.view.common.fragments.BaseFragment;

public class EditProjectFragment extends BaseFragment {

    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_edit_project, menu);

        MenuItem addPhotoItem = menu.findItem(R.id.action_add_photo_project);
        MenuItem saveProjectItem = menu.findItem(R.id.action_save_project);

    }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        return inflater.inflate(R.layout.fragment_edit_project, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        setDisplayOptions(MainActivity.DISPLAY_LOGO | MainActivity.DISPLAY_NAVDRAWER);
        //setNavigationDrawerSelection(R.id.drawer_item_projects);
    }
}
