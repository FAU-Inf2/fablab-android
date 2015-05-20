package de.fau.cs.mad.fablab.android.cart;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.TextView;

import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.rest.core.Product;

public class AddToCartDialog extends DialogFragment {
    public interface DialogListener {
        void onDialogDismissed();
    }

    private DialogListener listener;

    public static AddToCartDialog newInstance(Product product) {
        AddToCartDialog dialog = new AddToCartDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable("product", product);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final Product product = (Product) getArguments().getSerializable("product");
        if (product != null) {
            View v = View.inflate(getActivity(), R.layout.dialog_add_to_cart, null);
            ((TextView) v.findViewById(R.id.add_to_cart_name)).setText(product.getName());
            ((TextView) v.findViewById(R.id.add_to_cart_price)).setText(product.getPrice() + "€ "
                    + getResources().getString(R.string.price_per_unit) + " " + product.getUnit());
            builder.setView(v)
                    .setPositiveButton(R.string.add_to_cart, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            Cart.MYCART.addProduct(product);
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, null);
        } else {
            builder.setMessage(R.string.product_not_found);
            builder.setPositiveButton(android.R.string.ok, null);
        }
        return builder.create();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (listener != null) {
            listener.onDialogDismissed();
        }
    }

    public void setDialogListener(DialogListener listener) {
        this.listener = listener;
    }
}
