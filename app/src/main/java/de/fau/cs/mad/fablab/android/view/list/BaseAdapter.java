package de.fau.cs.mad.fablab.android.view.list;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import de.fau.cs.mad.fablab.android.viewmodel.BaseAdapterViewModel;
import de.fau.cs.mad.fablab.android.viewmodel.common.ObservableArrayList;

/***
 * This class represents the base class for all our RecyclerView Adapters
 * @param <ViewHolderType> The ViewHolder we want to use (must extend BaseViewHolder)
 */
public abstract class BaseAdapter<ViewHolderType extends BaseViewHolder> extends RecyclerView.Adapter<ViewHolderType>{

    //holds the data displayed by this adapter
    protected final BaseAdapterViewModel viewModel;

    /***
     * We only want derived classes
     */
    protected BaseAdapter(BaseAdapterViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public abstract ViewHolderType onCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public final void onBindViewHolder(ViewHolderType holder, int position) {
        holder.setData(viewModel.getData().get(position));
    }

    @Override
    public final int getItemCount() {
        return viewModel.getData().size();
    }
}
