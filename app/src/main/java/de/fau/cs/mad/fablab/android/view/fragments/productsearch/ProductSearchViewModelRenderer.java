package de.fau.cs.mad.fablab.android.view.fragments.productsearch;

import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pedrogomez.renderers.Renderer;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.view.common.binding.ViewCommandBinding;

public class ProductSearchViewModelRenderer extends Renderer<ProductSearchViewModel> {

    @InjectView(R.id.product_card_view)
    CardView mProductCardView;
    @InjectView(R.id.product_name)
    TextView mProductName;
    @InjectView(R.id.product_detail)
    TextView mProductDetail;
    @InjectView(R.id.product_price)
    TextView mProductPrice;
    @InjectView(R.id.product_unit)
    TextView mProductUnit;

    @Override
    protected void setUpView(View view) {
        ButterKnife.inject(this, view);
    }

    @Override
    protected void hookListeners(View view) {

    }

    @Override
    protected View inflate(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        return layoutInflater.inflate(R.layout.product_entry, viewGroup, false);
    }

    @Override
    public void render() {
        ProductSearchViewModel viewModel = getContent();

        new ViewCommandBinding().bind(getRootView(), viewModel.getShowDialogCommand());

        mProductName.setText(viewModel.getName());
        mProductDetail.setText(viewModel.getNameDetails());
        mProductPrice.setText(viewModel.getPrice());
        mProductUnit.setText(viewModel.getUnit());

        if(viewModel.isProductZeroPriced()){
            mProductName.setTextColor(mProductName.getResources().getColor(R.color
                    .primary_text_disabled_material_light));
            mProductDetail.setTextColor(mProductDetail.getResources().getColor(R.color
                    .primary_text_disabled_material_light));
            mProductPrice.setTextColor(mProductPrice.getResources().getColor(R.color
                    .primary_text_disabled_material_light));
            mProductUnit.setTextColor(mProductUnit.getResources().getColor(R.color
                    .primary_text_disabled_material_light));
        } else {
            mProductName.setTextColor(mProductName.getResources().getColor(R.color
                    .primary_text_default_material_light));
            mProductDetail.setTextColor(mProductDetail.getResources().getColor(R.color
                    .primary_text_default_material_light));
            mProductPrice.setTextColor(mProductPrice.getResources().getColor(R.color
                    .primary_text_default_material_light));
            mProductUnit.setTextColor(mProductUnit.getResources().getColor(R.color
                    .primary_text_default_material_light));
        }
    }
}
