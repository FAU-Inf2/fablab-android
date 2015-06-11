package de.fau.cs.mad.fablab.android.view.binding;

import android.support.v7.widget.RecyclerView;

import de.fau.cs.mad.fablab.android.view.list.BaseAdapter;
import de.fau.cs.mad.fablab.android.viewmodel.common.ObservableArrayList;

public class RecyclerViewAdapterBinding extends Binding{

    private static final String LOG_TAG = "RecyclerViewAdapterBinding";

    public RecyclerViewAdapterBinding(RecyclerView recyclerView, ObservableArrayList list, BaseAdapter adapter){
        list.setListener(adapter);
        recyclerView.setAdapter(adapter);
    }
}
