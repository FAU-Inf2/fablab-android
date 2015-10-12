package de.fau.cs.mad.fablab.android.model;

import android.util.Log;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;

import de.fau.cs.mad.fablab.android.model.entities.Cart;
import de.fau.cs.mad.fablab.android.model.entities.CartEntry;
import de.fau.cs.mad.fablab.android.model.events.ProductsChangedEvent;
import de.fau.cs.mad.fablab.android.viewmodel.common.ObservableArrayList;
import de.fau.cs.mad.fablab.rest.core.CartStatus;
import de.greenrobot.event.EventBus;

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

        EventBus.getDefault().register(this);
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

    public void addEntry(CartEntry entry) {
        mCartEntries.add(mCart.addEntry(entry));
    }

    public void addEntry(CartEntry entry, int position)
    {
        mCartEntries.add(position, mCart.addEntry(entry));
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

    @SuppressWarnings("unused")
    public void onEventMainThread(ProductsChangedEvent event) {
        for (CartEntry e : mCart.getEntries()) {
            if (e.getProduct() == null) {
                mCart.getEntries().remove(e);
            }
        }
        mCartEntries.clear();
        mCartEntries.addAll(mCart.getEntries());
    }

    public List<Cart> getAllCarts() {
        List<Cart> carts = mCartDao.queryForEq("status", CartStatus.PAID);
        if (mCartEntries.size() > 0) {
            carts.add(mCart);
        }
        return carts;
    }
}
