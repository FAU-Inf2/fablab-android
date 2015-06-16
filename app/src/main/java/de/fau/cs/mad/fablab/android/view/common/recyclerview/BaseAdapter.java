package de.fau.cs.mad.fablab.android.view.common.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import de.fau.cs.mad.fablab.android.viewmodel.common.BaseAdapterViewModel;
import de.fau.cs.mad.fablab.android.viewmodel.common.BaseViewModel;

/***
 * This class represents the base class for all our RecyclerView Adapters
 * @param <ViewHolderType> The ViewHolder we want to use (must extend BaseViewHolder)
 */
public abstract class BaseAdapter<ContentType, ViewHolderType extends BaseViewHolder, ViewModelType extends BaseViewModel> extends RecyclerView.Adapter<ViewHolderType>{

    //holds the data displayed by this adapter
    protected final BaseAdapterViewModel<ContentType> viewModel;

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
        holder.setViewModelData(viewModel.getData().get(position));
    }

    @Override
    public final int getItemCount() {
        return viewModel.getData().size();
    }
}
