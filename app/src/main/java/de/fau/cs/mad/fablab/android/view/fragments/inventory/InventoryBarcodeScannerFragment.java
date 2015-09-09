package de.fau.cs.mad.fablab.android.view.fragments.inventory;

import android.os.Bundle;

import de.fau.cs.mad.fablab.android.R;
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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel.setListener(this);
        mUser = (User) getArguments().getSerializable("USER");

        new ScannerViewCommandBinding().bind(mScannerView, mViewModel.getProcessBarcodeCommand());
    }

    @Override
    public void onShowAddToCartDialog(Product product) {
        getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                AddToInventoryDialogFragment.newInstance(product, mUser)).addToBackStack(null)
                .commit();
    }
}
