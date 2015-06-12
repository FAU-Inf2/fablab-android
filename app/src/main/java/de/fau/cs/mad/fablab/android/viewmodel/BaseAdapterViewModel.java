package de.fau.cs.mad.fablab.android.viewmodel;

import java.util.List;

import de.fau.cs.mad.fablab.android.viewmodel.common.ObservableArrayList;

public abstract class BaseAdapterViewModel<ListContentType> extends BaseViewModel implements ObservableArrayList.Listener<ListContentType>{

    Listener listener;

    public void setListener(Listener listener){
        this.listener = listener;
    }

    public abstract List<ListContentType> getData();

    @Override
    public void onItemAdded(ListContentType newItem) {
        if(listener != null){
            listener.onListDataChanged();
        }
    }

    @Override
    public void onItemRemoved(ListContentType removedItem) {
        if(listener != null){
            listener.onListDataChanged();
        }
    }

    public interface Listener extends BaseViewModel.Listener{
        void onListDataChanged();
    }
}
