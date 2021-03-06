package de.fau.cs.mad.fablab.android.view.fragments.projects;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.pedrogomez.renderers.RVRendererAdapter;

import javax.inject.Inject;

import butterknife.Bind;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.model.events.DeleteProjectEvent;
import de.fau.cs.mad.fablab.android.view.activities.MainActivity;
import de.fau.cs.mad.fablab.android.view.common.binding.MenuItemCommandBinding;
import de.fau.cs.mad.fablab.android.view.common.binding.RecyclerViewSwipeCommandBinding;
import de.fau.cs.mad.fablab.android.view.common.fragments.BaseFragment;
import de.greenrobot.event.EventBus;

public class ProjectFragment extends BaseFragment implements ProjectFragmentViewModel.Listener{

    @Bind(R.id.projects_recycler_view)
    RecyclerView mProjectsRV;

    private RVRendererAdapter<ProjectViewModel> mAdapter;
    private EventBus mEventBus = EventBus.getDefault();

    @Inject
    ProjectFragmentViewModel mViewModel;

    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_project, menu);

        MenuItem newItem = menu.findItem(R.id.action_new);

        if(newItem != null) {
            new MenuItemCommandBinding().bind(newItem.getSubMenu().getItem(0),
                    mViewModel.getNewProjectCommand());
            new MenuItemCommandBinding().bind(newItem.getSubMenu().getItem(1),
                    mViewModel.getNewProjectFromCartCommand());
        }

    }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mProjectsRV.setLayoutManager(layoutManager);

        mAdapter = new RVRendererAdapter<>(getLayoutInflater(savedInstanceState),
                new ProjectViewModelRendererBuilder(),
                mViewModel.getProjectViewModelCollection());
        mProjectsRV.setAdapter(mAdapter);

        new RecyclerViewSwipeCommandBinding().bind(mProjectsRV, mViewModel.getConfirmDeletionCommand());

        mViewModel.setListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
        return inflater.inflate(R.layout.fragment_projects, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        mEventBus.register(this);

        setDisplayOptions(MainActivity.DISPLAY_LOGO | MainActivity.DISPLAY_NAVDRAWER);
        mViewModel.update();
        setNavigationDrawerSelection(R.id.drawer_item_projects);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        mEventBus.unregister(this);
    }

    @Override
    public void onNewProjectClicked() {
        EditProjectFragment fragment = new EditProjectFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        args.putSerializable(getResources().getString(R.string.key_project), null);
        args.putSerializable(getResources().getString(R.string.key_cart), null);
        getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                fragment).addToBackStack(null).commit();
    }

    @SuppressWarnings("unused")
    public void onEvent(ProjectClickedEvent event) {
        EditProjectFragment fragment = new EditProjectFragment();
        Bundle args = new Bundle();
        args.putSerializable(getResources().getString(R.string.key_project), event.getProject());
        args.putSerializable(getResources().getString(R.string.key_cart), null);
        fragment.setArguments(args);
        getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                fragment).addToBackStack(null).commit();
    }

    @SuppressWarnings("unused")
    public void onEvent(DeleteProjectEvent event) {
        // project should be deleted via swipe
        if(event.getDelete() && (event.getProject() != null))
        {
            mViewModel.deleteProject(event.getProject());
        }
        // project swiped but should not be deleted
        else
        {
            onDataChanged();
        }
    }

    @Override
    public void onDataChanged() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void noCartsAvailable() {
        Toast.makeText(getActivity(), getResources().getString(R.string.no_carts_available), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showCartChooser() {
        CartChooserFragment fragment = new CartChooserFragment();
        getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                fragment).addToBackStack(null).commit();
    }

    @Override
    public void confirmDeletion(int position) {
        ConfirmDeleteDialogFragment fragment = new ConfirmDeleteDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(getString(R.string.key_delete_project_project),
                mAdapter.getItem(position).getProject());
        fragment.setArguments(args);
        fragment.show(getFragmentManager(), "confirm_delete_dialog");
    }

    @Override
    public void deleteFailure() {
        Toast.makeText(getActivity(), getResources().getString(R.string.delete_project_failure), Toast.LENGTH_SHORT).show();
    }
}
