package de.fau.cs.mad.fablab.android.view.fragments.alert;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import javax.inject.Inject;

import butterknife.InjectView;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.view.common.fragments.BaseDialogFragment;

public class AlertDialogFragment extends BaseDialogFragment {

    @InjectView(R.id.alert_dialog_tool_spinner)
    Spinner mToolSpinner;
    @InjectView(R.id.alert_dialog_edit_text)
    EditText mEditText;
    @InjectView(R.id.alert_dialog_ok_button)
    Button mOKButton;
    @InjectView(R.id.alert_dialog_cancel_button)
    Button mCancelButton;

    @Inject
    AlertDialogFragmentViewModel mViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return inflater.inflate(R.layout.alert_dialog, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
