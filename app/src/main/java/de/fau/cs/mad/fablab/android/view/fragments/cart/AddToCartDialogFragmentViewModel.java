package de.fau.cs.mad.fablab.android.view.fragments.cart;

import android.os.Bundle;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.model.CartModel;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.fau.cs.mad.fablab.rest.core.Product;

public class AddToCartDialogFragmentViewModel {
    public static final String KEY_PRODUCT = "product";
    private static final String KEY_AMOUNT = "amount";

    @Inject
    CartModel mCartModel;

    private Product mProduct;
    private double mAmount;

    private Listener mListener;

    private final Command<Integer> mAddToCartCommand = new Command<Integer>() {
        @Override
        public void execute(Integer parameter) {
            mCartModel.addEntry(mProduct, mAmount);
            if (mListener != null) {
                mListener.onDismiss();
            }
        }
    };

    private final Command<Integer> mChangeAmountCommand = new Command<Integer>() {
        @Override
        public void execute(Integer parameter) {
            mAmount = parameter;
            if (mListener != null) {
                mListener.onUpdatePriceTotal(getPriceTotal());
            }
        }
    };

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public Command<Integer> getAddToCartCommand() {
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

    public double getPriceTotal() {
        return mAmount * getPrice();
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

        void onUpdatePriceTotal(double priceTotal);
    }
}
