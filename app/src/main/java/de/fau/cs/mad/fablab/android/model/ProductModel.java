package de.fau.cs.mad.fablab.android.model;

import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.util.List;

import de.fau.cs.mad.fablab.android.viewmodel.common.ObservableArrayList;
import de.fau.cs.mad.fablab.rest.core.Product;
import de.fau.cs.mad.fablab.rest.myapi.ProductApi;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ProductModel {
    private RuntimeExceptionDao<Product, String> mProductDao;
    private ProductApi mProductApi;

    private ObservableArrayList<Product> mProducts;

    public ProductModel(RuntimeExceptionDao<Product, String> productDao, ProductApi productApi) {
        mProductDao = productDao;
        mProductApi = productApi;
        mProducts = new ObservableArrayList<>();

        mProducts.addAll(mProductDao.queryForAll());
    }

    Callback<List<Product>> productSearchCallback = new Callback<List<Product>>() {
        @Override
        public void success(List<Product> products, Response response) {
            for(Product product : products){
                mProducts.add(product);
            }
        }

        @Override
        public void failure(RetrofitError error) {
            //TODO maybe eventbus ?
        }
    };

    public void searchForProduct(String searchString){
        mProducts.clear();
        mProductApi.findByName(searchString, 0, 0, productSearchCallback);
    }

    public ObservableArrayList<Product> getProducts() {
        return mProducts;
    }

    public Product findProductById(String id) {
        for (Product product : mProducts) {
            if (product.getProductId().equals(id)) {
                return product;
            }
        }

        Product product = mProductApi.findByIdSynchronously(id);
        if (product != null) {
            mProductDao.create(product);
            mProducts.add(product);
        }
        return product;
    }
}
