package de.fau.cs.mad.fablab.android.view.floatingbutton;

import android.view.View;

import com.github.clans.fab.FloatingActionButton;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.view.MainActivity;
import de.fau.cs.mad.fablab.android.view.common.binding.ViewCommandBinding;

public class FloatingFablabButton {

    @Inject
    FloatingFablabButtonViewModel viewModel;

    @InjectView(R.id.scan_FAB)
    FloatingActionButton scanButton;
    @InjectView(R.id.search_FAB)
    FloatingActionButton searchButton;

    public FloatingFablabButton(MainActivity activity, View v){
        activity.inject(this);
        ButterKnife.inject(this, v);

        new ViewCommandBinding().bind(scanButton, viewModel.getStartBarcodeScannerCommand());
        new ViewCommandBinding().bind(searchButton, viewModel.getStartProductSearchCommand());
    }
}
