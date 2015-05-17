package de.fau.cs.mad.fablab.android.cart;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.TextView;

import de.fau.cs.mad.fablab.android.R;

public class AddToCartDialog extends DialogFragment {
    public interface DialogListener {
        void onDialogPositiveClick();
        void onDialogNegativeClick();
    }

    private DialogListener listener;

    public static AddToCartDialog newInstance(String name, String unit, double price) {
        AddToCartDialog dialog = new AddToCartDialog();
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        bundle.putString("unit", unit);
        bundle.putDouble("price", price);
        dialog.setArguments(bundle);
        return dialog;
    }

    public static AddToCartDialog newInstance() {
        AddToCartDialog dialog = new AddToCartDialog();
        Bundle bundle = new Bundle();
        bundle.putBoolean("product_not_found", true);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if (!getArguments().getBoolean("product_not_found")) {
            View v = View.inflate(getActivity(), R.layout.dialog_add_to_cart, null);
            String name = getArguments().getString("name");
            String unit = getArguments().getString("unit");
            double price = getArguments().getDouble("price");
            ((TextView) v.findViewById(R.id.add_to_cart_name)).setText(name);
            ((TextView) v.findViewById(R.id.add_to_cart_price)).setText(price + "€"
                    + getResources().getString(R.string.price_per_unit) + unit);
            builder.setView(v)
                    .setPositiveButton(R.string.add_to_cart, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            listener.onDialogPositiveClick();
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            listener.onDialogNegativeClick();
                        }
                    });
        } else {
            builder.setMessage(R.string.product_not_found);
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    listener.onDialogNegativeClick();
                }
            });
        }
        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (DialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException((activity.toString() + "must implement DialogListener"));
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        listener.onDialogNegativeClick();
    }
}
