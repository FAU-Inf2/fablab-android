package de.fau.cs.mad.fablab.android.view.floatingbutton;

import android.app.Activity;
import android.widget.Toast;

import javax.inject.Inject;

public class FloatingFablabButtonViewLauncher {
    @Inject
    Activity activity;

    public void switchToProductSearch() {
        //TODO add view switching logic
        Toast.makeText(activity, "Switching to Product search!", Toast.LENGTH_LONG).show();
    }

    public void switchToBarcodeScanner() {
        //TODO add view switching logic
        Toast.makeText(activity, "Switching to Barcode Scanner!", Toast.LENGTH_LONG).show();
    }
}
