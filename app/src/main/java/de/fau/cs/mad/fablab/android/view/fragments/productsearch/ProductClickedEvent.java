package de.fau.cs.mad.fablab.android.view.fragments.productsearch;

import de.fau.cs.mad.fablab.rest.core.Product;

public class ProductClickedEvent {

    Product mProduct;

    public ProductClickedEvent(Product product){
        this.mProduct = product;
    }

    public Product getProduct(){
        return mProduct;
    }
}
