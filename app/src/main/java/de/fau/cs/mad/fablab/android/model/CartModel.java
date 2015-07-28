package de.fau.cs.mad.fablab.android.model;

import android.util.Log;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;

import de.fau.cs.mad.fablab.android.model.entities.Cart;
import de.fau.cs.mad.fablab.android.model.entities.CartEntry;
import de.fau.cs.mad.fablab.android.viewmodel.common.ObservableArrayList;
import de.fau.cs.mad.fablab.rest.core.CartStatus;
import de.fau.cs.mad.fablab.rest.core.Product;

public class CartModel {
    private static final String LOG_TAG = "CartModel";

    private RuntimeExceptionDao<Cart, Long> mCartDao;

    private Cart mCart;
    private ObservableArrayList<CartEntry> mCartEntries;

    public CartModel(RuntimeExceptionDao<Cart, Long> cartDao) {
        mCartDao = cartDao;

        QueryBuilder<Cart, Long> queryBuilder = cartDao.queryBuilder();
        try {
            queryBuilder.where().eq("status", CartStatus.SHOPPING);
            mCart = cartDao.queryForFirst(queryBuilder.prepare());
        } catch (SQLException e) {
            Log.e(LOG_TAG, "Failed to load cart from database", e);
        }

        mCartEntries = new ObservableArrayList<>();
        if (mCart != null) {
            mCartEntries.addAll(mCart.getEntries());
        } else {
            createNewCart();
        }
    }

    private void createNewCart() {
        mCart = new Cart();
        mCartDao.create(mCart);
        mCartDao.refresh(mCart);
        mCartEntries.clear();
    }

    public ObservableArrayList<CartEntry> getCartEntries() {
        return mCartEntries;
    }

    public void addEntry(Product product, double amount) {
        mCartEntries.add(mCart.addEntry(product, amount));
    }

    public void removeEntry(CartEntry entry) {
        mCart.removeEntry(entry);
        mCartEntries.remove(entry);
    }

    public void updateEntry(CartEntry entry) {
        mCart.updateEntry(entry);
    }

    public double getTotalPrice() {
        return mCart.getTotalPrice();
    }

    public void markCartAsPaid() {
        mCart.setStatus(CartStatus.PAID);
        mCartDao.update(mCart);
        createNewCart();
    }
}
