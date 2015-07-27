package de.fau.cs.mad.fablab.android.view.fragments.productsearch;

import android.os.Bundle;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.fau.cs.mad.fablab.rest.core.Product;

public class ProductDialogFragmentViewModel {

    public static final String KEY_PRODUCT = "key_product";

    private Listener mListener;
    private Product mProduct;

    private Command<Void> mAddToCartCommand = new Command<Void>() {
        @Override
        public void execute(Void parameter) {
            if (mListener != null) {
                mListener.onAddToCart();
            }
        }
    };
    private Command<Void> mOutOfStockCommand = new Command<Void>() {
        @Override
        public void execute(Void parameter) {
            if (mListener != null) {
                mListener.onOutOfStock();
            }
        }
    };
    private Command<Void> mShowLocationCommand = new Command<Void>() {
        @Override
        public void execute(Void parameter) {
            if (mListener != null) {
                mListener.onShowLocation();
            }
        }
    };

    @Inject
    public ProductDialogFragmentViewModel() {

    }

    public Command<Void> getAddToCartCommand() {
        return mAddToCartCommand;
    }

    public Command<Void> getReportOutOfStockCommand() {
        return mOutOfStockCommand;
    }

    public Command<Void> getShowLocationCommand() {
        return mShowLocationCommand;
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public Product getProduct() {
        return mProduct;
    }

    public String getProductName() {
        return mProduct.getName();
    }

    public boolean isProductZeroPriced() {
        return mProduct.getPrice() == 0.0;
    }

    public boolean hasLocation() {
        return true;
        //Todo: activate when server method is finished
        //return mProduct.hasLocation();
    }

    public String getProductLocation() {
        //Todo: choose correct getLocation method
        return "0820/0815";
    }

    public void initialize(Bundle arguments) {
        mProduct = (Product) arguments.getSerializable(KEY_PRODUCT);
    }

    public interface Listener {
        void onAddToCart();

        void onOutOfStock();

        void onShowLocation();
    }
}
