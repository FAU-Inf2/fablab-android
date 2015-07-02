package de.fau.cs.mad.fablab.android.view.fragments.productsearch;

import com.pedrogomez.renderers.AdapteeCollection;
import com.pedrogomez.renderers.ListAdapteeCollection;


import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.model.ProductModel;
import de.fau.cs.mad.fablab.android.viewmodel.common.ObservableArrayList;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.fau.cs.mad.fablab.rest.core.Product;

public class ProductSearchFragmentViewModel implements ObservableArrayList.Listener<Product> {
    ProductModel mModel;
    Listener mListener;

    private ListAdapteeCollection<ProductSearchViewModel> mProductSearchViewModelCollection;

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
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public Command<String> getSearchCommand() {
        return searchCommand;
    }


    @Override
    public void onItemAdded(Product newItem) {
        mProductSearchViewModelCollection.add(new ProductSearchViewModel(newItem));
        mSearchState = false;
        if (mListener != null) {
            mListener.onDataChanged();
            mListener.onSearchStateChanged();
        }
    }

    @Override
    public void onItemRemoved(Product removedItem) {
        //TODO not working, item should be removed
        mProductSearchViewModelCollection.remove(new ProductSearchViewModel(removedItem));
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
