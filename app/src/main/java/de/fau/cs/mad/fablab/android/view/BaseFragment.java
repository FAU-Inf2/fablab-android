package de.fau.cs.mad.fablab.android.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

import de.fau.cs.mad.fablab.android.view.binding.Binding;
import de.fau.cs.mad.fablab.android.view.binding.ButtonBinding;
import de.fau.cs.mad.fablab.android.view.binding.RecyclerViewBinding;
import de.fau.cs.mad.fablab.android.viewmodel.Command;

/**
 * A simple {@link Fragment} subclass.
 */
public class BaseFragment extends Fragment {

    private List<Binding> bindings;

    public BaseFragment() {
        bindings = new LinkedList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        return textView;
    }

    protected void bindButton(View button, Command command)
    {
        ButtonBinding binding = new ButtonBinding((Button)button, command);
        bindings.add(binding);
    }

    protected void bindRecyclerView(RecyclerView recycler, Command command){
        RecyclerViewBinding binding = new RecyclerViewBinding(recycler, command);
        bindings.add(binding);
    }


}
