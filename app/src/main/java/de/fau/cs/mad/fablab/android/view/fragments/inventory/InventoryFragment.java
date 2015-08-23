package de.fau.cs.mad.fablab.android.view.fragments.inventory;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.Bind;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.view.common.binding.ViewCommandBinding;
import de.fau.cs.mad.fablab.android.view.common.fragments.BaseFragment;
import de.fau.cs.mad.fablab.rest.core.User;
import de.greenrobot.event.EventBus;

public class InventoryFragment extends BaseFragment implements InventoryFragmentViewModel.Listener{

    @Bind(R.id.button_scan_product_inventory)
    Button mScanButton;
    @Bind(R.id.button_search_product_inventory)
    Button mSearchButton;
    @Bind(R.id.button_show_inventory_inventory)
    Button mShowButton;
    @Bind(R.id.button_delete_inventory_inventory)
    Button mDeleteButton;

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
        new ViewCommandBinding().bind(mDeleteButton, mViewModel.getOnDeleteButtonClickedCommand());
        new ViewCommandBinding().bind(mShowButton, mViewModel.getOnShowInventoryClickedCommand());
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
    public void deletedSuccess()
    {
        Toast.makeText(getActivity(), "Inventar gelöscht", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void deletedFail()
    {
        Toast.makeText(getActivity(), "Fehler beim Inventar löschen aufgetreten", Toast.LENGTH_SHORT).show();
    }

}
