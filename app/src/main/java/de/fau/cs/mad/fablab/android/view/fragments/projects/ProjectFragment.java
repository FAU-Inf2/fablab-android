package de.fau.cs.mad.fablab.android.view.fragments.projects;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import butterknife.Bind;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.view.activities.MainActivity;
import de.fau.cs.mad.fablab.android.view.common.binding.MenuItemCommandBinding;
import de.fau.cs.mad.fablab.android.view.common.fragments.BaseFragment;

public class ProjectFragment extends BaseFragment implements ProjectFragmentViewModel.Listener{

    @Bind(R.id.projects_recycler_view)
    RecyclerView projectsRV;

    @Inject
    ProjectFragmentViewModel mViewModel;

    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_project, menu);

        MenuItem newItem = menu.findItem(R.id.action_new);
        new MenuItemCommandBinding().bind(newItem, mViewModel.getNewProjectCommand());

    }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel.setListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        return inflater.inflate(R.layout.fragment_projects, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        setDisplayOptions(MainActivity.DISPLAY_LOGO | MainActivity.DISPLAY_NAVDRAWER);
        //setNavigationDrawerSelection(R.id.drawer_item_projects);
    }

    @Override
    public void onNewProjectClicked() {
        EditProjectFragment fragment = new EditProjectFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        args.putSerializable(getResources().getString(R.string.key_project), null);
        getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                fragment).addToBackStack(null).commit();
    }
}
