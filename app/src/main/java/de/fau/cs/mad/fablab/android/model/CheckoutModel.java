package de.fau.cs.mad.fablab.android.model;

import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

import de.fau.cs.mad.fablab.android.model.entities.CartEntry;
import de.fau.cs.mad.fablab.android.model.events.CheckoutStatusChangedEvent;
import de.fau.cs.mad.fablab.android.model.util.CancellableCallback;
import de.fau.cs.mad.fablab.rest.core.CartEntryServer;
import de.fau.cs.mad.fablab.rest.core.CartServer;
import de.fau.cs.mad.fablab.rest.core.CartStatus;
import de.fau.cs.mad.fablab.rest.core.PlatformType;
import de.fau.cs.mad.fablab.rest.myapi.CartApi;
import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CheckoutModel {
    private CartModel mCartModel;
    private CartApi mCartApi;
    private PushModel mPushModel;
    private EventBus mEventBus = EventBus.getDefault();

    private Handler mCartStatusHandler = new Handler();
    private CartStatusRunner mCartStatusRunner;
    private CancellableCallback<Response> mCartCreationCallback;

    private CartStatus mCheckoutStatus;
    private String mCartCode;

    public CheckoutModel(CartModel cartModel, CartApi cartApi, PushModel pushModel) {
        mCartModel = cartModel;
        mCartApi = cartApi;
        mPushModel = pushModel;

        mEventBus.register(this);
    }

    public CartStatus getStatus() {
        return mCheckoutStatus;
    }

    public void startCheckout(final String cartCode) {
        mCheckoutStatus = CartStatus.SHOPPING;

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
                    mEventBus.post(CartStatus.PENDING);
                    startPollingCartStatus(cartCode);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (!isCancelled()) {
                    mEventBus.post(CartStatus.FAILED);
                }
            }
        };
        mCartApi.create(cartServer, mCartCreationCallback);

        mCartCode = cartCode;
    }

    private void startPollingCartStatus(String cartCode) {
        mCartStatusRunner = new CartStatusRunner(cartCode);
        mCartStatusHandler.postDelayed(mCartStatusRunner, 5000);
    }

    private void stopPollingCartStatus() {
        mCartStatusHandler.removeCallbacks(mCartStatusRunner);
    }

    private class CartStatusRunner implements Runnable {
        private final long REFRESH_TIME_MILLIS = 3 * 1000; // 3 seconds

        private String mCartCode;
        private boolean mIsCancelled = false;

        public CartStatusRunner(String cartCode) {
            mCartCode = cartCode;
        }

        @Override
        public void run() {
            mCartApi.getStatus(mCartCode, new Callback<CartStatus>() {
                @Override
                public void success(CartStatus cartStatus, Response response) {
                    if (!mIsCancelled) {
                        switch (cartStatus) {
                            case CANCELLED:
                                CheckoutModel.this.cancel();
                                break;
                            case PAID:
                                finish();
                                break;
                            default:
                                mCartStatusHandler.postDelayed(mCartStatusRunner,
                                        REFRESH_TIME_MILLIS);
                        }
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    if (!mIsCancelled) {
                        mEventBus.post(CartStatus.FAILED);
                    }
                }
            });
        }

        public void cancel() {
            mIsCancelled = true;
        }
    }

    private void cancel() {
        mCartCode = null;
        mEventBus.post(CartStatus.CANCELLED);
        mCheckoutStatus = CartStatus.CANCELLED;
        stopPollingCartStatus();
    }

    private void finish() {
        mCartCode = null;
        mCartModel.markCartAsPaid();
        mEventBus.post(CartStatus.PAID);
        mCheckoutStatus = CartStatus.PAID;
        stopPollingCartStatus();
    }

    public void cancelCheckout() {
        if (mCartCreationCallback != null) {
            mCartCreationCallback.cancel();
        }
        if (mCartStatusRunner != null) {
            mCartStatusRunner.cancel();
        }
        mCartCode = null;
        mCheckoutStatus = CartStatus.SHOPPING;
    }

    @SuppressWarnings("unused")
    public void onEvent(CheckoutStatusChangedEvent event) {
        if (event.getCartCode().equals(mCartCode)) {
            switch (event.getStatus()) {
                case CANCELLED:
                    cancel();
                    break;
                case PAID:
                    finish();
                    break;
            }
        }
    }
}
