package de.fau.cs.mad.fablab.android.view.fragments.inventory;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.Bind;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.util.UiUtils;
import de.fau.cs.mad.fablab.android.view.common.binding.ViewCommandBinding;
import de.fau.cs.mad.fablab.android.view.common.fragments.BaseFragment;
import de.fau.cs.mad.fablab.rest.core.User;

public class InventoryFragment extends BaseFragment implements InventoryFragmentViewModel.Listener{

    @Bind(R.id.button_scan_product_inventory)
    Button mScanButton;
    @Bind(R.id.button_search_product_inventory)
    Button mSearchButton;
    @Bind(R.id.button_show_inventory_inventory)
    Button mShowButton;
    @Bind(R.id.button_delete_inventory_inventory)
    Button mDeleteButton;
    @Bind(R.id.button_search_category_inventory)
    Button mSearchCategoryButton;

    @Inject
    InventoryFragmentViewModel mViewModel;

    @Inject
    public InventoryFragment(){

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        UiUtils.hideKeyboard(getActivity());

        mViewModel.setListener(this);

        User user = (User) getArguments().getSerializable(getResources().getString(R.string.key_user));
        mViewModel.setUser(user);

        new ViewCommandBinding().bind(mScanButton, mViewModel.getOnScanButtonClickedCommand());
        new ViewCommandBinding().bind(mSearchButton, mViewModel.getOnSearchButtonClickedCommand());
        new ViewCommandBinding().bind(mDeleteButton, mViewModel.getOnDeleteButtonClickedCommand());
        new ViewCommandBinding().bind(mShowButton, mViewModel.getOnShowInventoryClickedCommand());
        new ViewCommandBinding().bind(mSearchCategoryButton, mViewModel.getOnCategorySearchButtonClickedCommand());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        return inflater.inflate(R.layout.fragment_inventory, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        setDisplayOptions(R.id.drawer_item_inventory, true, false, false);
    }

    @Override
    public void deletedSuccess()
    {
        Toast.makeText(getActivity(), getResources().getString(R.string.inventory_delete_inventory_success), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void deletedFail()
    {
        Toast.makeText(getActivity(), getResources().getString(R.string.inventory_delete_inventory_fail), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void addedSuccess()
    {
        Toast.makeText(getActivity(), getResources().getString(R.string.inventory_add_inventory_success), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void addedFail()
    {
        Toast.makeText(getActivity(), getResources().getString(R.string.inventory_add_inventory_fail), Toast.LENGTH_SHORT).show();
    }

}
