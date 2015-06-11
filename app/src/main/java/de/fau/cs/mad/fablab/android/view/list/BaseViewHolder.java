package de.fau.cs.mad.fablab.android.view.list;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/***
 * Base class for ViewHolders. Extends RecyclerView ViewHolder
 * @param <ListType>
 */
public abstract class BaseViewHolder<ListType> extends RecyclerView.ViewHolder{

    /***
     * Protected constructor since we only want derived classes
     * @param itemView
     */
    protected BaseViewHolder(View itemView) {
        super(itemView);
    }

    /***
     * Sets the data for a ViewHolder
     * @param data
     */
    abstract void setData(ListType data);
}