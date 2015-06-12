package de.fau.cs.mad.fablab.android.view.common.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.LinkedList;
import java.util.List;

import de.fau.cs.mad.fablab.android.view.common.binding.BaseBinding;
import de.fau.cs.mad.fablab.android.view.common.binding.Bindable;
import de.fau.cs.mad.fablab.android.view.common.binding.ViewCommandBinding;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;

/***
 * Base class for ViewHolders. Extends RecyclerView ViewHolder
 * @param <ViewModelType>
 */
public abstract class BaseViewHolder<ViewModelType> extends RecyclerView.ViewHolder implements Bindable {

    List<BaseBinding> bindings;
    /***
     * Protected constructor since we only want derived classes
     * @param itemView
     */
    protected BaseViewHolder(View itemView) {
        super(itemView);
        this.bindings = new LinkedList<>();
    }

    /***
     * Sets the data for a ViewHolder
     * @param data
     */
    public abstract void setViewModel(ViewModelType data);

    public final void bindViewToCommand(View view, Command command){
        ViewCommandBinding binding = new ViewCommandBinding(view, command);
        bindings.add(binding);
    }

    @Override
    public void bindRecyclerView(RecyclerView recycler, Command command) {
        //TODO i dont think we want a recyclerview inside a viewHolder
        throw new UnsupportedOperationException();
    }
}