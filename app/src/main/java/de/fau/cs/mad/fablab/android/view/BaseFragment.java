package de.fau.cs.mad.fablab.android.view;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.fau.cs.mad.fablab.android.view.binding.Binding;
import de.fau.cs.mad.fablab.android.viewmodel.Command;

/**
 * A simple {@link Fragment} subclass.
 */
public class BaseFragment extends Fragment {

    private List<Binding> bindings;

    public BaseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        return textView;
    }

    protected void bindButton(View button, Command command)
    {

    }


}
