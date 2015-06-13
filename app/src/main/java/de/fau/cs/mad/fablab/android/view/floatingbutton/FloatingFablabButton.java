package de.fau.cs.mad.fablab.android.view.floatingbutton;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;

import java.util.LinkedList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import dagger.ObjectGraph;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.view.common.binding.BaseBinding;
import de.fau.cs.mad.fablab.android.view.common.binding.Bindable;
import de.fau.cs.mad.fablab.android.view.common.binding.ViewCommandBinding;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.fau.cs.mad.fablab.android.viewmodel.dependencyinjection.ViewModelModule;

public class FloatingFablabButton implements Bindable{

    Activity activity;
    List<BaseBinding> bindings;
    FloatingFablabButtonViewModel viewModel;

    @InjectView(R.id.scan_FAB)
    FloatingActionButton scanButton;
    @InjectView(R.id.search_FAB)
    FloatingActionButton searchButton;

    public FloatingFablabButton(Activity activity, View v){
        this.bindings = new LinkedList<>();
        this.activity = activity;

        ButterKnife.inject(this, v);

        ObjectGraph objectGraph = ObjectGraph.create(new ViewModelModule(activity));
        viewModel = objectGraph.get(FloatingFablabButtonViewModel.class);

        bindViewToCommand(scanButton, viewModel.getStartBarcodeScannerCommand());
        bindViewToCommand(searchButton, viewModel.getStartProductSearchCommand());
    }

    @Override
    public void bindViewToCommand(View view, Command command) {
        ViewCommandBinding binding = new ViewCommandBinding(view, command);
        bindings.add(binding);
    }

    @Override
    public void bindRecyclerView(RecyclerView recycler, Command command) {
        throw new UnsupportedOperationException();
    }
}
