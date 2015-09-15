package de.fau.cs.mad.fablab.android.view.fragments.checkout;

import android.os.Bundle;

import com.pedrogomez.renderers.AdapteeCollection;
import com.pedrogomez.renderers.ListAdapteeCollection;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.model.CartModel;
import de.fau.cs.mad.fablab.android.model.CheckoutModel;
import de.fau.cs.mad.fablab.android.model.entities.CartEntry;
import de.fau.cs.mad.fablab.android.view.activities.BackButtonPressedEvent;
import de.fau.cs.mad.fablab.android.view.cartpanel.CartEntryViewModel;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.fau.cs.mad.fablab.rest.core.CartStatus;
import de.greenrobot.event.EventBus;

public class CheckoutViewModel {
    @Inject
    CheckoutModel mCheckoutModel;
    @Inject
    CartModel mCartModel;

    private Listener mListener;
    private EventBus mEventBus = EventBus.getDefault();

    private String mCartCode;
    private ListAdapteeCollection<CartEntryViewModel> mCartEntryViewModelCollection;

    private final Command<Void> mRetryCommand = new Command<Void>() {
        @Override
        public void execute(Void parameter) {
            mRetryCommand.setIsAvailable(false);
            if (mListener != null) {
                mListener.onShowProgressBar();
            }
            mCheckoutModel.cancelCheckout();
            mCheckoutModel.startCheckout(mCartCode);
        }
    };

    private final Command<Void> mOkCommand = new Command<Void>() {
        @Override
        public void execute(Void parameter) {
            mCheckoutModel.finishCheckout();
            if (mListener != null) {
                mListener.onDismiss();
            }
        }
    };

    public CheckoutViewModel() {
        mCartEntryViewModelCollection = new ListAdapteeCollection<>();
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public Command<Void> getRetryCommand() {
        return mRetryCommand;
    }

    public Command<Void> getOkCommand() {
        return mOkCommand;
    }

    public AdapteeCollection<CartEntryViewModel> getCartEntryViewModelCollection() {
        return mCartEntryViewModelCollection;
    }

    public double getTotalPrice() {
        return mCartModel.getTotalPrice();
    }

    public void initialize(Bundle args) {
        mRetryCommand.setIsAvailable(false);
        mOkCommand.setIsAvailable(false);

        mCartCode = args.getString(CheckoutFragment.KEY_CART_CODE);

        if (mCheckoutModel.getStatus() == CartStatus.SHOPPING) {
            mCheckoutModel.startCheckout(mCartCode);
        }

        for (CartEntry cartEntry : mCartModel.getCartEntries()) {
            mCartEntryViewModelCollection.add(new CartEntryViewModel(cartEntry, false));
        }

        updateState(mCheckoutModel.getStatus());
    }

    private void updateState(CartStatus status) {
        if (mListener != null) {
            switch (status) {
                case WAITING:
                    mListener.onShowProgressBar();
                    break;
                case PENDING:
                    mListener.onHideProgressBar();
                    mListener.onShowCartPreview();
                    break;
                case FAILED:
                    mListener.onHideProgressBar();
                    mListener.onHideCartPreview();
                    mListener.onShowErrorMessage();
                    mRetryCommand.setIsAvailable(true);
                    break;
                case CANCELLED:
                    mOkCommand.setIsAvailable(true);
                    mListener.onHideCartPreview();
                    mListener.onShowCancelledMessage();
                    break;
                case PAID:
                    mOkCommand.setIsAvailable(true);
                    mListener.onHideCartPreview();
                    mListener.onShowPaidMessage();
                    break;
            }
        }
    }

    public void pause() {
        mEventBus.unregister(this);
    }

    public void resume() {
        mEventBus.register(this);
    }

    public void onEvent(CartStatus event) {
        updateState(event);
    }

    @SuppressWarnings("unused")
    public void onEvent(BackButtonPressedEvent event) {
        mCheckoutModel.cancelCheckout();
        if (mListener != null) {
            mListener.onDismiss();
        }
    }

    public interface Listener {
        void onShowProgressBar();

        void onHideProgressBar();

        void onShowCartPreview();

        void onHideCartPreview();

        void onShowErrorMessage();

        void onShowCancelledMessage();

        void onShowPaidMessage();

        void onDismiss();
    }
}
