package de.fau.cs.mad.fablab.android.view.fragments.cart;

import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.pedrogomez.renderers.Renderer;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.util.Formatter;
import de.fau.cs.mad.fablab.android.view.common.binding.SpinnerCommandBinding;
import de.fau.cs.mad.fablab.android.view.common.binding.ViewCommandBinding;

public class CartEntryViewModelRenderer extends Renderer<CartEntryViewModel>
        implements CartEntryViewModel.Listener {
    @InjectView(R.id.cart_product_name)
    TextView product_name_tv;
    @InjectView(R.id.cart_product_quantity)
    TextView product_amount_tv;
    @InjectView(R.id.cart_product_quantity_spinner)
    Spinner product_amount_spinner;
    @InjectView(R.id.cart_product_price)
    TextView product_price_tv;

    @Override
    protected void setUpView(View view) {
        ButterKnife.inject(this, view);
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

        viewModel.setListener(this);

        // make the first word of the product name bold
        String[] name = viewModel.getProductName().split(" ");
        String productName = "<b>" + name[0] + "</b>";
        for (int i = 1; i < name.length; i++) {
            productName += "&nbsp;" + name[i];
        }

        product_name_tv.setText(Html.fromHtml(productName));

        String unit = getRootView().getResources().getString(R.string.cart_product_quantity) +
                "&nbsp;<i>" + viewModel.getUnit() + "</i>:";
        product_amount_tv.setText(Html.fromHtml(unit));

        // set spinner range to change quantity
        List<String> list = new ArrayList<>();
        for (int j = 1; j < viewModel.getAmount() + 100; j++) {
            list.add(String.valueOf(j));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getRootView().getContext(),
                R.layout.cart_entry_quantity_spinner_item, list);
        adapter.setDropDownViewResource(R.layout.cart_entry_quantity_spinner_dropdown);
        product_amount_spinner.setAdapter(adapter);
        product_amount_spinner.setSelection(((int) viewModel.getAmount()) - 1);
        new SpinnerCommandBinding().bind(product_amount_spinner, viewModel.getUpdateAmountCommand());

        new ViewCommandBinding().bind(product_name_tv, viewModel.getTextViewClickedCommand());

        product_price_tv.setText(Formatter.formatPrice(viewModel.getTotalPrice()));
    }

    @Override
    public void onAmountChanged() {
        product_price_tv.setText(Formatter.formatPrice(getContent().getTotalPrice()));
    }

    @Override
    public void onTextViewClicked(){
        if(product_name_tv.getEllipsize() == TextUtils.TruncateAt.MARQUEE){
            product_name_tv.setEllipsize(TextUtils.TruncateAt.END);
        }else{
            product_name_tv.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        }
        product_name_tv.setSelected(true);
    }
}
