package de.fau.cs.mad.fablab.android.view.fragments.inventory;

import com.pedrogomez.renderers.AdapteeCollection;
import com.pedrogomez.renderers.ListAdapteeCollection;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.model.InventoryModel;
import de.fau.cs.mad.fablab.android.viewmodel.common.BaseViewModel;
import de.fau.cs.mad.fablab.rest.core.InventoryItem;

public class ShowInventoryFragmentViewModel extends BaseViewModel<InventoryItem> {

    private InventoryModel mModel;
    private Listener mListener;
    private ListAdapteeCollection<InventoryViewModel> mShowInventoryViewModelCollection;

    @Inject
    public ShowInventoryFragmentViewModel(InventoryModel model) {
        mModel = model;
        mModel.getInventoryItems().setListener(this);
        mModel.getInventory();
        mShowInventoryViewModelCollection = new ListAdapteeCollection<>();
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    @Override
    public void onAllItemsAdded(Collection<? extends InventoryItem> collection) {
        addItems(collection);
    }

    private void addItems(Collection<? extends InventoryItem> items) {
        int positionStart = mShowInventoryViewModelCollection.size();
        int count = 0;
        for (InventoryItem newItem : items) {
            mShowInventoryViewModelCollection.add(new InventoryViewModel(newItem));
            count++;
        }
        if (mListener != null && count > 0) {
            mListener.onDataInserted(positionStart, count);
        }
    }

    @Override
    public void onAllItemsRemoved(List<InventoryItem> removedItems) {

        int count = mShowInventoryViewModelCollection.size();
        mShowInventoryViewModelCollection.clear();

        if (mListener != null) {
            mListener.onAllDataRemoved(count);
        }
    }

    public AdapteeCollection<InventoryViewModel> getInventoryViewModelCollection() {
        return mShowInventoryViewModelCollection;
    }

    public interface Listener {
        void onDataInserted(int positionStart, int itemCount);
        void onAllDataRemoved(int itemCount);
    }
}
