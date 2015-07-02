package de.fau.cs.mad.fablab.android.view.fragments.productsearch;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.model.ProductModel;
import de.fau.cs.mad.fablab.android.viewmodel.common.ObservableArrayList;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.fau.cs.mad.fablab.rest.core.Product;
import de.greenrobot.event.EventBus;

public class ProductSearchFragmentViewModel implements ObservableArrayList.Listener<Product>
{
    ProductModel mModel;
    Listener mListener;

    boolean mSearchState = false;

    private Command<String> searchCommand = new Command<String>() {
        @Override
        public void execute(String parameter) {
            mSearchState = true;
            mModel.searchForProduct(parameter);
            mListener.onSearchStateChanged();
        }
    };


    @Inject
    public ProductSearchFragmentViewModel(ProductModel mModel){
        this.mModel = mModel;
        mModel.getProducts().setListener(this);
    }

    public void setListener(Listener listener) {
        mListener = listener;

        List<ProductSearchViewModel> viewModels = new ArrayList<>();
        for (Product product : mModel.getProducts()) {
            viewModels.add(new ProductSearchViewModel(product));
        }
        mListener.onDataPrepared(viewModels);
    }

    public Command<String> getSearchCommand() {
        return searchCommand;
    }


    @Override
    public void onItemAdded(Product newItem) {
        mListener.onItemAdded(new ProductSearchViewModel(newItem));
        mSearchState = false;
        mListener.onSearchStateChanged();
    }

    @Override
    public void onItemRemoved(Product removedItem) {
        //TODO not working, item should be removed
        mListener.onItemRemoved(new ProductSearchViewModel(removedItem));
    }

    public boolean getSearchState() {
        return mSearchState;
    }

    public interface Listener{
        void onItemAdded(ProductSearchViewModel viewModel);

        void onItemRemoved(ProductSearchViewModel viewModel);

        void onDataPrepared(List<ProductSearchViewModel> viewModels);

        void onSearchStateChanged();
    }
}


