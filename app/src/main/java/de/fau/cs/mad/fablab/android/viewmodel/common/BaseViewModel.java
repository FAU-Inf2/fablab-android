package de.fau.cs.mad.fablab.android.viewmodel.common;

import java.util.Collection;
import java.util.List;

public abstract class BaseViewModel<T> implements ObservableArrayList.Listener<T> {
    public void onItemAdded(T newItem) {

    }

    public void onAllItemsAdded(Collection<? extends T> collection){

    }

    public void onItemRemoved(T removedItem) {

    }

    public void onAllItemsRemoved(List<T> removedItems) {

    }
}
