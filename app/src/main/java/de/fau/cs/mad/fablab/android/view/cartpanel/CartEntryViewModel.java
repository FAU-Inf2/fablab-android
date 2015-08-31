package de.fau.cs.mad.fablab.android.view.cartpanel;

import de.fau.cs.mad.fablab.android.model.entities.CartEntry;
import de.fau.cs.mad.fablab.android.util.Formatter;

public class CartEntryViewModel {
    private CartEntry mCartEntry;

    public CartEntryViewModel(CartEntry cartEntry) {
        mCartEntry = cartEntry;
    }

    public CartEntry getCartEntry() {
        return mCartEntry;
    }

    public String getProductName() {
        return Formatter.formatProductName(mCartEntry.getProduct().getName())[0];
    }

    public String getProductDetails() {
        return Formatter.formatProductName(mCartEntry.getProduct().getName())[1];
    }

    public String getUnit() {
        return mCartEntry.getProduct().getUnit();
    }

    public boolean isDecimalAmount() {
        return mCartEntry.getProduct().getUom().getRounding() != 1.0;
    }

    public double getAmount() {
        return mCartEntry.getAmount();
    }

    public double getTotalPrice() {
        return mCartEntry.getTotalPrice();
    }
}
