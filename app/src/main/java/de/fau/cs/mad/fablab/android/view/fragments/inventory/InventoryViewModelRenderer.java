package de.fau.cs.mad.fablab.android.view.fragments.inventory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pedrogomez.renderers.Renderer;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.fau.cs.mad.fablab.android.R;

public class InventoryViewModelRenderer extends Renderer<InventoryViewModel> {

    @Bind(R.id.product_name_inventory_item_entry)
    TextView product_name_tv;
    @Bind(R.id.product_amount_inventory_item_entry)
    TextView product_amount_tv;
    @Bind(R.id.user_name_inventory_item_entry)
    TextView user_name_tv;
    @Bind(R.id.updated_at_inventory_item_entry)
    TextView updated_at_tv;
    @Bind(R.id.product_id_inventory_item_entry)
    TextView product_id_tv;

    @Override
    protected void setUpView(View view)
    {
        ButterKnife.bind(this, view);
    }

    @Override
    protected void hookListeners(View view)
    {

    }

    @Override
    protected View inflate(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        return layoutInflater.inflate(R.layout.inventory_item_entry, viewGroup, false);
    }

    @Override
    public void render() {
        InventoryViewModel viewModel = getContent();

        product_name_tv.setText(viewModel.getName());
        product_amount_tv.setText(Double.toString(viewModel.getAmount()));
        user_name_tv.setText(viewModel.getUser());
        updated_at_tv.setText(viewModel.getUpdatedAt().toString());
        product_id_tv.setText(viewModel.getID());
    }
}
