package de.fau.cs.mad.fablab.android.productsearch;

import android.app.Application;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.cart.CartActivity;
import de.fau.cs.mad.fablab.android.productMap.ProductMapActivity;
import de.fau.cs.mad.fablab.rest.core.Product;

public class ProductDialog extends DialogFragment {

    public static final String      PRODUCT_KEY = "product_key";
    private Product                 product;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get selected product
        product = (Product) getArguments().getSerializable(PRODUCT_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //get gui elements
        View view = inflater.inflate(R.layout.product_dialog, container, false);
        TextView title = (TextView) view.findViewById(R.id.product_dialog_title);
        final TextView location = (TextView) view.findViewById(R.id.product_dialog_location);
        final TextView cart = (TextView) view.findViewById(R.id.product_dialog_cart);
        final TextView report = (TextView) view.findViewById(R.id.product_dialog_report);

        //set product name as title
        title.setText(product.getName());

        //set listeners for click events
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show location
                Intent intent = new Intent(getActivity(), ProductMapActivity.class);
                startActivity(intent);

            }
        });
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add to cart

            }
        });
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //report

            }
        });

        return view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        //get a window without title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return dialog;
    }


}
