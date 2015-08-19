package de.fau.cs.mad.fablab.android.view.fragments.productsearch;

import android.os.Bundle;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.model.MailModel;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.fau.cs.mad.fablab.rest.core.Product;

public class ProductDialogFragmentViewModel {

    public static final String KEY_PRODUCT = "key_product";

    private Listener mListener;
    private Product mProduct;
    private MailModel mMailModel;

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
    public ProductDialogFragmentViewModel(MailModel mailModel) {
        mMailModel = mailModel;
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
        String loc = mProduct.getLocation();
        // delete spaces in location string around brackets  "fau fablab / Lagerort"
        loc = loc.replace(" / ", "/" );
        // and replace the rest of the spaces to avoid errors on server side query string parsing
        loc = loc.replace(" ", "_");

        return loc;
    }

    public void initialize(Bundle arguments) {
        mProduct = (Product) arguments.getSerializable(KEY_PRODUCT);
    }

    public String getMailAddress() {
        return mMailModel.getFeedbackMailAddress();
    }

    public interface Listener {
        void onAddToCart();

        void onOutOfStock();

        void onShowLocation();
    }
}
