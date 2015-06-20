package de.fau.cs.mad.fablab.android.view.fragments.cart;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.InjectView;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.view.common.binding.MenuItemCommandBinding;
import de.fau.cs.mad.fablab.android.view.common.binding.NumberPickerCommandBinding;
import de.fau.cs.mad.fablab.android.view.common.fragments.BaseDialogFragment;
import de.fau.cs.mad.fablab.rest.core.Product;

public class AddToCartDialogFragment extends BaseDialogFragment
        implements AddToCartDialogFragmentViewModel.Listener {
    @InjectView(R.id.add_to_cart_name)
    TextView mNameTextView;
    @InjectView(R.id.add_to_cart_price)
    TextView mPriceTextView;
    @InjectView(R.id.add_to_cart_numberPicker)
    NumberPicker mNumberPicker;
    @InjectView(R.id.add_to_cart_price_total)
    TextView mPriceTotalTextView;

    @Inject
    AddToCartDialogFragmentViewModel mViewModel;

    public static AddToCartDialogFragment newInstance(Product product) {
        AddToCartDialogFragment dialogFragment = new AddToCartDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(AddToCartDialogFragmentViewModel.KEY_PRODUCT, product);
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel.restoreState(getArguments(), savedInstanceState);

        mNameTextView.setText(mViewModel.getName());
        mPriceTextView.setText(getFormattedPrice(mViewModel.getPrice())
                + getString(R.string.currency) + " "
                + getString(R.string.add_to_cart_price_per_unit) + " " + mViewModel.getUnit());

        mNumberPicker.setMinValue(1);
        mNumberPicker.setMaxValue(100);
        mNumberPicker.setValue((int) mViewModel.getAmount());
        mNumberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        new NumberPickerCommandBinding().bind(mNumberPicker, mViewModel.getChangeAmountCommand());

        mPriceTotalTextView.setText(getFormattedPrice(mViewModel.getPriceTotal())
                + getString(R.string.currency));

        mViewModel.setListener(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_add_to_cart, menu);
        new MenuItemCommandBinding().bind(menu.findItem(R.id.action_add_to_cart),
                mViewModel.getAddToCartCommand());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_to_cart, container, false);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mViewModel.saveState(outState);
    }

    @Override
    public void onUpdatePriceTotal(double priceTotal) {
        mPriceTotalTextView.setText(getFormattedPrice(priceTotal) + getString(R.string.currency));
    }

    private String getFormattedPrice(double price) {
        return String.format("%.2f", price);
    }
}
