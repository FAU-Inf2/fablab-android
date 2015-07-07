package de.fau.cs.mad.fablab.android.view.fragments.cart;

import com.pedrogomez.renderers.AdapteeCollection;
import com.pedrogomez.renderers.ListAdapteeCollection;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.model.CartModel;
import de.fau.cs.mad.fablab.android.viewmodel.common.ObservableArrayList;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.fau.cs.mad.fablab.rest.core.CartEntry;
import de.greenrobot.event.EventBus;

public class CartSlidingUpPanelViewModel implements ObservableArrayList.Listener<CartEntry> {
    private CartModel mModel;
    private Listener mListener;
    private EventBus mEventBus;

    private ListAdapteeCollection<CartEntryViewModel> mCartEntryViewModelCollection;
    private CartEntry mLastRemovedEntry;
    private boolean mVisible = true;

    private final Command<Integer> mRemoveCartEntryCommand = new Command<Integer>() {
        @Override
        public void execute(Integer parameter) {
            mLastRemovedEntry = mCartEntryViewModelCollection.get(parameter).getCartEntry();
            mCartEntryViewModelCollection.remove((int) parameter);
            mModel.removeEntry(mLastRemovedEntry);
            if (mListener != null) {
                mListener.onDataChanged();
                mListener.onShowUndoSnackbar();
            }
        }
    };

    private final Command<Void> mUndoRemoveCartEntryCommand = new Command<Void>() {
        @Override
        public void execute(Void parameter) {
            mModel.addEntry(mLastRemovedEntry.getProduct(), mLastRemovedEntry.getAmount());
        }
    };

    private final Command<Void> mStartCheckoutCommand = new Command<Void>() {
        @Override
        public void execute(Void parameter) {
            mEventBus.post(new StartCheckoutEvent());
        }
    };

    @Inject
    public CartSlidingUpPanelViewModel(CartModel model) {
        mModel = model;
        mModel.getCartEntries().setListener(this);

        mEventBus = EventBus.getDefault();
        mEventBus.register(this);

        mCartEntryViewModelCollection = new ListAdapteeCollection<>();
    }

    public void setListener(Listener listener) {
        mListener = listener;

        if (mListener != null) {
            for (CartEntry cartEntry : mModel.getCartEntries()) {
                mCartEntryViewModelCollection.add(new CartEntryViewModel(cartEntry, mModel));
            }
            mListener.onDataChanged();
        }
    }

    public Command<Integer> getRemoveCartEntryCommand() {
        return mRemoveCartEntryCommand;
    }

    public Command<Void> getUndoRemoveCartEntryCommand() {
        return mUndoRemoveCartEntryCommand;
    }

    public Command<Void> getStartCheckoutCommand() {
        return mStartCheckoutCommand;
    }

    @Override
    public void onItemAdded(CartEntry newItem) {
        mCartEntryViewModelCollection.add(new CartEntryViewModel(newItem, mModel));
        if (mListener != null) {
            mListener.onDataChanged();
        }
    }

    @Override
    public void onItemRemoved(CartEntry removedItem) {

    }

    public AdapteeCollection<CartEntryViewModel> getCartEntryViewModelCollection() {
        return mCartEntryViewModelCollection;
    }

    public int getCartEntriesCount() {
        return mModel.getCartEntries().size();
    }

    public double getTotalPrice() {
        return mModel.getTotalPrice();
    }

    public boolean isVisible() {
        return mVisible && getCartEntriesCount() != 0;
    }

    public void setVisibility(boolean visible) {
        if (mVisible != visible) {
            mVisible = visible;
            if (mListener != null) {
                mListener.onVisibilityChanged();
            }
        }
    }

    public void pause() {
        mEventBus.unregister(this);
    }

    @SuppressWarnings("unused")
    public void onEvent(CartEntryUpdatedEvent event) {
        if (mListener != null) {
            mListener.onDataChanged();
        }
    }

    public interface Listener {
        void onDataChanged();

        void onShowUndoSnackbar();

        void onVisibilityChanged();
    }
}
