package de.fau.cs.mad.fablab.android.view.fragments.projects;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pedrogomez.renderers.Renderer;

import java.text.DecimalFormat;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.view.common.binding.ViewCommandBinding;

public class CartViewModelRenderer extends Renderer<CartViewModel> {

    @Bind(R.id.cart_entry_item_count)
    TextView mItemCountTV;
    @Bind(R.id.cart_entry_cart_price)
    TextView mCartPriceTV;

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
        return layoutInflater.inflate(R.layout.cart_entry_rv, viewGroup, false);
    }

    @Override
    public void render() {

        CartViewModel viewModel = getContent();

        new ViewCommandBinding().bind(getRootView(), viewModel.getCreateProjectFromCartCommand());
        mItemCountTV.setText(Integer.toString(viewModel.getItemCount()));
        mCartPriceTV.setText(new DecimalFormat("00.00").format(viewModel.getCartPrice()));

    }
}
