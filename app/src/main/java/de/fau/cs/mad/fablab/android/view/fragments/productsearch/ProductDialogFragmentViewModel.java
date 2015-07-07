package de.fau.cs.mad.fablab.android.view.fragments.productsearch;

import android.os.Bundle;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.view.fragments.productmap.LocationParser;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.fau.cs.mad.fablab.rest.core.Product;
import de.greenrobot.event.EventBus;

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

    public boolean hasLocation() {
        return LocationParser.getLocation(mProduct.getLocation()) != null;
    }

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
