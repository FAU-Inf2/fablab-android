package de.fau.cs.mad.fablab.android.view.fragments.projects;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

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
    @Bind(R.id.project_image_upload_progress_bar)
    ProgressBar mProgressBar;

    @Inject
    EditProjectFragmentViewModel mViewModel;

    private static final int PICK_IMAGE = 100;

    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_edit_project, menu);

        MenuItem addPhotoItem = menu.findItem(R.id.action_add_photo_project);
        MenuItem saveProjectItem = menu.findItem(R.id.action_save_project);

        new MenuItemCommandBinding().bind(saveProjectItem, mViewModel.getSaveProjectCommand());
        new MenuItemCommandBinding().bind(addPhotoItem, mViewModel.getAddPhotoCommand());
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

    @Override
    public void startPicturePicker() {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

        startActivityForResult(chooserIntent, PICK_IMAGE);
    }

    @Override
    public void projectNotUploaded() {
        Toast.makeText(getActivity(), getResources().getString(R.string.project_photo_not_yet_uploaded), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgressBar(boolean show) {
        if(show)
        {
            mProgressBar.setVisibility(View.VISIBLE);
        }
        else
        {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void uploadFailure() {
        Toast.makeText(getActivity(), getResources().getString(R.string.project_photo_upload_failure), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setDescription(String text) {
        mDescriptionTV.setText(text);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK
                    && null != data) {

                Uri selectedImage = data.getData();
                String fileName = selectedImage.getLastPathSegment();
                Bitmap bitmap;
                try {
                    bitmap = BitmapFactory.decodeStream(getActivity().getApplicationContext().getContentResolver().openInputStream(selectedImage));

                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
                    try {
                        byte[] array = bos.toByteArray();
                        showProgressBar(true);
                        mViewModel.uploadImage(array, fileName);
                    } catch(OutOfMemoryError error)
                    {
                        Toast.makeText(getActivity(), getResources().getString(R.string.project_photo_image_too_large), Toast.LENGTH_SHORT).show();
                    }
                    bitmap.recycle();
                    bos.close();
                    bos = null;

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            } else {

            }
        } catch (Exception e) {

        }

    }
}
