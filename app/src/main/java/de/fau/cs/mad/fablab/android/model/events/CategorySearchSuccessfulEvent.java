package de.fau.cs.mad.fablab.android.model.events;

import java.util.List;

import de.fau.cs.mad.fablab.rest.core.Product;

public class CategorySearchSuccessfulEvent {
    private final List<Product> mProducts;

    public CategorySearchSuccessfulEvent(List<Product> products) {
        mProducts = products;
    }

    public List<Product> getProducts() {
        return mProducts;
    }
}
