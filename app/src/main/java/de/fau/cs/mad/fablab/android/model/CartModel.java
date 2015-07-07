package de.fau.cs.mad.fablab.android.model;

import android.os.Handler;
import android.util.Log;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import de.fau.cs.mad.fablab.android.model.events.CheckoutStatusChangedEvent;
import de.fau.cs.mad.fablab.android.viewmodel.common.ObservableArrayList;
import de.fau.cs.mad.fablab.rest.core.Cart;
import de.fau.cs.mad.fablab.rest.core.CartEntry;
import de.fau.cs.mad.fablab.rest.core.CartEntryServer;
import de.fau.cs.mad.fablab.rest.core.CartServer;
import de.fau.cs.mad.fablab.rest.core.CartStatus;
import de.fau.cs.mad.fablab.rest.core.Product;
import de.fau.cs.mad.fablab.rest.myapi.CartApi;
import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CartModel {
    private static final String LOG_TAG = "CartModel";

    private RuntimeExceptionDao<Cart, Long> mCartDao;
    private CartApi mCartApi;
    private PushModel mPushModel;
    private EventBus mEventBus = EventBus.getDefault();
    private Handler mCartStatusHandler = new Handler();
    private Runnable mCartStatusRunner;

    private Cart mCart;
    private ObservableArrayList<CartEntry> mCartEntries;
    private String mCartCode;

    public CartModel(RuntimeExceptionDao<Cart, Long> cartDao, CartApi cartApi, PushModel pushModel) {
        mCartDao = cartDao;
        mCartApi = cartApi;
        mPushModel = pushModel;

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

    public void startCheckout(final String cartCode) {
        if (mCartEntries.size() < 1) {
            throw new IllegalStateException();
        }

        CartServer cartServer = new CartServer();
        cartServer.setCartCode(cartCode);
        cartServer.setStatus(CartStatus.PENDING);
        cartServer.setPushId(mPushModel.getPushId());
        List<CartEntryServer> cartEntriesServer = new ArrayList<>();
        for (CartEntry entry : mCart.getEntries()) {
            cartEntriesServer.add(new CartEntryServer(entry.getProduct().getProductId(),
                    cartServer, entry.getAmount()));
        }
        cartServer.setItems(cartEntriesServer);

        mCartApi.create(cartServer, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                mEventBus.post(CartStatus.PENDING);
                startPollingCartStatus(cartCode);
            }

            @Override
            public void failure(RetrofitError error) {
                mEventBus.post(CartStatus.FAILED);
            }
        });

        mCartCode = cartCode;
    }

    private void startPollingCartStatus(String cartCode) {
        mCartStatusRunner = new CartStatusRunner(cartCode);
        mCartStatusHandler.post(mCartStatusRunner);
    }

    private void stopPollingCartStatus() {
        mCartStatusHandler.removeCallbacks(mCartStatusRunner);
    }

    private class CartStatusRunner implements Runnable {
        private final long REFRESH_TIME_MILLIS = 3 * 1000; // 3 seconds

        private String mCartCode;

        public CartStatusRunner(String cartCode) {
            mCartCode = cartCode;
        }

        @Override
        public void run() {
           mCartApi.getStatus(mCartCode, new Callback<CartStatus>() {
               @Override
               public void success(CartStatus cartStatus, Response response) {
                   switch (cartStatus) {
                       case CANCELLED:
                           cancelCheckout();
                           break;
                       case PAID:
                           finishCheckout();
                           break;
                       default:
                           mCartStatusHandler.postDelayed(mCartStatusRunner, REFRESH_TIME_MILLIS);
                   }
               }

               @Override
               public void failure(RetrofitError error) {
                   mEventBus.post(CartStatus.FAILED);
               }
           });
        }
    }

    private void cancelCheckout() {
        mCartCode = null;
        mEventBus.post(CartStatus.CANCELLED);
        stopPollingCartStatus();
    }

    private void finishCheckout() {
        mCartCode = null;
        mCart.setStatus(CartStatus.PAID);
        mCartDao.update(mCart);
        createNewCart();
        mEventBus.post(CartStatus.PAID);
        stopPollingCartStatus();
    }

    @SuppressWarnings("unused")
    public void onEvent(CheckoutStatusChangedEvent event) {
        if (event.getCartCode().equals(mCartCode)) {
            switch (event.getStatus()) {
                case CANCELLED:
                    cancelCheckout();
                    break;
                case PAID:
                    finishCheckout();
                    break;
            }
        }
    }
}
