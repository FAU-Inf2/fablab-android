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

import butterknife.InjectView;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.view.common.fragments.BaseFragment;

public class AboutFragment extends BaseFragment {
    @InjectView(R.id.about_version)
    TextView version_tv;
    @InjectView(R.id.about_content)
    TextView content_tv;
    @InjectView(R.id.about_used_libraries)
    TextView used_libraries_tv;

    @Inject
    public AboutFragment() {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String version;
        try {
            PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(
                    getActivity().getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
        version_tv.setText(version);

        content_tv.setText(Html.fromHtml(getString(R.string.about_content)));
        content_tv.setMovementMethod(LinkMovementMethod.getInstance());

        used_libraries_tv.setText(Html.fromHtml(getString(R.string.about_used_libraries)));
        used_libraries_tv.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about, container, false);
    }
}
