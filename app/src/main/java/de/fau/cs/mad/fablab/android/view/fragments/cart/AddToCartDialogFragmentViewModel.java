package de.fau.cs.mad.fablab.android.view.fragments.cart;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.fau.cs.mad.fablab.rest.core.Product;

public class AddToCartDialogFragmentViewModel {
    public static final String KEY_PRODUCT = "product";
    private static final String KEY_AMOUNT = "amount";

    private Product mProduct;
    private double mAmount;

    private Listener mListener;
    @Inject
    FragmentManager mFragmentManager;

    private final Command<Void> mAddToCartCommand = new Command<Void>() {
        @Override
        public void execute(Void parameter) {
            // TODO add product to cart
            mFragmentManager.popBackStack();
        }
    };

    private final Command<Integer> mChangeAmountCommand = new Command<Integer>() {
        @Override
        public void execute(Integer parameter) {
            if (mListener != null) {
                mListener.onUpdatePriceTotal(parameter * getPrice());
            }
            mAmount = parameter;
        }
    };

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public Command<Void> getAddToCartCommand() {
        return mAddToCartCommand;
    }

    public Command<Integer> getChangeAmountCommand() {
        return mChangeAmountCommand;
    }

    public String getName() {
        return mProduct.getName();
    }

    public double getPrice() {
        return mProduct.getPrice();
    }

    public String getUnit() {
        return mProduct.getUnit();
    }

    public double getAmount() {
        return mAmount;
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
        void onUpdatePriceTotal(double priceTotal);
    }
}
