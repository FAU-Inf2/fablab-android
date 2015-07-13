package de.fau.cs.mad.fablab.android.model.events;

import android.support.annotation.NonNull;

import de.fau.cs.mad.fablab.rest.core.CartStatus;

public class CheckoutStatusChangedEvent {
    private final String mCartCode;
    private final CartStatus mCartStatus;

    public CheckoutStatusChangedEvent(@NonNull String cartCode, @NonNull CartStatus cartStatus) {
        mCartCode = cartCode;
        mCartStatus = cartStatus;
    }

    public String getCartCode() {
        return mCartCode;
    }

    public CartStatus getStatus() {
        return mCartStatus;
    }
}
