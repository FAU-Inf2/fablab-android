package de.fau.cs.mad.fablab.android.view.fragments.projects;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.j256.ormlite.dao.ForeignCollection;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

import javax.inject.Inject;

import butterknife.Bind;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.model.entities.Cart;
import de.fau.cs.mad.fablab.android.model.entities.CartEntry;
import de.fau.cs.mad.fablab.android.view.activities.MainActivity;
import de.fau.cs.mad.fablab.android.view.common.binding.MenuItemCommandBinding;
import de.fau.cs.mad.fablab.android.view.common.fragments.BaseFragment;
import de.fau.cs.mad.fablab.android.viewmodel.common.Project;
import de.fau.cs.mad.fablab.rest.core.ProjectImageUpload;

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
    private static final int REQUEST_CAMERA = 200;

    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_edit_project, menu);

        MenuItem addPhotoItem = menu.findItem(R.id.action_add_photo_project);
        MenuItem saveProjectItem = menu.findItem(R.id.action_save_project);

        new MenuItemCommandBinding().bind(saveProjectItem, mViewModel.getSaveProjectCommand());

        if(addPhotoItem != null) {
            new MenuItemCommandBinding().bind(addPhotoItem.getSubMenu().getItem(0),
                    mViewModel.getAddPhotoGalleryCommand());
            new MenuItemCommandBinding().bind(addPhotoItem.getSubMenu().getItem(1),
                    mViewModel.getAddPhotoCameraCommand());
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Project project = (Project) getArguments().getSerializable(getResources().getString(R.string.key_project));
        Cart cart = (Cart) getArguments().getSerializable(getResources().getString(R.string.key_cart));
        if(project != null)
        {
            mTitleTV.setText(project.getProjectFile().getFilename());
            mShortDescriptionTV.setText(project.getProjectFile().getDescription());
            mDescriptionTV.setText(project.getProjectFile().getContent());
        }
        else
        {
            if(cart != null)
            {
                String text = getString(R.string.edit_cart_cart) + "\n";
                ForeignCollection<CartEntry> entries = cart.getEntries();
                for(CartEntry e : entries)
                {
                    text += "* " + e.getAmount() + " " + e.getProduct().getName() + "\n";
                }
                text += "\n";
                text += getString(R.string.edit_cart_description) + "\n";
                mDescriptionTV.setText(text);
            }
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
        Intent pickIntent = new Intent(Intent.ACTION_GET_CONTENT, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");
        Intent chooserIntent = Intent.createChooser(pickIntent, "Select Image");
        startActivityForResult(chooserIntent, PICK_IMAGE);
    }

    @Override
    public void startCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
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
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK
                && null != data) {

            Uri selectedImage = data.getData();
            String fileName = selectedImage.getLastPathSegment();
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getApplicationContext().getContentResolver().openInputStream(selectedImage));
                if(bitmap == null)
                {
                    Toast.makeText(getActivity(), getResources().getString(R.string.project_photo_image_wrong_format), Toast.LENGTH_SHORT).show();
                }
                else {
                    processBitmap(bitmap, fileName);
                }
            } catch (OutOfMemoryError error) {
                Toast.makeText(getActivity(), getResources().getString(R.string.project_photo_image_too_large), Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK
                && null != data) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            processBitmap(bitmap, UUID.randomUUID().toString());
        }
    }

    private void processBitmap(Bitmap bitmap, String fileName)
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        try {
            byte[] array = bos.toByteArray();
            showProgressBar(true);
            mViewModel.uploadImage(new ProjectImageUpload(fileName + ".png",
                          array, mViewModel.getProject().getGistID()));

            bitmap.recycle();
            bos.close();
            bos = null;
        }
        catch(OutOfMemoryError error)
        {
            Toast.makeText(getActivity(), getResources().getString(R.string.project_photo_image_too_large), Toast.LENGTH_SHORT).show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
}
