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

    private final Command<Void> mAddToCartCommand = new Command<Void>() {
        @Override
        public void execute(Void parameter) {
            if (mAmount != 0) {
                mCartModel.addEntry(mProduct, mAmount);
            }
            if (mListener != null) {
                mListener.onDismiss();
            }
        }
    };

    private final Command<String> mChangeAmountCommand = new Command<String>() {
        @Override
        public void execute(String parameter) {
            double value = "".equals(parameter) ? 0 : Double.parseDouble(parameter);
            double rounding = mProduct.getUom().getRounding();
            mAmount = rounding * Math.ceil(value / rounding);
            if (mListener != null) {
                mListener.onUpdatePriceAndAmount(getPriceTotal(), (value != mAmount) ? mAmount : -1);
            }
        }
    };

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public Command<Void> getAddToCartCommand() {
        return mAddToCartCommand;
    }

    public Command<String> getChangeAmountCommand() {
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

    public double getPriceTotal() {
        return mAmount * getPrice();
    }

    public boolean isDecimalAmount() {
        return mProduct.getUom().getRounding() != 1;
    }

    public void saveState(Bundle outState) {
        outState.putDouble(KEY_AMOUNT, mAmount);
    }

    public void restoreState(Bundle arguments, Bundle savedInstanceState) {
        mProduct = (Product) arguments.getSerializable(KEY_PRODUCT);
        if (savedInstanceState != null) {
            mAmount = savedInstanceState.getDouble(KEY_AMOUNT);
        } else {
            mAmount = 0;
        }
    }

    public interface Listener {
        void onDismiss();

        void onUpdatePriceAndAmount(double priceTotal, double amount);
    }
}
