package de.fau.cs.mad.fablab.android.view.fragments.projects;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import javax.inject.Inject;

import butterknife.Bind;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.view.activities.MainActivity;
import de.fau.cs.mad.fablab.android.view.common.binding.MenuItemCommandBinding;
import de.fau.cs.mad.fablab.android.view.common.fragments.BaseFragment;
import de.fau.cs.mad.fablab.android.viewmodel.common.Project;

public class EditProjectFragment extends BaseFragment implements EditProjectFragmentViewModel.Listener{

    @Bind(R.id.edit_project_title_et)
    EditText mTitleTV;
    @Bind(R.id.edit_project_short_description_et)
    EditText mShortDescriptionTV;
    @Bind(R.id.edit_project_description_et)
    EditText mDescriptionTV;

    @Inject
    EditProjectFragmentViewModel mViewModel;

    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_edit_project, menu);

        MenuItem addPhotoItem = menu.findItem(R.id.action_add_photo_project);
        MenuItem saveProjectItem = menu.findItem(R.id.action_save_project);

        new MenuItemCommandBinding().bind(saveProjectItem, mViewModel.getSaveProjectCommand());

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Project project = (Project) getArguments().getSerializable(getResources().getString(R.string.key_project));
        if(project != null)
        {
            mTitleTV.setText(project.getProjectFile().getFilename());
            mShortDescriptionTV.setText(project.getProjectFile().getDescription());
            mDescriptionTV.setText(project.getProjectFile().getContent());
        }
        mViewModel.setProject(project);

        mViewModel.setListener(this);
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
        setNavigationDrawerSelection(R.id.drawer_item_projects);
    }

    @Override
    public void onSaveProjectClicked() {
        SaveProjectDialogFragment fragment = new SaveProjectDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(getResources().getString(R.string.key_project), mViewModel.getProject());
        fragment.setArguments(args);
        fragment.show(getFragmentManager(), "SaveProjectDialogFragment");
    }

    @Override
    public String getTitle() {
        return mTitleTV.getText().toString();
    }

    @Override
    public String getShortDescription() {
        return mShortDescriptionTV.getText().toString();
    }

    @Override
    public String getText() {
        return mDescriptionTV.getText().toString();
    }
}
