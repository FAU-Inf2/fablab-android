package de.fau.cs.mad.fablab.android.view.fragments.projects;

import de.fau.cs.mad.fablab.android.model.entities.Cart;

public class CartClickedEvent {

    private final Cart mCart;

    public CartClickedEvent(Cart cart)
    {
        mCart = cart;
    }

    public Cart getProject()
    {
        return mCart;
    }
}
