package de.fau.cs.mad.fablab.android.view.binding;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import de.fau.cs.mad.fablab.android.viewmodel.common.Command;

public interface Bindable {

    void bindViewToCommand(View view, Command command);

    void bindRecyclerView(RecyclerView recycler, Command command);
}
