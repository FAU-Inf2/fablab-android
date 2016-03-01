package de.fau.cs.mad.fablab.android.view.fragments.projects;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.Bind;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.view.common.binding.ViewCommandBinding;
import de.fau.cs.mad.fablab.android.view.common.fragments.BaseDialogFragment;

public class LicenseInformationDialogFragment extends BaseDialogFragment
        implements LicenseInformationDialogFragmentViewModel.Listener {

    @Bind(R.id.project_license_information_title_tv)
    TextView title_tv;
    @Bind(R.id.project_license_ok_button)
    Button ok_button;
    @Bind(R.id.project_license_cancel_button)
    Button cancel_button;

    @Inject
    LicenseInformationDialogFragmentViewModel mViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return inflater.inflate(R.layout.fragment_license_information_dialog, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        new ViewCommandBinding().bind(ok_button, mViewModel.getOKCommand());
        new ViewCommandBinding().bind(cancel_button, mViewModel.getCancelCommand());

        title_tv.setText(Html.fromHtml(getString(R.string.license_information_title)));
        title_tv.setMovementMethod(LinkMovementMethod.getInstance());

        mViewModel.setListener(this);
    }

    @Override
    public void buttonClicked() {
        dismiss();
    }
}
