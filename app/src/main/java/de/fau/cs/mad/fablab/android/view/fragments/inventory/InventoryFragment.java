package de.fau.cs.mad.fablab.android.view.fragments.inventory;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import javax.inject.Inject;

import butterknife.Bind;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.model.events.NavigationEventBarcodeScannerInventory;
import de.fau.cs.mad.fablab.android.model.events.NavigationEventProductSearchInventory;
import de.fau.cs.mad.fablab.android.view.common.binding.ViewCommandBinding;
import de.fau.cs.mad.fablab.android.view.common.fragments.BaseFragment;
import de.fau.cs.mad.fablab.rest.core.User;
import de.greenrobot.event.EventBus;

public class InventoryFragment extends BaseFragment implements InventoryFragmentViewModel.Listener{

    @Bind(R.id.button_scan_product_inventory)
    Button mScanButton;
    @Bind(R.id.button_search_product_inventory)
    Button mSearchButton;

    @Inject
    InventoryFragmentViewModel mViewModel;

    EventBus mEventBus = EventBus.getDefault();

    @Inject
    public InventoryFragment(){

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel.setListener(this);

        User user = (User) getArguments().getSerializable("USER");
        mViewModel.setUser(user);

        new ViewCommandBinding().bind(mScanButton, mViewModel.getOnScanButtonClickedCommand());
        new ViewCommandBinding().bind(mSearchButton, mViewModel.getOnSearchButtonClickedCommand());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_inventory, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        setDisplayOptions(R.id.drawer_item_inventory, false, false);
    }

    @Override
    public void onScanButtonClicked() {
        mEventBus.post(new NavigationEventBarcodeScannerInventory(mViewModel.getUser()));
    }

    @Override
    public void onSearchButtonClicked() {
        mEventBus.post(new NavigationEventProductSearchInventory(mViewModel.getUser()));
    }
}
