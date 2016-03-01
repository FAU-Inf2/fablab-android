package de.fau.cs.mad.fablab.android.view.fragments.projects;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import javax.inject.Inject;

import butterknife.Bind;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.view.common.binding.ViewCommandBinding;
import de.fau.cs.mad.fablab.android.view.common.fragments.BaseDialogFragment;
import de.fau.cs.mad.fablab.android.viewmodel.common.Project;

public class ConfirmDeleteDialogFragment extends BaseDialogFragment
        implements ConfirmDeleteDialogFragmentViewModel.Listener{

    @Bind(R.id.delete_project_button)
    Button mDeleteButton;
    @Bind(R.id.not_delete_project_button)
    Button mNotDeleteButton;

    @Inject
    ConfirmDeleteDialogFragmentViewModel mViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return inflater.inflate(R.layout.fragment_confirm_delete_project_dialog, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(getArguments() != null) {
            int deletePosition = getArguments().getInt(getString(R.string.key_delete_project_position), -1);
            mViewModel.setDeletePosition(deletePosition);
            Project project = (Project) getArguments().getSerializable(
                    getString(R.string.key_delete_project_project));
            mViewModel.setProject(project);
        }

        new ViewCommandBinding().bind(mDeleteButton, mViewModel.getDeleteProjectCommand());
        new ViewCommandBinding().bind(mNotDeleteButton, mViewModel.getNotDeleteProjectCommand());

        mViewModel.setListener(this);
    }

    @Override
    public void onButtonClicked() {
        dismiss();
    }
}
