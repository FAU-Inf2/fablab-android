package de.fau.cs.mad.fablab.android.view.fragments.categorysearch;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.unnamed.b.atv.model.TreeNode;

import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.rest.core.Category;

public class TreeItemHolder extends TreeNode.BaseNodeViewHolder<TreeItemHolder.TreeItem>{

    TextView value_tv;
    ImageView arrow_iv;
    RelativeLayout node_rl;


    public TreeItemHolder(Context context) {
        super(context);
    }

    @Override
    public View createNodeView(final TreeNode node, TreeItem item) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.node_entry, null, false);

        value_tv = (TextView) view.findViewById(R.id.node_value);
        arrow_iv = (ImageView) view.findViewById(R.id.node_icon);
        node_rl = (RelativeLayout) view.findViewById(R.id.category_node);

        value_tv.setText(item.getCategory().getName());
        return view;
    }

    @Override
    public void toggle(boolean activate) {
        arrow_iv.setRotation(arrow_iv.getRotation() - 180f);
    }

    public void setActive(boolean active) {
        if(active) {
            node_rl.setBackgroundColor(0xFF607D8B);
            value_tv.setTypeface(null, Typeface.BOLD);
            value_tv.setTextColor(0xFFFFFFFF);
        } else {
            node_rl.setBackgroundColor(0x00FFFFFF);
            value_tv.setTypeface(null, Typeface.NORMAL);
            value_tv.setTextColor(0xFF2D3F43);
        }
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