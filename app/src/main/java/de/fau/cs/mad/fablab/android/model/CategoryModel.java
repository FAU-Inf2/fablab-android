package de.fau.cs.mad.fablab.android.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.rest.core.Category;
import de.fau.cs.mad.fablab.rest.myapi.CategoryApi;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CategoryModel {

    private CategoryApi mApi;
    private List<Category> mAllCategories;
    private List<Category> mRoots;
    private HashMap<Long, Category> mChildren;

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

    @Inject
    public CategoryModel(CategoryApi api){
        mApi = api;
        mAllCategories = new ArrayList<>();
        mRoots = new ArrayList<>();
        mChildren = new HashMap<>();
        update();
    }

    public void update()
    {
        mApi.findAll(mGetAllCallback);
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

    public List<Category> getRootCategories()
    {
        return mRoots;
    }

    public HashMap<Long, Category> getChildrenCategories()
    {
        return mChildren;
    }
}
