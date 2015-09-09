package de.fau.cs.mad.fablab.android.model.events;

import de.fau.cs.mad.fablab.rest.core.CartStatus;

public class CartStatusPushedEvent {
    private final CartStatus mCartStatus;

    public CartStatusPushedEvent(CartStatus cartStatus) {
        mCartStatus = cartStatus;
    }

    public CartStatus getCartStatus() {
        return mCartStatus;
    }
}
