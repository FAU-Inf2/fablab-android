package de.fau.cs.mad.fablab.android.view.fragments.barcodescanner;

import android.support.v4.app.FragmentManager;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.cart.AddToCartDialog;
import de.fau.cs.mad.fablab.rest.core.Product;

public class BarcodeScannerViewLauncher {
    @Inject
    FragmentManager mFragmentManager;

    public void showAddToCartDialogFragment(Product product) {
        mFragmentManager.beginTransaction().replace(R.id.fragment_container,
                AddToCartDialog.newInstance(product)).addToBackStack(null).commit();
    }
}
