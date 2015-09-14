package de.fau.cs.mad.fablab.android.view.fragments.categorysearch;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;

import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.view.common.fragments.BaseDialogFragment;
import de.fau.cs.mad.fablab.rest.core.Category;

public class CategoryDialogFragment extends BaseDialogFragment {

    @Bind(R.id.container_tree_view)
    RelativeLayout treeViewContainer;

    @Inject
    CategoryDialogFragmentViewModel mViewModel;


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

        initializeCategoryTree();
    }

    private void initializeCategoryTree()
    {
        List<Category> roots = mViewModel.getRootCategories();
        HashMap<Long, Category> children = mViewModel.getChildrenCategories();

        TreeNode root = TreeNode.root();
        for(Category c : roots)
        {
            TreeNode node = new TreeNode(c);
            depthFirstSearch(node, c, children);
            root.addChild(node);
        }

        AndroidTreeView tView = new AndroidTreeView(getActivity(), root);
        treeViewContainer.addView(tView.getView());
    }

    private void depthFirstSearch(TreeNode node, Category category, HashMap<Long, Category> children)
    {
        System.out.println(category.getName());
        List<Long> childrenID = category.getCategories();
        if(childrenID.isEmpty())
        {
            return;
        }
        for(Long id : childrenID)
        {
            Category child = children.get(id);
            TreeNode childNode = new TreeNode(child);
            depthFirstSearch(childNode, child, children);
            node.addChild(childNode);
        }
    }
}
