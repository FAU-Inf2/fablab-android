package de.fau.cs.mad.fablab.android.view.cartpanel;

import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pedrogomez.renderers.Renderer;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.util.Formatter;
import de.fau.cs.mad.fablab.android.view.common.binding.ViewCommandBinding;

public class CartEntryViewModelRenderer extends Renderer<CartEntryViewModel> {
    @Bind(R.id.cart_product_card)
    CardView product_cv;
    @Bind(R.id.cart_product_name)
    TextView product_name_tv;
    @Bind(R.id.cart_product_details)
    TextView product_details_tv;
    @Bind(R.id.cart_product_quantity)
    TextView product_amount_tv;
    @Bind(R.id.cart_product_price)
    TextView product_price_tv;

    @Override
    protected void setUpView(View view) {
        ButterKnife.bind(this, view);
    }

    @Override
    protected void hookListeners(View view) {

    }

    @Override
    protected View inflate(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        return layoutInflater.inflate(R.layout.cart_entry, viewGroup, false);
    }

    @Override
    public void render() {
        final CartEntryViewModel viewModel = getContent();

        product_name_tv.setText(viewModel.getProductName());
        product_details_tv.setText(viewModel.getProductDetails());

        if (viewModel.isDecimalAmount()) {
            product_amount_tv.setText(viewModel.getAmount() + " " + viewModel.getUnit());
        } else {
            product_amount_tv.setText((int) viewModel.getAmount() + " " + viewModel.getUnit());
        }

        product_price_tv.setText(Formatter.formatPrice(viewModel.getTotalPrice()));

        new ViewCommandBinding().bind(product_cv, viewModel.getShowDialogCommand());
    }
}
