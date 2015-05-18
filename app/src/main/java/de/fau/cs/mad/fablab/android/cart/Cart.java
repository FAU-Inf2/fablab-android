package de.fau.cs.mad.fablab.android.cart;

import android.content.Context;

import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.util.List;

import de.fau.cs.mad.fablab.android.db.DatabaseHelper;
import de.fau.cs.mad.fablab.rest.core.CartEntry;
import de.fau.cs.mad.fablab.rest.core.Product;

public enum Cart {
    MYCART;

    private List<CartEntry> products;
    private RuntimeExceptionDao<CartEntry, Long> dao;

    Cart(){
    }

    // initialization of the db and the cart
    // always call this method from the starting activity
    public void init(Context context){
        dao = DatabaseHelper.getHelper(context).getCartEntryDao();
        products = dao.queryForAll();
    }

    public List<CartEntry> getProducts(){
        return products;
    }

    // update CartEntry
    public void updateEntry(CartEntry entry){
        for(CartEntry temp : products){
            if(temp.getProductId() == entry.getProductId()){
                temp.setAmount(entry.getAmount());
                dao.update(temp);
            }
        }
    }

    // remove CartEntry
    public boolean removeEntry(CartEntry entry){
        for(int i=0; i<products.size(); i++){
            if(products.get(i).getProductId() == entry.getProductId()){
                dao.delete(entry);
                products.remove(i);
                return true;
            }
        }

        return false;
    }

    // add product to cart
    public void addProduct(Product product){
        // update existing cart entry
        for(CartEntry temp : products){
            if (temp.getProductId() == product.getProductId()){
                temp.setAmount(temp.getAmount() + 1);
                dao.update(temp);
                return;
            }
        }

        // create new one
        CartEntry new_entry = new CartEntry(product,1);
        dao.create(new_entry);
        products.add(new_entry);
    }

    // return total price
    public double totalPrice(){
        double total = 0;
        for(int i=0;i<products.size();i++){
            total += products.get(i).getPrice()*products.get(i).getAmount();
        }

        if (total == 0){
            return total;
        }else{
            return (total/100);
        }
    }
}
