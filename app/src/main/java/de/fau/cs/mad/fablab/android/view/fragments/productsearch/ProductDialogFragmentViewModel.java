package de.fau.cs.mad.fablab.android.view.fragments.productsearch;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.fau.cs.mad.fablab.rest.core.Category;
import de.fau.cs.mad.fablab.rest.core.Product;

public class ProductDialogFragmentViewModel {

    public static final String KEY_PRODUCT = "key_product";

    private Listener mListener;
    private Product mProduct;

    private Command<Void> mAddToCartCommand = new Command<Void>() {
        @Override
        public void execute(Void parameter) {
            if(mListener != null) {
                mListener.onAddToCart();
            }
        }
    };
    private Command<Void> mOutOfStockCommand = new Command<Void>() {
        @Override
        public void execute(Void parameter) {
            if(mListener != null) {
                mListener.onOutOfStock();
            }
        }
    };
    private Command<Void> mShowLocationCommand = new Command<Void>() {
        @Override
        public void execute(Void parameter) {
            if(mListener != null) {
                mListener.onShowLocation();
            }
        }
    };

    @Inject
    public ProductDialogFragmentViewModel(){

    }

    public void setListener(Listener listener) {
        this.mListener = listener;
    }

    public void restoreState(Bundle arguments, Bundle savedInstanceState){
        mProduct = (Product)arguments.getSerializable(KEY_PRODUCT);

        //TODO savedInstanceState
    }

    public void saveState(Bundle outState){
        //TODO
    }

    public Product getProduct() {
        return mProduct;
    }

    public String getProductName() {
        return mProduct.getName();
    }

    public boolean hasLocation()
    {
        return true;
        //Todo: activate when server method is finished
//        if( mProduct.hasLocation() == true;
//            return true;
//        else
//            return false;
    }

    //public String getProductLocation() {}


    public boolean isProductZeroPriced() {
        return mProduct.getPrice() == 0.0;
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

    public interface Listener{
        void onAddToCart();
        void onOutOfStock();
        void onShowLocation();
    }
}
