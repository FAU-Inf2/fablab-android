package de.fau.cs.mad.fablab.android.view.fragments.productsearch;


import com.pedrogomez.renderers.AdapteeCollection;
import com.pedrogomez.renderers.ListAdapteeCollection;


import java.text.Collator;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.model.ProductModel;
import de.fau.cs.mad.fablab.android.viewmodel.common.ObservableArrayList;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.fau.cs.mad.fablab.rest.core.Product;

import de.greenrobot.event.EventBus;

public class ProductSearchFragmentViewModel implements ObservableArrayList.Listener<Product> {
    ProductModel mModel;
    Listener mListener;
    EventBus mEventBus;

    private ListAdapteeCollection<ProductSearchViewModel> mProductSearchViewModelCollection;
    private HashMap<Product, ProductSearchViewModel> mProductSearchViewModels;

    boolean mSearchState = false;

    private Command<String> searchCommand = new Command<String>() {
        @Override
        public void execute(String parameter) {
            mSearchState = true;
            mModel.searchForProduct(parameter);
            if(mListener != null) {
                mListener.onSearchStateChanged();
            }
        }
    };


    @Inject
    public ProductSearchFragmentViewModel(ProductModel mModel){
        this.mModel = mModel;
        mEventBus = EventBus.getDefault();
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
        Collections.sort(mProductSearchViewModelCollection, new SortByName());
        if (mListener != null) {
            mListener.onDataChanged();
            mListener.onSearchStateChanged();
        }
    }

    @Override
    public void onAllItemsAdded(Collection<? extends Product> collection) {
        ProductSearchViewModel viewModel;
        for (Product newItem : collection) {
            viewModel = new ProductSearchViewModel(newItem);
            mProductSearchViewModelCollection.add(viewModel);
            mProductSearchViewModels.put(newItem, viewModel);
        }
        mSearchState = false;
        Collections.sort(mProductSearchViewModelCollection, new SortByName());
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
                ProductSearchViewModel viewModel = new ProductSearchViewModel(product);
                mProductSearchViewModelCollection.add(viewModel);
                mProductSearchViewModels.put(product, viewModel);
            }
            Collections.sort(mProductSearchViewModelCollection, new SortByName());
            mListener.onDataChanged();
        }
    }

    public void pause() {
        mEventBus.unregister(this);
    }

    public void resume() {
        mEventBus.register(this);
    }

    @SuppressWarnings("unused")
    public void onEvent(ProductSearchRetrofitErrorEvent event) {
        mSearchState = false;
        if(mListener != null) {
            mListener.onSearchStateChanged();
            mListener.onRetrofitErrorOccurred();
        }
    }

    public interface Listener{
        void onDataChanged();
        void onSearchStateChanged();
        void onRetrofitErrorOccurred();
    }

    class SortByName implements Comparator<ProductSearchViewModel> {

        @Override
        public int compare(ProductSearchViewModel psvm1, ProductSearchViewModel psvm2) {
            Collator collator = Collator.getInstance(Locale.GERMAN);
            collator.setStrength(Collator.SECONDARY);
            return collator.compare(psvm1.getUnformattedName(), psvm2.getUnformattedName());
        }

    }

}
