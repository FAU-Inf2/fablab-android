package de.fau.cs.mad.fablab.android.view.fragments.reservation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

import javax.inject.Inject;

import butterknife.Bind;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.view.activities.MainActivity;
import de.fau.cs.mad.fablab.android.view.common.binding.ViewCommandBinding;
import de.fau.cs.mad.fablab.android.view.common.fragments.BaseDialogFragment;
import de.fau.cs.mad.fablab.rest.core.FabTool;
import de.fau.cs.mad.fablab.rest.core.User;

public class ReservationDialogFragment extends BaseDialogFragment
        implements ReservationDialogFragmentViewModel.Listener {

    @Bind(R.id.reservation_dialog_fragment_button)
    Button mAddButton;

    @Bind(R.id.reservation_dialog_user)
    EditText mEditTextUser;

    @Bind(R.id.reservation_dialog_project)
    EditText mEditTextProject;

    @Bind(R.id.reservation_dialog_duration)
    NumberPicker mNumberPickerDuration;

    @Inject
    ReservationDialogFragmentViewModel mViewModel;

    private User mUser;
    private FabTool mTool;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return inflater.inflate(R.layout.fragment_reservation_dialog, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel.setListener(this);

        mNumberPickerDuration.setMinValue(1);
        mNumberPickerDuration.setMaxValue(200);

        new ViewCommandBinding().bind(mAddButton, mViewModel.getAddCommand());
    }

    @Override
    public void onResume() {
        super.onResume();
        setDisplayOptions(MainActivity.DISPLAY_LOGO | MainActivity.DISPLAY_NAVDRAWER);
        setNavigationDrawerSelection(R.id.drawer_item_reservation);

        if(getUser() != null) {
            mEditTextUser.setText(getUser().getUsername());
        }
    }

    @Override
    public void onAddReservation() {
        if(getUser() != null) {
            mViewModel.addReservation(getUser(), getTool(), mEditTextProject.getText().toString(), mNumberPickerDuration.getValue());
        } else {
            mViewModel.addReservation(new User(mEditTextUser.getText().toString(), ""), getTool(), mEditTextProject.getText().toString(), mNumberPickerDuration.getValue());
        }
        dismiss();
    }

    public void setUser(User user)
    {
        this.mUser = user;
    }

    public User getUser()
    {
        return mUser;
    }

    public void setTool(FabTool fabTool)
    {
        this.mTool = fabTool;
    }

    public FabTool getTool()
    {
        return mTool;
    }
}
