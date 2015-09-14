package de.fau.cs.mad.fablab.android.view.fragments.categorysearch;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.view.common.binding.ViewCommandBinding;
import de.fau.cs.mad.fablab.android.view.common.fragments.BaseDialogFragment;
import de.fau.cs.mad.fablab.rest.core.Category;

public class CategoryDialogFragment extends BaseDialogFragment implements CategoryDialogFragmentViewModel.Listener {

    @Bind(R.id.container_tree_view)
    RelativeLayout treeViewContainer;
    @Bind(R.id.status_bar_tv)
    TextView statusBarTextView;
    @Bind(R.id.button_categorysearch_get_products)
    Button getProductsButton;


    @Inject
    CategoryDialogFragmentViewModel mViewModel;

    private AndroidTreeView mTreeView;
    private String currentCategory;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return inflater.inflate(R.layout.fragment_category_search, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel.setListener(this);

        initializeCategoryTree();

        if (savedInstanceState != null) {
            String state = savedInstanceState.getString("tState");
            if (!TextUtils.isEmpty(state)) {
                mTreeView.restoreState(state);
            }
        }

        new ViewCommandBinding().bind(getProductsButton, mViewModel.getOnGetProductsButtonClickedCommand());
    }

    private void initializeCategoryTree()
    {
        List<Category> roots = mViewModel.getRootCategories();
        HashMap<Long, Category> children = mViewModel.getChildrenCategories();

        TreeNode root = TreeNode.root();
        for(Category c : roots)
        {
            TreeNode node = new TreeNode(new TreeItemHolder.TreeItem(c));
            depthFirstSearch(node, c, children);
            root.addChild(node);
        }

        mTreeView = new AndroidTreeView(getActivity(), root);
        mTreeView.setDefaultViewHolder(TreeItemHolder.class);
        mTreeView.setDefaultAnimation(true);
        mTreeView.setDefaultContainerStyle(R.style.TreeNodeStyleCustom);

        mTreeView.setDefaultNodeClickListener(new TreeNode.TreeNodeClickListener() {
            @Override
            public void onClick(TreeNode treeNode, Object o) {
                currentCategory = treeNode.getValue().toString();
                statusBarTextView.setText(currentCategory);
            }
        });
        treeViewContainer.addView(mTreeView.getView());
    }

    private void depthFirstSearch(TreeNode node, Category category, HashMap<Long, Category> children)
    {
        List<Long> childrenID = category.getCategories();
        if(childrenID.isEmpty())
        {
            return;
        }
        for(Long id : childrenID)
        {
            Category child = children.get(id);
            TreeNode childNode = new TreeNode(new TreeItemHolder.TreeItem(child));
            depthFirstSearch(childNode, child, children);
            node.addChild(childNode);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("tState", mTreeView.getSaveState());
    }

    @Override
    public String getCategory() {
        return currentCategory;
    }

    @Override
    public void onGetProductsButtonClicked() {
        this.dismiss();
    }
}
