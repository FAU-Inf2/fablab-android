package de.fau.cs.mad.fablab.android.view.fragments.inventory;

import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.view.fragments.barcodescanner.BarcodeScannerFragment;
import de.fau.cs.mad.fablab.rest.core.Product;

public class InventoryBarcodeScannerFragment extends BarcodeScannerFragment {

    @Override
    public void onShowAddToCartDialog(Product product) {
        getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                AddToInventoryDialogFragment.newInstance(product)).addToBackStack(null)
                .commit();
    }
}
