package de.fau.cs.mad.fablab.android.view.fragments.productsearch;

import android.os.Bundle;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.model.FablabMailModel;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.fau.cs.mad.fablab.rest.core.Category;
import de.fau.cs.mad.fablab.rest.core.Product;

public class ProductDialogFragmentViewModel {

    public static final String KEY_PRODUCT = "key_product";

    private Listener mListener;
    private Product mProduct;
    private FablabMailModel mFablabMailModel;

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
    public ProductDialogFragmentViewModel(FablabMailModel fablabMailModel) {
        mFablabMailModel = fablabMailModel;
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
        if(mProduct.getLocation().equals("unknown location"))
            return false;
        else
            return true;
    }

    public String getProductLocation() {
        //Todo: choose correct getLocation method
        //long categoryloc = mProduct.getCategory().getLocation_id();
        //String cat = mProduct.getCategoryString();
        // id = mProduct.getCategoryId();
        //long locId = mProduct.getLocation_id();
        String loc = mProduct.getLocation();
        loc = loc.replace(" / ", "" );
        loc = loc.replace(" ", "_");

        return loc;
    }

    public void initialize(Bundle arguments) {
        mProduct = (Product) arguments.getSerializable(KEY_PRODUCT);
    }

    public String getMailAddress()
    {
        return mFablabMailModel.getMailAddress();
    }

    public interface Listener {
        void onAddToCart();

        void onOutOfStock();

        void onShowLocation();
    }
}
