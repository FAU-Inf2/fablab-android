package de.fau.cs.mad.fablab.android.cart;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.j256.ormlite.dao.RuntimeExceptionDao;

import de.fau.cs.mad.fablab.android.db.DatabaseHelper;
import de.fau.cs.mad.fablab.android.ui.NewsActivity;
import de.fau.cs.mad.fablab.rest.core.Cart;
import de.fau.cs.mad.fablab.rest.core.CartStatusEnum;

public class CanceledDialogFragment extends DialogFragment {

    private Cart cart;
    private RuntimeExceptionDao<Cart, String> cartDao;

    private final static String CART = "CART";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cartDao = DatabaseHelper.getHelper(getActivity().getApplication()).getCartDao();

        Bundle args = getArguments();
        cart = (Cart)args.getSerializable(CART);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Der Bezahlvorgang wurde abgebrochen! Wollen Sie Ihren Warenkorb verwerfen?");
        builder.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                cartDao.delete(cart);
                Intent intent = new Intent(getActivity(), NewsActivity.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Nein", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                cart.setStatus(CartStatusEnum.SHOPPING);
                cartDao.update(cart);
                Intent intent = new Intent(getActivity(), NewsActivity.class);
                startActivity(intent);
            }
        });

        // Create the AlertDialog object and return it
        return builder.create();
    }

}