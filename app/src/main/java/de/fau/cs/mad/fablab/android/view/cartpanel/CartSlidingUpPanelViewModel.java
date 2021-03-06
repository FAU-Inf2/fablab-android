package de.fau.cs.mad.fablab.android.view.cartpanel;

import com.pedrogomez.renderers.AdapteeCollection;
import com.pedrogomez.renderers.ListAdapteeCollection;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.model.CartModel;
import de.fau.cs.mad.fablab.android.model.entities.CartEntry;
import de.fau.cs.mad.fablab.android.viewmodel.common.BaseViewModel;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.greenrobot.event.EventBus;

public class CartSlidingUpPanelViewModel extends BaseViewModel<CartEntry> {
    private CartModel mModel;
    private Listener mListener;
    private EventBus mEventBus = EventBus.getDefault();

    private ListAdapteeCollection<CartEntryViewModel> mCartEntryViewModelCollection;
    private CartEntry mLastRemovedEntry;
    private int mLastRemovedEntryPosition;
    private boolean mVisible = true;

    private final Command<Integer> mRemoveCartEntryCommand = new Command<Integer>() {
        @Override
        public void execute(Integer parameter) {
            mLastRemovedEntryPosition = parameter;
            mLastRemovedEntry = mCartEntryViewModelCollection.get(parameter).getCartEntry();
            mCartEntryViewModelCollection.remove((int) parameter);
            mModel.removeEntry(mLastRemovedEntry);
            if (mListener != null) {
                mListener.onItemRemoved();
                mListener.onShowUndoSnackbar();
            }
        }
    };

    private final Command<Void> mUndoRemoveCartEntryCommand = new Command<Void>() {
        @Override
        public void execute(Void parameter) {
            CartEntry entry = new CartEntry(mLastRemovedEntry.getProduct(),
                    mLastRemovedEntry.getAmount());
            //mModel.addEntry(entry);
            mModel.addEntry(entry, mLastRemovedEntryPosition);
        }
    };

    private final Command<Void> mStartCheckoutCommand = new Command<Void>() {
        @Override
        public void execute(Void parameter) {
            if (mListener != null) {
                mListener.onStartCheckout();
            }
        }
    };

    @Inject
    public CartSlidingUpPanelViewModel(CartModel model) {
        mModel = model;
        mModel.getCartEntries().setListener(this);

        mCartEntryViewModelCollection = new ListAdapteeCollection<>();
    }

    public void setListener(Listener listener) {
        mListener = listener;
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
        int position = mLastRemovedEntryPosition >= 0 ? mLastRemovedEntryPosition : getCartEntriesCount() - 1;

        mCartEntryViewModelCollection.add(position, new CartEntryViewModel(newItem));
        mLastRemovedEntryPosition = -1;

        if (mListener != null) {
            mListener.onItemAdded(position);
        }
    }

    @Override
    public void onAllItemsAdded(Collection<? extends CartEntry> collection) {
        for (CartEntry cartEntry : collection) {
            mCartEntryViewModelCollection.add(new CartEntryViewModel(cartEntry));
        }
        if (mListener != null) {
            mListener.onDataPrepared();
        }
    }

    @Override
    public void onItemRemoved(CartEntry removedItem) {
        for (int i = 0; i < mCartEntryViewModelCollection.size(); i++) {
            if (mCartEntryViewModelCollection.get(i).getCartEntry().equals(removedItem)) {
                mCartEntryViewModelCollection.remove(i);
            }
        }
        if (mListener != null) {
            mListener.onItemRemoved();
        }
    }

    @Override
    public void onAllItemsRemoved(List<CartEntry> list) {
        mCartEntryViewModelCollection.clear();
        if (mListener != null) {
            mListener.onDataPrepared();
        }
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

    public void initialize() {
        if (mListener != null) {
            for (CartEntry cartEntry : mModel.getCartEntries()) {
                mCartEntryViewModelCollection.add(new CartEntryViewModel(cartEntry));
            }
            mListener.onDataPrepared();
        }
        mLastRemovedEntryPosition = -1;
    }

    public void pause() {
        mEventBus.unregister(this);
    }

    public void resume() {
        mEventBus.register(this);
    }

    @SuppressWarnings("unused")
    public void onEvent(CartEntryUpdatedEvent event) {
        if (mListener != null) {
            for (int i = 0; i < mCartEntryViewModelCollection.size(); i++) {
                if (mCartEntryViewModelCollection.get(i).getCartEntry().equals(
                        event.getCartEntry())) {
                    mListener.onItemChanged(i);
                }
            }
        }
    }

    public interface Listener {
        void onDataPrepared();

        void onItemAdded(int position);

        void onItemChanged(int position);

        void onItemRemoved();

        void onShowUndoSnackbar();

        void onVisibilityChanged();

        void onStartCheckout();
    }
}
