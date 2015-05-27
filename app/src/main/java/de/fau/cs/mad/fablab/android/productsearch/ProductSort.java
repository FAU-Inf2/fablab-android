package de.fau.cs.mad.fablab.android.productsearch;

import java.util.Comparator;
import de.fau.cs.mad.fablab.rest.core.Product;

public class ProductSort implements Comparator<Product> {

    @Override
    public int compare(Product p1, Product p2) {
        return p1.getName().compareTo(p2.getName());
    }

}