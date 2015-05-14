package de.fau.cs.mad.fablab.android.basket;

import java.util.ArrayList;
import java.util.List;

import de.fau.cs.mad.fablab.rest.core.Product;

// Singleton class for the basket realized with enum
public enum Basket {
    MYBASKET;

    private List<Product> products;
    private List<Integer> quantities;

    Basket(){
        products = new ArrayList<Product>();
        quantities = new ArrayList<Integer>();
    }

    public List<Integer> getQuantities(){
        return this.quantities;
    }

    public List<Product> getProducts(){
        return this.products;
    }

    public void addProduct(Product p){
        for(int i=0;i<products.size();i++){
            if(products.get(i).getId() == p.getId()){
                quantities.set(i,quantities.get(i)+1);
                return;
            }
        }
        products.add(p);
        quantities.add(1);
    }

    public void removeProduct(Product p){
        // remove here
    }

    public void deleteBasket(){
        products = new ArrayList<Product>();
    }
}
