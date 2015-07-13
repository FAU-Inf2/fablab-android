package de.fau.cs.mad.fablab.android.view.fragments.barcodescanner;

import de.fau.cs.mad.fablab.rest.core.Product;

public class ProductFoundEvent {
    private final Product mProduct;

    public ProductFoundEvent(Product product) {
        mProduct = product;
    }

    public Product getProduct() {
        return mProduct;
    }
}
