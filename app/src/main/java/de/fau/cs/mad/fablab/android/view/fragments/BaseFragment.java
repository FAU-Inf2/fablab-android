package de.fau.cs.mad.fablab.android.view.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.LinkedList;
import java.util.List;

import de.fau.cs.mad.fablab.android.view.binding.RecyclerViewAdapterBinding;
import de.fau.cs.mad.fablab.android.view.binding.Binding;
import de.fau.cs.mad.fablab.android.view.binding.ViewCommandBinding;
import de.fau.cs.mad.fablab.android.view.binding.RecyclerViewCommandBinding;
import de.fau.cs.mad.fablab.android.view.list.BaseAdapter;
import de.fau.cs.mad.fablab.android.viewmodel.Command;
import de.fau.cs.mad.fablab.android.viewmodel.common.ObservableArrayList;

/***
 * Base class for all Fragments
 */
public abstract class BaseFragment extends Fragment {

    //to hold all bindings declared in this instance
    private List<Binding> bindings;

    protected BaseFragment() {
        bindings = new LinkedList<>();
    }

    public abstract View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    protected final void bindButtonToCommand(View button, Command command)
    {
        ViewCommandBinding binding = new ViewCommandBinding((Button)button, command);
        bindings.add(binding);
    }

    protected final void bindRecyclerView(RecyclerView recycler, Command command){
        RecyclerViewCommandBinding binding = new RecyclerViewCommandBinding(recycler, command);
        bindings.add(binding);
    }

    protected final void bindAdapterToRecyclerView(RecyclerView recycler, ObservableArrayList list, BaseAdapter adapter){
        RecyclerViewAdapterBinding binding = new RecyclerViewAdapterBinding(recycler, list, adapter);
        bindings.add(binding);
    }


}
