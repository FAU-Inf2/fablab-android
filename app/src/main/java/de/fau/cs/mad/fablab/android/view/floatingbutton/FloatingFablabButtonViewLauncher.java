package de.fau.cs.mad.fablab.android.view.floatingbutton;

import android.app.Activity;
import android.widget.Toast;

public class FloatingFablabButtonViewLauncher {

    Activity activity;

    public FloatingFablabButtonViewLauncher(Activity activity){
        this.activity = activity;
    }

    public void switchToProductSearch() {
        //TODO add view switching logic
        Toast.makeText(activity, "Switching to Product search!", Toast.LENGTH_LONG).show();
    }

    public void switchToBarcodeScanner() {
        //TODO add view switching logic
        Toast.makeText(activity, "Switching to Barcode Scanner!", Toast.LENGTH_LONG).show();
    }
}
