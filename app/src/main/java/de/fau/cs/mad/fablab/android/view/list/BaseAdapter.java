package de.fau.cs.mad.fablab.android.view.list;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import de.fau.cs.mad.fablab.android.viewmodel.common.ObservableArrayList;

/***
 * This class represents the base class for all our RecyclerView Adapters
 * It implements the ObservableArrayList.Listener so it will be notified when the managed content is changed
 * @param <ListContentType> The type which the internal list contains
 * @param <ViewHolderType> The ViewHolder we want to use (must extend BaseViewHolder)
 */
public abstract class BaseAdapter<ListContentType, ViewHolderType extends BaseViewHolder> extends RecyclerView.Adapter<ViewHolderType> implements ObservableArrayList.Listener{

    //holds the data displayed by this adapter
    protected final List<ListContentType> data;

    /***
     * We only want derived classes
     */
    protected BaseAdapter() {
        data = new ArrayList<>();
    }

    @Override
    public abstract ViewHolderType onCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public final void onBindViewHolder(ViewHolderType holder, int position) {
        holder.setData(data.get(position));
    }

    @Override
    public final int getItemCount() {
        return data.size();
    }

    /***
     * Gets called by ObservableArrayList
     * @param newItem The item that was added to the list
     */
    @Override
    public final void onItemAdded(Object newItem) {
        data.add((ListContentType)newItem);
        notifyDataSetChanged();
    }

    /***
     * Gets called by ObservableArrayList
     * @param removedItem The item that was removed from the list
     */
    @Override
    public final void onItemRemoved(Object removedItem) {
        data.remove(removedItem);
        notifyDataSetChanged();
    }
}
