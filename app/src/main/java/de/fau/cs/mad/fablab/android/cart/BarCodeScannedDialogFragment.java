package de.fau.cs.mad.fablab.android.cart;

import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;


public class BarCodeScannedDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Bitte gehen Sie zur Kasse und bezahlen Sie.");
        // Create the AlertDialog object and return it
        return builder.create();
    }

}
