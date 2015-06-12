package de.fau.cs.mad.fablab.android.view.common.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.LinkedList;
import java.util.List;

import de.fau.cs.mad.fablab.android.view.common.binding.Binding;
import de.fau.cs.mad.fablab.android.view.common.binding.ViewCommandBinding;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;

/***
 * Base class for ViewHolders. Extends RecyclerView ViewHolder
 * @param <ListType>
 */
public abstract class BaseViewHolder<ListType> extends RecyclerView.ViewHolder{

    List<Binding> bindings;
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
    public abstract void setData(ListType data);

    public final void bindViewToCommand(View view, Command command){
        ViewCommandBinding binding = new ViewCommandBinding(view, command);
        bindings.add(binding);
    }
}