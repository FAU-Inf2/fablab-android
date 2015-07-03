package de.fau.cs.mad.fablab.android.view.fragments.productsearch;

import com.pedrogomez.renderers.AdapteeCollection;
import com.pedrogomez.renderers.ListAdapteeCollection;


import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.model.ProductModel;
import de.fau.cs.mad.fablab.android.viewmodel.common.ObservableArrayList;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.fau.cs.mad.fablab.rest.core.Product;

public class ProductSearchFragmentViewModel implements ObservableArrayList.Listener<Product> {
    ProductModel mModel;
    Listener mListener;

    private ListAdapteeCollection<ProductSearchViewModel> mProductSearchViewModelCollection;
    private HashMap<Product, ProductSearchViewModel> mProductSearchViewModels;

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
        mProductSearchViewModelCollection = new ListAdapteeCollection<>();
        mProductSearchViewModels = new HashMap<>();

    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public Command<String> getSearchCommand() {
        return searchCommand;
    }


    @Override
    public void onItemAdded(Product newItem) {
        ProductSearchViewModel viewModel = new ProductSearchViewModel(newItem);
        mProductSearchViewModelCollection.add(viewModel);
        mProductSearchViewModels.put(newItem, viewModel);
        mSearchState = false;
        if (mListener != null) {
            mListener.onDataChanged();
            mListener.onSearchStateChanged();
        }
    }

    @Override
    public void onItemRemoved(Product removedItem) {
        mProductSearchViewModelCollection.remove(mProductSearchViewModels.get(removedItem));
        mProductSearchViewModels.remove(removedItem);
        if (mListener != null) {
            mListener.onDataChanged();
        }
    }

    public AdapteeCollection<ProductSearchViewModel> getProductSearchViewModelCollection() {
        return mProductSearchViewModelCollection;
    }

    public boolean getSearchState() {
        return mSearchState;
    }

    public void initialize() {
        if (mListener != null) {
            for (Product product : mModel.getProducts()) {
                mProductSearchViewModelCollection.add(new ProductSearchViewModel(product));
            }
            mListener.onDataChanged();
        }
    }

    public interface Listener{
        void onDataChanged();

        void onSearchStateChanged();
    }
}
