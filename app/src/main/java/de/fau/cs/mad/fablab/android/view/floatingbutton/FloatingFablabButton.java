package de.fau.cs.mad.fablab.android.view.floatingbutton;

import android.app.Activity;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;

import butterknife.ButterKnife;
import butterknife.InjectView;
import dagger.ObjectGraph;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.view.common.binding.ViewCommandBinding;
import de.fau.cs.mad.fablab.android.viewmodel.dependencyinjection.ViewModelModule;

public class FloatingFablabButton {

    Activity activity;
    FloatingFablabButtonViewModel viewModel;

    @InjectView(R.id.scan_FAB)
    FloatingActionButton scanButton;
    @InjectView(R.id.search_FAB)
    FloatingActionButton searchButton;

    public FloatingFablabButton(Activity activity, View v){
        this.activity = activity;

        ButterKnife.inject(this, v);

        ObjectGraph objectGraph = ObjectGraph.create(new ViewModelModule(activity));
        viewModel = objectGraph.get(FloatingFablabButtonViewModel.class);

        new ViewCommandBinding().bind(scanButton, viewModel.getStartBarcodeScannerCommand());
        new ViewCommandBinding().bind(searchButton, viewModel.getStartProductSearchCommand());
    }
}
