package de.fau.cs.mad.fablab.android.model;

import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

import de.fau.cs.mad.fablab.android.model.entities.CartEntry;
import de.fau.cs.mad.fablab.android.model.util.CancellableCallback;
import de.fau.cs.mad.fablab.rest.core.CartEntryServer;
import de.fau.cs.mad.fablab.rest.core.CartServer;
import de.fau.cs.mad.fablab.rest.core.CartStatus;
import de.fau.cs.mad.fablab.rest.core.PlatformType;
import de.fau.cs.mad.fablab.rest.myapi.CartApi;
import de.greenrobot.event.EventBus;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CheckoutModel {
    private static final long POLLING_FREQUENCY = 3 * 1000; // 3 seconds

    private CartModel mCartModel;
    private PushModel mPushModel;
    private CartApi mCartApi;
    private EventBus mEventBus = EventBus.getDefault();

    private Handler mCartStatusHandler = new Handler();
    private CartStatusRunnable mCartStatusRunnable = new CartStatusRunnable();
    private CancellableCallback<Response> mCartCreationCallback;
    private CancellableCallback<CartStatus> mCartPollingCallback;

    private CartStatus mCheckoutStatus = CartStatus.SHOPPING;
    private String mCartCode = null;

    public CheckoutModel(CartModel cartModel, CartApi cartApi, PushModel pushModel) {
        mCartModel = cartModel;
        mCartApi = cartApi;
        mPushModel = pushModel;
    }

    public CartStatus getStatus() {
        return mCheckoutStatus;
    }

    public void startCheckout(String cartCode) {
        mCheckoutStatus = CartStatus.WAITING;
        mEventBus.post(CartStatus.WAITING);
        mCartCode = cartCode;

        CartServer cartServer = new CartServer();
        cartServer.setCartCode(cartCode);
        cartServer.setStatus(CartStatus.PENDING);
        cartServer.setPushToken(mPushModel.getPushId());
        cartServer.setPlatformType(PlatformType.ANDROID);
        List<CartEntryServer> cartEntriesServer = new ArrayList<>();
        for (CartEntry entry : mCartModel.getCartEntries()) {
            cartEntriesServer.add(new CartEntryServer(entry.getProduct().getProductId(),
                    cartServer, entry.getAmount()));
        }
        cartServer.setItems(cartEntriesServer);

        mCartCreationCallback = new CancellableCallback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                if (!isCancelled()) {
                    mCheckoutStatus = CartStatus.PENDING;
                    mEventBus.post(CartStatus.PENDING);
                    mCartStatusHandler.postDelayed(mCartStatusRunnable, POLLING_FREQUENCY);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (!isCancelled()) {
                    mCheckoutStatus = CartStatus.FAILED;
                    mEventBus.post(CartStatus.FAILED);
                }
            }
        };
        mCartApi.create(cartServer, mCartCreationCallback);
    }

    private class CartStatusRunnable implements Runnable {
        @Override
        public void run() {
            mCartPollingCallback = new CancellableCallback<CartStatus>() {
                @Override
                public void success(CartStatus cartStatus, Response response) {
                    if (!isCancelled()) {
                        switch (cartStatus) {
                            case CANCELLED:
                                mCheckoutStatus = CartStatus.CANCELLED;
                                mEventBus.post(CartStatus.CANCELLED);
                                break;
                            case PAID:
                                mCartModel.markCartAsPaid();
                                mCheckoutStatus = CartStatus.PAID;
                                mEventBus.post(CartStatus.PAID);
                                break;
                            default:
                                mCartStatusHandler.postDelayed(mCartStatusRunnable,
                                        POLLING_FREQUENCY);
                        }
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    if (!isCancelled()) {
                        mCheckoutStatus = CartStatus.FAILED;
                        mEventBus.post(CartStatus.FAILED);
                    }
                }
            };
            mCartApi.getStatus(mCartCode, mCartPollingCallback);
        }
    }

    public void cancelCheckout() {
        if (mCartCreationCallback != null) {
            mCartCreationCallback.cancel();
        }
        if (mCartPollingCallback != null) {
            mCartPollingCallback.cancel();
        }
        mCartStatusHandler.removeCallbacks(mCartStatusRunnable);
        finishCheckout();
    }

    public void finishCheckout() {
        mCartCreationCallback = null;
        mCartPollingCallback = null;
        mCheckoutStatus = CartStatus.SHOPPING;
        mCartCode = null;
    }
}
