package de.fau.cs.mad.fablab.android.view.fragments.categorysearch;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.model.CategoryModel;
import de.fau.cs.mad.fablab.rest.core.Category;

public class CategoryDialogFragmentViewModel {

    @Inject
    CategoryModel mCategoryModel;

    public List<Category> getAllCategories()
    {
        return mCategoryModel.getAllCategories();
    }

    public List<Category> getRootCategories()
    {
        return mCategoryModel.getRootCategories();
    }

    public HashMap<Integer, Category> getChildrenCategories()
    {
        return mCategoryModel.getChildrenCategories();
    }
}
