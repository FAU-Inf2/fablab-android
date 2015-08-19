package de.fau.cs.mad.fablab.android.view.floatingbutton;

import android.view.View;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.view.activities.MainActivity;
import de.fau.cs.mad.fablab.android.view.common.binding.ViewCommandBinding;

public class FloatingFablabButton implements FloatingFablabButtonViewModel.Listener{

    @Inject
    FloatingFablabButtonViewModel viewModel;

    @Bind(R.id.shopping_cart_FAM)
    FloatingActionMenu actionMenu;

    @Bind(R.id.scan_FAB)
    FloatingActionButton scanButton;
    @Bind(R.id.search_FAB)
    FloatingActionButton searchButton;

    public FloatingFablabButton(MainActivity activity, View v){
        activity.inject(this);
        ButterKnife.bind(this, v);

        viewModel.setListener(this);
        new ViewCommandBinding().bind(scanButton, viewModel.getStartBarcodeScannerCommand());
        new ViewCommandBinding().bind(searchButton, viewModel.getStartProductSearchCommand());
    }

    @Override
    public void onFloatingButtonClicked() {
        actionMenu.toggle(true);
    }

    public void setVisibility(boolean visible) {
        actionMenu.setVisibility(visible ? View.VISIBLE : View.GONE);
    }
}
