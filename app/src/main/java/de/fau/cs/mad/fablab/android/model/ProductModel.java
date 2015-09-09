package de.fau.cs.mad.fablab.android.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

import de.fau.cs.mad.fablab.android.model.events.NoProductsFoundEvent;
import de.fau.cs.mad.fablab.android.model.events.ProductFoundEvent;
import de.fau.cs.mad.fablab.android.model.events.ProductNotFoundEvent;
import de.fau.cs.mad.fablab.android.model.events.ProductSearchRetrofitErrorEvent;
import de.fau.cs.mad.fablab.android.model.events.ProductsChangedEvent;
import de.fau.cs.mad.fablab.android.viewmodel.common.ObservableArrayList;
import de.fau.cs.mad.fablab.rest.core.Product;
import de.fau.cs.mad.fablab.rest.myapi.ProductApi;
import de.greenrobot.event.EventBus;
import retrofit.RetrofitError;

public class ProductModel {
    private static final String TAG = "ProductModel";
    private final String KEY_LAST_PRODUCT_UPDATE = "key_last_product_update";

    private final CountDownLatch mCountDownLatch;
    private RuntimeExceptionDao<Product, Long> mProductDao;
    private ProductApi mProductApi;
    private EventBus mEventBus = EventBus.getDefault();
    private SharedPreferences mPreferences;
    private ObservableArrayList<Product> mProducts;

    public ProductModel(RuntimeExceptionDao<Product, Long> productDao, ProductApi productApi,
                        Context context) {
        mCountDownLatch = new CountDownLatch(1);
        mProductDao = productDao;
        mProductApi = productApi;
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        mProducts = new ObservableArrayList<>();
        long timeSinceUpdate = (System.currentTimeMillis() - mPreferences.getLong(
                KEY_LAST_PRODUCT_UPDATE, 0)) / 1000;
        if (timeSinceUpdate > 86400 || mProductDao.countOf() == 0) {
            fetchProducts();
        } else {
            mCountDownLatch.countDown();
        }
    }

    private void fetchProducts() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                List<Product> products = null;
                try {
                    products = mProductApi.findAllSynchronously(0, 0);
                } catch (RetrofitError error) {
                    Log.e(TAG, "Failed to get product list", error);
                    if (mProductDao.countOf() == 0) {
                        long backoff = 1000;
                        while (products == null) {
                            try {
                                Thread.sleep(backoff);
                                products = mProductApi.findAllSynchronously(0, 0);
                            } catch (InterruptedException e) {
                                Log.e(TAG, "Interrupt", e);
                            } catch (RetrofitError e) {
                                Log.e(TAG, "Failed to get product list", e);
                                backoff *= 2;
                            }
                        }
                    } else {
                        mCountDownLatch.countDown();
                        return null;
                    }
                }

                final List<Product> updatedProducts = new ArrayList<>(products);
                updatedProducts.removeAll(mProductDao.queryForAll());
                for (int i = 0; i < updatedProducts.size(); i++) {
                    Product product = updatedProducts.get(i);
                    if (product.getProductId().isEmpty()) {
                        updatedProducts.remove(i);
                        i--;
                    }
                }
                boolean productsUpdated = updatedProducts.size() > 0;
                if (productsUpdated) {
                    mProductDao.callBatchTasks(new Callable<Product>() {
                        @Override
                        public Product call() throws Exception {
                            for (Product product : updatedProducts) {
                                product.setDatabaseId(Long.parseLong(product.getProductId()));
                                mProductDao.createOrUpdate(product);
                            }
                            return null;
                        }
                    });
                }

                final List<Product> removedProducts = mProductDao.queryForAll();
                removedProducts.removeAll(products);
                boolean productsRemoved = removedProducts.size() > 0;
                if (productsRemoved) {
                    mProductDao.callBatchTasks(new Callable<Product>() {
                        @Override
                        public Product call() throws Exception {
                            for (Product product : removedProducts) {
                                product.setDatabaseId(Long.parseLong(product.getProductId()));
                                mProductDao.delete(product);
                            }
                            return null;
                        }
                    });
                }

                if (productsUpdated || productsRemoved) {
                    mEventBus.post(new ProductsChangedEvent());
                }

                mPreferences.edit().putLong(KEY_LAST_PRODUCT_UPDATE, System.currentTimeMillis())
                        .apply();
                mCountDownLatch.countDown();
                return null;
            }
        }.execute((Void) null);
    }

    public ObservableArrayList<Product> getProducts() {
        return mProducts;
    }

    public void searchForProduct(String searchString) {
        mProducts.clear();

        new AsyncTask<String, Void, List<Product>>() {

            @Override
            protected List<Product> doInBackground(String... params) {
                try {
                    mCountDownLatch.await();
                } catch (InterruptedException e) {
                    Log.e(TAG, "Interrupt", e);
                }
                List<Product> result = null;
                try {
                    String[] searchTokens = params[0].split(" ");
                    Where<Product, Long> query = mProductDao.queryBuilder().where().like("name",
                            "%" + searchTokens[0] + "%");
                    for (int i = 1; i < searchTokens.length; i++) {
                        query.and().like("name", "%" + searchTokens[i] + "%");
                    }
                    result = mProductDao.query(query.prepare());
                } catch (SQLException e) {
                    Log.e(TAG, "Query failed", e);
                }
                return result;
            }

            @Override
            protected void onPostExecute(List<Product> result) {
                super.onPostExecute(result);
                if (result == null) {
                    mEventBus.post(new ProductSearchRetrofitErrorEvent());
                } else if (result.isEmpty()) {
                    mEventBus.post(new NoProductsFoundEvent());
                } else {
                    mProducts.addAll(result);
                }
            }
        }.execute(searchString);
    }

    public void findProductById(String id) {
        for (Product product : mProducts) {
            if (product.getProductId().equals(id)) {
                mEventBus.post(new ProductFoundEvent(product));
            }
        }

        new AsyncTask<String, Void, Product>() {

            @Override
            protected Product doInBackground(String... params) {
                try {
                    mCountDownLatch.await();
                } catch (InterruptedException e) {
                    Log.e(TAG, "Interrupt", e);
                }
                Product result = null;
                try {
                    PreparedQuery<Product> query = mProductDao.queryBuilder().where().eq("product_id",
                            params[0]).prepare();
                    result = mProductDao.queryForFirst(query);
                } catch (SQLException e) {
                    Log.e(TAG, "Query failed", e);
                }
                return result;
            }

            @Override
            protected void onPostExecute(Product result) {
                super.onPostExecute(result);
                if (result == null) {
                    mEventBus.post(new ProductNotFoundEvent());
                } else {
                    mEventBus.post(new ProductFoundEvent(result));
                }
            }
        }.execute(id);
    }
}
