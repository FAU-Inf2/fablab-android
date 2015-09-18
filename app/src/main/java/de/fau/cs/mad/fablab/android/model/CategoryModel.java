package de.fau.cs.mad.fablab.android.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.fau.cs.mad.fablab.android.model.events.CategorySearchSuccessfulEvent;
import de.fau.cs.mad.fablab.android.viewmodel.common.ObservableArrayList;
import de.fau.cs.mad.fablab.rest.core.Category;
import de.fau.cs.mad.fablab.rest.core.Product;
import de.fau.cs.mad.fablab.rest.myapi.CategoryApi;
import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CategoryModel {

    private CategoryApi mCategoryApi;
    private ProductModel mProductModel;
    private List<Category> mAllCategories;
    private List<Category> mRoots;
    private HashMap<Long, Category> mChildren;
    private ObservableArrayList<Product> mProducts;

    private Callback<List<Category>> mGetAllCallback = new Callback<List<Category>>() {
        @Override
        public void success(List<Category> categories, Response response) {
            mAllCategories.clear();
            mAllCategories.addAll(categories);
            separateRootChildren(mAllCategories);
        }

        @Override
        public void failure(RetrofitError error) {
        }
    };

    public CategoryModel(CategoryApi categoryApi, ProductModel productModel) {
        mCategoryApi = categoryApi;
        mProductModel = productModel;
        mAllCategories = new ArrayList<>();
        mRoots = new ArrayList<>();
        mChildren = new HashMap<>();
        mProducts = new ObservableArrayList<>();
        EventBus.getDefault().register(this);
        update();
    }

    public void update()
    {
        mCategoryApi.findAll(mGetAllCallback);
    }

    private void separateRootChildren(List<Category> list)
    {
        mRoots.clear();
        mChildren.clear();
        for(Category c : list)
        {
            if(c.getParent_category_id() == 0)
            {
                mRoots.add(c);
            }
            else
            {
                mChildren.put(c.getCategoryId(), c);
            }
        }
    }

    public List<Category> getAllCategories()
    {
        return mAllCategories;
    }

    public ObservableArrayList<Product> getCategoryProducts()
    {
        return mProducts;
    }

    public List<Category> getRootCategories()
    {
        return mRoots;
    }

    public HashMap<Long, Category> getChildrenCategories()
    {
        return mChildren;
    }

    public void getCategoryProducts(String category)
    {
        mProducts.clear();
        mProductModel.findByCategory(category);
    }

    public void onEvent(CategorySearchSuccessfulEvent event) {
        mProducts.addAll(event.getProducts());
    }
}
