package de.fau.cs.mad.fablab.android.view.fragments.categorysearch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.unnamed.b.atv.model.TreeNode;

import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.rest.core.Category;

public class TreeItemHolder extends TreeNode.BaseNodeViewHolder<TreeItemHolder.TreeItem>{

    TextView value_tv;
    ImageView arrow_iv;


    public TreeItemHolder(Context context) {
        super(context);
    }

    @Override
    public View createNodeView(final TreeNode node, TreeItem item) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.node_entry, null, false);

        value_tv = (TextView) view.findViewById(R.id.node_value);
        arrow_iv = (ImageView) view.findViewById(R.id.node_icon);

        value_tv.setText(item.getCategory().getName());
        return view;
    }

    @Override
    public void toggle(boolean activate) {
        arrow_iv.setRotation(arrow_iv.getRotation()-180f);
    }


    public static class TreeItem {
        private Category mCategory;

        public TreeItem(Category category) {
            mCategory = category;
        }

        public Category getCategory()
        {
            return mCategory;
        }

        @Override
        public String toString() {
            return mCategory.getName();
        }
    }
}