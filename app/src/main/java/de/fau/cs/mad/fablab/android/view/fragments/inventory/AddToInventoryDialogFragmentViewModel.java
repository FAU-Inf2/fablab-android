package de.fau.cs.mad.fablab.android.view.fragments.inventory;

import android.os.Bundle;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.model.ProductModel;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.fau.cs.mad.fablab.rest.core.Product;

public class AddToInventoryDialogFragmentViewModel {

    public static final String KEY_PRODUCT = "product";
    private static final String KEY_AMOUNT = "amount";

    @Inject
    ProductModel mProductModel;

    private Product mProduct;
    private double mAmount;

    private Listener mListener;

    private final Command<Void> mAddToInventoryCommand = new Command<Void>() {
        @Override
        public void execute(Void parameter) {
            //TODO send to server

            System.out.println("Product: " + mProduct.getName() + " with amount: " + mAmount + " send to server");

            if (mListener != null) {
                mListener.onDismiss();
            }
        }
    };

    private final Command<Integer> mChangeAmountCommand = new Command<Integer>() {
        @Override
        public void execute(Integer parameter) {
            mAmount = parameter;
        }
    };


    public void setListener(Listener listener) {
        mListener = listener;
    }

    public String getName() {
        return mProduct.getName();
    }

    public double getAmount() {
        return mAmount;
    }

    public Command<Void> getAddToInventoryCommand()
    {
        return mAddToInventoryCommand;
    }

    public Command<Integer> getChangeAmountCommand() {
        return mChangeAmountCommand;
    }

    public void saveState(Bundle outState) {
        outState.putDouble(KEY_AMOUNT, mAmount);
    }

    public void restoreState(Bundle arguments, Bundle savedInstanceState) {
        mProduct = (Product) arguments.getSerializable(KEY_PRODUCT);
        if (savedInstanceState != null) {
            mAmount = savedInstanceState.getDouble(KEY_AMOUNT);
        } else {
            mAmount = 1;
        }
    }

    public interface Listener {
        void onDismiss();

    }
}
