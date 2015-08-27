package de.fau.cs.mad.fablab.android.view.fragments.checkout;

import android.os.Bundle;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.model.CheckoutModel;
import de.fau.cs.mad.fablab.android.view.activities.BackButtonPressedEvent;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.fau.cs.mad.fablab.rest.core.CartStatus;
import de.greenrobot.event.EventBus;

public class CheckoutViewModel {
    @Inject
    CheckoutModel mModel;
    private Listener mListener;
    private EventBus mEventBus = EventBus.getDefault();

    private String mCartCode;

    private final Command<Void> mRetryCommand = new Command<Void>() {
        @Override
        public void execute(Void parameter) {
            mRetryCommand.setIsAvailable(false);
            if (mListener != null) {
                mListener.onShowSpinner();
            }
            mModel.cancelCheckout();
            mModel.startCheckout(mCartCode);
        }
    };

    private final Command<Void> mOkCommand = new Command<Void>() {
        @Override
        public void execute(Void parameter) {
            mModel.finishCheckout();
            if (mListener != null) {
                mListener.onDismiss();
            }
        }
    };

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public Command<Void> getRetryCommand() {
        return mRetryCommand;
    }

    public Command<Void> getOkCommand() {
        return mOkCommand;
    }

    public void initialize(Bundle args) {
        mRetryCommand.setIsAvailable(false);
        mOkCommand.setIsAvailable(false);

        mCartCode = args.getString(CheckoutFragment.KEY_CART_CODE);

        if (mModel.getStatus() == CartStatus.SHOPPING) {
            mModel.startCheckout(mCartCode);
        }

        updateState(mModel.getStatus());
    }

    private void updateState(CartStatus status) {
        if (mListener != null) {
            switch (status) {
                case WAITING:
                    mListener.onShowSpinner();
                    break;
                case PENDING:
                    mListener.onHideSpinner();
                    mListener.onShowPayMessage();
                    break;
                case FAILED:
                    mListener.onHideSpinner();
                    mListener.onShowErrorMessage();
                    mRetryCommand.setIsAvailable(true);
                    break;
                case CANCELLED:
                    mOkCommand.setIsAvailable(true);
                    mListener.onShowCancelledMessage();
                    break;
                case PAID:
                    mOkCommand.setIsAvailable(true);
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

    @SuppressWarnings("unused")
    public void onEvent(CartStatus event) {
        updateState(event);
    }

    @SuppressWarnings("unused")
    public void onEvent(BackButtonPressedEvent event) {
        mModel.cancelCheckout();
        if (mListener != null) {
            mListener.onDismiss();
        }
    }

    public interface Listener {
        void onShowSpinner();

        void onHideSpinner();

        void onShowPayMessage();

        void onShowErrorMessage();

        void onShowCancelledMessage();

        void onShowPaidMessage();

        void onDismiss();
    }
}
