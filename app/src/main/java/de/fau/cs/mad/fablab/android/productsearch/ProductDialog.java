package de.fau.cs.mad.fablab.android.productsearch;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.rest.core.Product;

public class ProductDialog extends DialogFragment {

    public interface ProductDialogListener {
        void onShowLocationClick();
        void onAddToCartClick();
        void onReportClick();
    }

    public static final String      PRODUCT_KEY = "product_key";
    private Product                 product;
    private ProductDialogListener   productDialogListener;

    public static ProductDialog newInstance(Product product) {
        ProductDialog productDialog = new ProductDialog();

        //supply product as an argument
        Bundle arguments = new Bundle();
        arguments.putSerializable(PRODUCT_KEY, product);
        productDialog.setArguments(arguments);

        return productDialog;
    }

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

        //handling for unsaleable products
        //for testing: price > 250 -> unsaleable
        if(product.getPrice() > 250) {
            cart.setTextColor(getActivity().getResources().getColor(R.color
                    .primary_text_disabled_material_light));
            report.setTextColor(getActivity().getResources().getColor(R.color
                    .primary_text_disabled_material_light));
        }

        //set listeners for click events for all products
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productDialogListener.onShowLocationClick();
            }
        });
        //set listeners for click events for saleable products
        if(!(product.getPrice() > 250)) {
            cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productDialogListener.onAddToCartClick();
                }
            });
            report.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productDialogListener.onReportClick();

                }
            });
        }

        return view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        //get a window without title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return dialog;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            productDialogListener = (ProductDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException((activity.toString() +
                    "must implement ProductDialogListener"));
        }
    }


}
