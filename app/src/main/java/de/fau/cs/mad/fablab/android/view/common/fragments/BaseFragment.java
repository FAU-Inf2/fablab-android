package de.fau.cs.mad.fablab.android.view.common.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;
import java.util.List;

import de.fau.cs.mad.fablab.android.view.common.binding.Bindable;
import de.fau.cs.mad.fablab.android.view.common.binding.Binding;
import de.fau.cs.mad.fablab.android.view.common.binding.ViewCommandBinding;
import de.fau.cs.mad.fablab.android.view.common.binding.RecyclerViewCommandBinding;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;

/***
 * Base class for all Fragments
 */
public abstract class BaseFragment extends Fragment implements Bindable{

    //to hold all bindings declared in this instance
    private List<Binding> bindings;

    protected BaseFragment() {
        bindings = new LinkedList<>();
    }

    public abstract View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    public final void bindViewToCommand(View view, Command command)
    {
        ViewCommandBinding binding = new ViewCommandBinding(view, command);
        bindings.add(binding);
    }

    public final void bindRecyclerView(RecyclerView recycler, Command command){
        RecyclerViewCommandBinding binding = new RecyclerViewCommandBinding(recycler, command);
        bindings.add(binding);
    }
}
