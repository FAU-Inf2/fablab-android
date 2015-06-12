package de.fau.cs.mad.fablab.android.view.fragments;

import android.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.LinkedList;
import java.util.List;

import de.fau.cs.mad.fablab.android.view.binding.Bindable;
import de.fau.cs.mad.fablab.android.view.binding.Binding;
import de.fau.cs.mad.fablab.android.view.binding.RecyclerViewCommandBinding;
import de.fau.cs.mad.fablab.android.view.binding.ViewCommandBinding;
import de.fau.cs.mad.fablab.android.viewmodel.common.Command;

public abstract class BaseDialogFragment extends DialogFragment implements Bindable{

    //to hold all bindings declared in this instance
    private List<Binding> bindings;

    protected BaseDialogFragment() {
        bindings = new LinkedList<>();
    }

    public final void bindViewToCommand(View view, Command command)
    {
        ViewCommandBinding binding = new ViewCommandBinding(view, command);
        bindings.add(binding);
    }

    @Override
    public void bindRecyclerView(RecyclerView recycler, Command command) {
        RecyclerViewCommandBinding binding = new RecyclerViewCommandBinding(recycler, command);
        bindings.add(binding);
    }
}
