package de.fau.cs.mad.fablab.android.model;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;

import de.fau.cs.mad.fablab.android.viewmodel.common.ObservableArrayList;
import de.fau.cs.mad.fablab.rest.core.Cart;
import de.fau.cs.mad.fablab.rest.core.CartEntry;
import de.fau.cs.mad.fablab.rest.core.CartStatus;
import de.fau.cs.mad.fablab.rest.core.Product;

public class CartModel {
    private RuntimeExceptionDao<Cart, Long> mCartDao;

    private Cart mCart;
    private ObservableArrayList<CartEntry> mCartEntries;

    public CartModel(RuntimeExceptionDao<Cart, Long> cartDao) {
        mCartDao = cartDao;

        QueryBuilder<Cart, Long> queryBuilder = cartDao.queryBuilder();
        try {
            queryBuilder.where().eq("status", CartStatus.SHOPPING).or().eq("status",
                    CartStatus.PENDING);
            mCart = cartDao.queryForFirst(queryBuilder.prepare());
        } catch (SQLException e) {
            e.printStackTrace();
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

    public CartStatus getStatus() {
        return mCart.getStatus();
    }

    public double getTotalPrice() {
        return mCart.getTotalPrice();
    }

    public void setCartCode(String cartCode) {
        mCart.setCartCode(cartCode);
        mCartDao.update(mCart);
    }

    public void startCeckout() {
        if (mCartEntries.size() < 1) {
            throw new IllegalStateException();
        }
        mCart.setStatus(CartStatus.PENDING);
        mCartDao.update(mCart);
    }

    public void cancelCheckout() {
        if (mCart.getStatus() != CartStatus.PENDING) {
            throw new IllegalStateException();
        }
        mCart.setStatus(CartStatus.SHOPPING);
        mCartDao.update(mCart);
    }

    public void finishCheckout() {
        if (mCart.getStatus() != CartStatus.PENDING) {
            throw new IllegalStateException();
        }
        mCart.setStatus(CartStatus.PAID);
        mCartDao.update(mCart);
        createNewCart();
    }
}
