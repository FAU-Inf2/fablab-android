package de.fau.cs.mad.fablab.android.model;

import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.util.List;

import de.fau.cs.mad.fablab.android.model.events.ProductFoundEvent;
import de.fau.cs.mad.fablab.android.model.events.ProductNotFoundEvent;
import de.fau.cs.mad.fablab.android.model.util.CancellableCallback;
import de.fau.cs.mad.fablab.android.model.events.NoProductsFoundEvent;
import de.fau.cs.mad.fablab.android.model.events.ProductSearchRetrofitErrorEvent;
import de.fau.cs.mad.fablab.android.viewmodel.common.ObservableArrayList;
import de.fau.cs.mad.fablab.rest.core.Product;
import de.fau.cs.mad.fablab.rest.myapi.ProductApi;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import de.greenrobot.event.EventBus;

public class ProductModel {
    private RuntimeExceptionDao<Product, String> mProductDao;
    private ProductApi mProductApi;
    private EventBus mEventBus = EventBus.getDefault();

    private ObservableArrayList<Product> mProducts;

    private CancellableCallback<List<Product>> mProductSearchCallback;

    private Callback<Product> mProductIdSearch = new Callback<Product>() {
        @Override
        public void success(Product product, Response response) {
            mEventBus.post(new ProductFoundEvent(product));
        }

        @Override
        public void failure(RetrofitError error) {
            mEventBus.post(new ProductNotFoundEvent());
        }
    };

    public ProductModel(RuntimeExceptionDao<Product, String> productDao, ProductApi productApi) {
        mProductDao = productDao;
        mProductApi = productApi;
        mProducts = new ObservableArrayList<>();
    }

    public void searchForProduct(String searchString) {
        if (mProductSearchCallback != null) {
            mProductSearchCallback.cancel();
        }
        mProducts.clear();
        mProductSearchCallback = new CancellableCallback<List<Product>>() {
            @Override
            public void success(List<Product> products, Response response) {
                if (!isCancelled()) {
                    if (products.isEmpty()) {
                        EventBus.getDefault().post(new NoProductsFoundEvent());
                    }
                    mProducts.addAll(products);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (!isCancelled()) {
                    mEventBus.post(new ProductSearchRetrofitErrorEvent());
                }
            }
        };
        mProductApi.findByName(searchString, 0, 0, mProductSearchCallback);
    }

    public ObservableArrayList<Product> getProducts() {
        return mProducts;
    }

    public void findProductById(String id) {
        for (Product product : mProducts) {
            if (product.getProductId().equals(id)) {
                mEventBus.post(new ProductFoundEvent(product));
            }
        }

        mProductApi.findById(id, mProductIdSearch);
    }

    public void persist(Product product) {
        mProductDao.createOrUpdate(product);
    }
}