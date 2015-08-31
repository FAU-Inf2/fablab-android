package de.fau.cs.mad.fablab.android.view.cartpanel;

import de.fau.cs.mad.fablab.android.model.entities.CartEntry;

public class CartEntryClickedEvent {
    private final CartEntry mCartEntry;

    public CartEntryClickedEvent(CartEntry cartEntry) {
        mCartEntry = cartEntry;
    }

    public CartEntry getCartEntry() {
        return mCartEntry;
    }
}
