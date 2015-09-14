package de.fau.cs.mad.fablab.android.view.fragments.productsearch;

import com.pedrogomez.renderers.AdapteeCollection;
import com.pedrogomez.renderers.ListAdapteeCollection;

import java.text.Collator;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.model.AutoCompleteModel;
import de.fau.cs.mad.fablab.android.model.ProductModel;
import de.fau.cs.mad.fablab.android.model.events.NoProductsFoundEvent;
import de.fau.cs.mad.fablab.android.model.events.ProductSearchRetrofitErrorEvent;
import de.fau.cs.mad.fablab.android.viewmodel.common.BaseViewModel;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.fau.cs.mad.fablab.rest.core.Product;

import de.greenrobot.event.EventBus;

public class ProductSearchFragmentViewModel extends BaseViewModel<Product> {

    private ProductModel mProductModel;
    private AutoCompleteModel mAutoCompleteModel;
    private Listener mListener;
    private EventBus mEventBus;
    private boolean mSearchState = false;
    private boolean mIsOrderedByName = true;

    private ListAdapteeCollection<ProductSearchViewModel> mProductSearchViewModelCollection;

    private Command<String> searchCommand = new Command<String>() {
        @Override
        public void execute(String parameter) {
            mSearchState = true;
            mProductModel.searchForProduct(parameter);
            if(mListener != null) {
                mListener.onSearchStateChanged();
            }
        }
    };

    private Command<Void> searchCategoryCommand = new Command<Void>() {
        @Override
        public void execute(Void parameter) {
            if(mListener != null) {
                mListener.onCategorySearchClicked();
            }
        }
    };

    private Command<Void> orderProductsByNameCommand = new Command<Void>() {
        @Override
        public void execute(Void parameter) {
            mIsOrderedByName = true;
            orderItemsByName();
        }
    };

    private Command<Void> orderProductsByPriceCommand = new Command<Void>() {
        @Override
        public void execute(Void parameter) {
            mIsOrderedByName = false;
            orderItemsByPrice();
        }
    };

    @Inject
    public ProductSearchFragmentViewModel(ProductModel productModel, AutoCompleteModel
            autoCompleteModel){
        mProductModel = productModel;
        mAutoCompleteModel = autoCompleteModel;
        mEventBus = EventBus.getDefault();
        mProductModel.getProducts().setListener(this);
        mProductSearchViewModelCollection = new ListAdapteeCollection<>();
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public Command<String> getSearchCommand() {
        return searchCommand;
    }


    public Command<Void> getOrderProductsByNameCommand() {
        return orderProductsByNameCommand;
    }

    public Command<Void> getOrderProductsByPriceCommand() {
        return orderProductsByPriceCommand;
    }

    public Command<Void> getSearchCategoryCommand()
    {
        return searchCategoryCommand;
    }

    private void orderItemsByName() {
        Collections.sort(mProductSearchViewModelCollection, new OrderByName());
        if(mListener != null) {
            mListener.onDataChanged();
            mListener.onProductOrderChanged();
        }
    }

    private void orderItemsByPrice() {
        Collections.sort(mProductSearchViewModelCollection, new OrderByPrice());
        if(mListener != null) {
            mListener.onDataChanged();
            mListener.onProductOrderChanged();
        }
    }

    public boolean getSearchState() {
        return mSearchState;
    }

    public boolean isOrderedByName() {
        return mIsOrderedByName;
    }

    public AdapteeCollection<ProductSearchViewModel> getProductSearchViewModelCollection() {
        return mProductSearchViewModelCollection;
    }

    public String[] getAutoCompleteWords() {
        return mAutoCompleteModel.getAutoCompleteWords();
    }

    public void loadProductNames() {
        mAutoCompleteModel.loadProductNames();
    }

    public void initialize() {
        if (mListener != null) {
            for (Product product : mProductModel.getProducts()) {
                ProductSearchViewModel viewModel = new ProductSearchViewModel(product);
                mProductSearchViewModelCollection.add(viewModel);
            }
            if(mIsOrderedByName) {
                orderItemsByName();
            } else {
                orderItemsByPrice();
            }
            mListener.onDataChanged();
        }
    }

    public void resume() {
        mEventBus.register(this);
    }

    public void pause() {
        mEventBus.unregister(this);
    }

    @Override
    public void onAllItemsAdded(Collection<? extends Product> collection) {
        ProductSearchViewModel viewModel;
        for (Product newItem : collection) {
            viewModel = new ProductSearchViewModel(newItem);
            mProductSearchViewModelCollection.add(viewModel);
        }
        mSearchState = false;
        if(mIsOrderedByName) {
            orderItemsByName();
        } else {
            orderItemsByPrice();
        }
        if (mListener != null) {
            mListener.onDataChanged();
            mListener.onSearchStateChanged();
        }

    }

    @Override
    public void onAllItemsRemoved(List<Product> removedItems) {
        mProductSearchViewModelCollection.clear();
        if (mListener != null) {
            mListener.onDataChanged();
        }
    }

    @SuppressWarnings("unused")
    public void onEvent(ProductSearchRetrofitErrorEvent event) {
        mSearchState = false;
        if(mListener != null) {
            mListener.onSearchStateChanged();
            mListener.onRetrofitErrorOccurred();
        }
    }

    @SuppressWarnings("unused")
    public void onEvent(NoProductsFoundEvent event) {
        mSearchState = false;
        if(mListener != null) {
            mListener.onSearchStateChanged();
            mListener.onNoProductsFound();
        }
    }

    class OrderByName implements Comparator<ProductSearchViewModel> {

        @Override
        public int compare(ProductSearchViewModel psvm1, ProductSearchViewModel psvm2) {
            Collator collator = Collator.getInstance(Locale.GERMAN);
            collator.setStrength(Collator.SECONDARY);
            return collator.compare(psvm1.getUnformattedName(), psvm2.getUnformattedName());
        }

    }

    class OrderByPrice implements Comparator<ProductSearchViewModel> {

        @Override
        public int compare(ProductSearchViewModel psvm1, ProductSearchViewModel psvm2) {
            if(psvm1.getUnformattedPrice() > psvm2.getUnformattedPrice()) {
                return 1;
            } else if(psvm1.getUnformattedPrice() < psvm2.getUnformattedPrice()) {
                return -1;
            } else {
                return 0;
            }
        }

    }

    public interface Listener{
        void onDataChanged();
        void onSearchStateChanged();
        void onRetrofitErrorOccurred();
        void onNoProductsFound();
        void onProductOrderChanged();
        void onCategorySearchClicked();
    }

}
