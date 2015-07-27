package de.fau.cs.mad.fablab.android.view.fragments.cart;

import de.fau.cs.mad.fablab.android.model.CartModel;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.fau.cs.mad.fablab.rest.core.CartEntry;
import de.greenrobot.event.EventBus;

public class CartEntryViewModel {
    private CartEntry mCartEntry;
    private CartModel mModel;
    private Listener mListener;

    private final Command<Integer> mUpdateAmountCommand = new Command<Integer>() {
        @Override
        public void execute(Integer parameter) {
            int newAmount = parameter + 1;
            if (((int) mCartEntry.getAmount()) != newAmount) {
                mCartEntry.setAmount(newAmount);
                mModel.updateEntry(mCartEntry);
                if (mListener != null) {
                    mListener.onAmountChanged();
                }
                EventBus.getDefault().post(new CartEntryUpdatedEvent(CartEntryViewModel.this));
            }
        }
    };

    private final Command<Void> mTextViewClickedCommand = new Command<Void>(){
        @Override
        public void execute(Void parameter){
            if (mListener != null){
                mListener.onTextViewClicked();
            }
        }
    };

    public CartEntryViewModel(CartEntry cartEntry, CartModel model) {
        mCartEntry = cartEntry;
        mModel = model;
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public Command<Integer> getUpdateAmountCommand() {
        return mUpdateAmountCommand;
    }

    public Command<Void> getTextViewClickedCommand(){
        return mTextViewClickedCommand;
    }

    public CartEntry getCartEntry() {
        return mCartEntry;
    }

    public String getProductName() {
        return mCartEntry.getProduct().getName();
    }

    public String getUnit() {
        return mCartEntry.getProduct().getUnit();
    }

    public double getAmount() {
        return mCartEntry.getAmount();
    }

    public double getTotalPrice() {
        return mCartEntry.getTotalPrice();
    }

    public interface Listener {
        void onAmountChanged();

        void onTextViewClicked();
    }
}
