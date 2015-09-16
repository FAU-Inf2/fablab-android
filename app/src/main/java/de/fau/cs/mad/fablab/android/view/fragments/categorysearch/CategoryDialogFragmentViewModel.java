package de.fau.cs.mad.fablab.android.view.fragments.categorysearch;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.model.CategoryModel;
import de.fau.cs.mad.fablab.android.model.events.CategorySearchProductsEvent;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.fau.cs.mad.fablab.rest.core.Category;
import de.greenrobot.event.EventBus;

public class CategoryDialogFragmentViewModel {

    private CategoryModel mCategoryModel;
    private Listener mListener;
    private EventBus mEventBus = EventBus.getDefault();

    private Command<Void> mOnGetProductsButtonClickedCommand = new Command<Void>() {
        @Override
        public void execute(Void parameter) {
            if(mListener != null)
            {
                mCategoryModel.getCategoryProducts(mListener.getCategory());
                mEventBus.post(new CategorySearchProductsEvent());
                mListener.onGetProductsButtonClicked();
            }
        }
    };

    @Inject
    CategoryDialogFragmentViewModel(CategoryModel categoryModel)
    {
        mCategoryModel = categoryModel;
    }

    public void setListener(Listener listener)
    {
        mListener = listener;
    }

    public Command<Void> getOnGetProductsButtonClickedCommand()
    {
        return mOnGetProductsButtonClickedCommand;
    }

    public List<Category> getAllCategories()
    {
        return mCategoryModel.getAllCategories();
    }

    public List<Category> getRootCategories()
    {
        return mCategoryModel.getRootCategories();
    }

    public HashMap<Long, Category> getChildrenCategories()
    {
        return mCategoryModel.getChildrenCategories();
    }

    public interface Listener{
        String getCategory();
        void onGetProductsButtonClicked();
    }
}
