package de.fau.cs.mad.fablab.android.view.fragments.about;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.Bind;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.view.common.binding.ViewCommandBinding;
import de.fau.cs.mad.fablab.android.view.common.fragments.BaseFragment;

public class AboutFragment extends BaseFragment implements AboutFragmentViewModel.Listener {
    @Bind(R.id.about_version)
    TextView version_tv;
    @Bind(R.id.about_own_license)
    TextView own_license_tv;
    @Bind(R.id.about)
    TextView about_tv;
    @Bind(R.id.about_content)
    TextView content_tv;
    @Bind(R.id.about_open_source_licenses)
    TextView about_open_source_licenses_tv;
    @Bind(R.id.about_used_libraries)
    TextView used_libraries_tv;

    @Inject
    AboutFragmentViewModel mViewModel;

    @Inject
    public AboutFragment(){

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel.setListener(this);

        String version;
        try {
            PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(
                    getActivity().getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }


        version_tv.setText(version);
        new ViewCommandBinding().bind(version_tv, mViewModel.getToggleVersionCommand());

        own_license_tv.setText(Html.fromHtml(getString(R.string.about_license)));
        own_license_tv.setVisibility(View.GONE);

        new ViewCommandBinding().bind(about_tv, mViewModel.getToggleAboutCommand());

        content_tv.setText(Html.fromHtml(getString(R.string.about_content)));
        content_tv.setMovementMethod(LinkMovementMethod.getInstance());
        content_tv.setVisibility(View.GONE);

        new ViewCommandBinding().bind(about_open_source_licenses_tv, mViewModel.getToggleOpenSourceLicensesCommand());

        used_libraries_tv.setText(Html.fromHtml(getString(R.string.about_used_libraries)));
        used_libraries_tv.setMovementMethod(LinkMovementMethod.getInstance());
        used_libraries_tv.setVisibility(View.GONE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        setDisplayOptions(R.id.drawer_item_about, false, false);
    }

    private void toogle(TextView tv){
        tv.setVisibility(tv.isShown() ? View.GONE : View.VISIBLE);
    }

    @Override
    public void toggleVersion() {
        toogle(own_license_tv);
    }

    @Override
    public void toggleAbout() {
        toogle(content_tv);
    }

    @Override
    public void toggleOpenSourceLicenses() {
        toogle(used_libraries_tv);
    }
}
