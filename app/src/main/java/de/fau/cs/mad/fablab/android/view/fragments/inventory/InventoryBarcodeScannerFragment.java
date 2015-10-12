package de.fau.cs.mad.fablab.android.view.fragments.inventory;

import android.os.Bundle;

import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.view.activities.MainActivity;
import de.fau.cs.mad.fablab.android.view.common.binding.ScannerViewCommandBinding;
import de.fau.cs.mad.fablab.android.view.fragments.barcodescanner.BarcodeScannerFragment;
import de.fau.cs.mad.fablab.rest.core.Product;
import de.fau.cs.mad.fablab.rest.core.User;

public class InventoryBarcodeScannerFragment extends BarcodeScannerFragment {

    private User mUser;

    public void setUser(User user)
    {
        mUser = user;
    }

    public User getUser()
    {
        return mUser;
    }

    @Override
    public void onResume() {
        super.onResume();
        setDisplayOptions(MainActivity.DISPLAY_TITLE | MainActivity.DISPLAY_NAVDRAWER);
        setNavigationDrawerSelection(R.id.drawer_item_inventory);
        setTitle(R.string.title_scan_product);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel.setListener(this);
        mUser = (User) getArguments().getSerializable(getResources().getString(R.string.key_user));

    }

    @Override
    public void onShowAddToCartDialog(Product product) {
        getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                AddToInventoryDialogFragment.newInstance(product, mUser)).addToBackStack(null)
                .commit();
    }
}
