package de.fau.cs.mad.fablab.android.view.fragments.inventory;

import com.pedrogomez.renderers.AdapteeCollection;
import com.pedrogomez.renderers.ListAdapteeCollection;

import java.text.Collator;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.model.InventoryModel;
import de.fau.cs.mad.fablab.android.model.events.InventoryGetInventoryEvent;
import de.fau.cs.mad.fablab.android.viewmodel.common.BaseViewModel;
import de.fau.cs.mad.fablab.rest.core.InventoryItem;
import de.greenrobot.event.EventBus;

public class ShowInventoryFragmentViewModel extends BaseViewModel<InventoryItem> {

    private InventoryModel mModel;
    private EventBus mEventBus = EventBus.getDefault();;
    private Listener mListener;
    private ListAdapteeCollection<InventoryViewModel> mShowInventoryViewModelCollection;

    @Inject
    public ShowInventoryFragmentViewModel(InventoryModel model) {
        mModel = model;
        mModel.getInventoryItems().setListener(this);
        mModel.getInventory();
        mEventBus.register(this);
        mShowInventoryViewModelCollection = new ListAdapteeCollection<>();
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    @Override
    public void onAllItemsAdded(Collection<? extends InventoryItem> collection) {
        addItems(collection);
        sortByName();
    }

    private void addItems(Collection<? extends InventoryItem> items) {
        for (InventoryItem newItem : items) {
            mShowInventoryViewModelCollection.add(new InventoryViewModel(newItem));
        }
        if (mListener != null) {
            mListener.onDataChanged();
        }
    }

    private void sortByName() {
        Collections.sort(mShowInventoryViewModelCollection, new Comparator<InventoryViewModel>()
        {
            @Override
            public int compare(InventoryViewModel lhs, InventoryViewModel rhs) {
                Collator collator = Collator.getInstance(Locale.GERMAN);
                collator.setStrength(Collator.SECONDARY);
                return collator.compare(lhs.getName(), rhs.getName());
            }
        });
        if(mListener != null) {
            mListener.onDataChanged();
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

    public int getCollectionSize()
    {
        return mModel.getInventoryItems().size();
    }

    public void onEvent(InventoryGetInventoryEvent event)
    {
        if(mListener != null)
        {
            if(event.getState()) {
                mListener.getSuccess();
            }
            else
            {
                mListener.getFail();
            }
        }
    }

    public interface Listener {
        void onAllDataRemoved(int itemCount);
        void onDataChanged();
        void getSuccess();
        void getFail();
    }
}
