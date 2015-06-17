package de.fau.cs.mad.fablab.android.view.floatingbutton;

import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.view.fragments.barcodescanner.BarcodeScannerFragment;

public class FloatingFablabButtonViewLauncher {

    @Inject
    Activity mActivity;
    @Inject
    FragmentManager mFragmentManager;

    public void switchToProductSearch() {
        //TODO add view switching logic
        Toast.makeText(mActivity, "Switching to Product search!", Toast.LENGTH_LONG).show();
    }

    public void switchToBarcodeScanner() {
        mFragmentManager.beginTransaction().replace(R.id.fragment_container,
                new BarcodeScannerFragment()).addToBackStack(null).commit();
    }
}
