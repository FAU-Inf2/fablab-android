package de.fau.cs.mad.fablab.android.model;

import com.j256.ormlite.dao.RuntimeExceptionDao;

import de.fau.cs.mad.fablab.android.viewmodel.common.ObservableArrayList;
import de.fau.cs.mad.fablab.rest.core.Product;
import de.fau.cs.mad.fablab.rest.myapi.ProductApi;

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
