package de.fau.cs.mad.fablab.android.view.fragments.cart;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.Bind;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.model.events.AppBarShowDoorStateEvent;
import de.fau.cs.mad.fablab.android.model.events.AppBarShowTitleEvent;
import de.fau.cs.mad.fablab.android.util.Formatter;
import de.fau.cs.mad.fablab.android.util.UiUtils;
import de.fau.cs.mad.fablab.android.view.activities.BackButtonPressedEvent;
import de.fau.cs.mad.fablab.android.view.common.binding.EditTextCommandBinding;
import de.fau.cs.mad.fablab.android.view.common.binding.EnterKeyCommandBinding;
import de.fau.cs.mad.fablab.android.view.common.binding.MenuItemCommandBinding;
import de.fau.cs.mad.fablab.android.view.common.fragments.BaseDialogFragment;
import de.fau.cs.mad.fablab.rest.core.Product;
import de.greenrobot.event.EventBus;

public class AddToCartDialogFragment extends BaseDialogFragment
        implements AddToCartDialogFragmentViewModel.Listener {
    @Bind(R.id.add_to_cart_name)
    TextView mNameTextView;
    @Bind(R.id.add_to_cart_price)
    TextView mPriceTextView;
    @Bind(R.id.add_to_cart_amount)
    EditText mAmountEditText;
    @Bind(R.id.add_to_cart_amount_tv)
    TextView mAmountTextView;
    @Bind(R.id.add_to_cart_price_total)
    TextView mPriceTotalTextView;

    @Inject
    AddToCartDialogFragmentViewModel mViewModel;

    private EventBus mEventBus = EventBus.getDefault();

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

        mEventBus.post(new AppBarShowDoorStateEvent(false));
        mEventBus.post(new AppBarShowTitleEvent(false));

        mViewModel.restoreState(getArguments(), savedInstanceState);

        mNameTextView.setText(mViewModel.getName());
        mPriceTextView.setText(Html.fromHtml(Formatter.formatPrice(mViewModel.getPrice())) + " "
                + getString(R.string.add_to_cart_price_per_unit) + " " + mViewModel.getUnit());

        if (mViewModel.isDecimalAmount()) {
            mAmountEditText.setInputType(InputType.TYPE_CLASS_NUMBER
                    | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        } else {
            mAmountEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        mAmountEditText.requestFocus();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                UiUtils.showKeyboard(getActivity(), mAmountEditText);
            }
        }, 250);

        new EditTextCommandBinding().bind(mAmountEditText, mViewModel.getChangeAmountCommand());
        new EnterKeyCommandBinding().bind(mAmountEditText, mViewModel.getAddToCartCommand());

        mPriceTotalTextView.setText(Html.fromHtml(Formatter.formatPrice(
                mViewModel.getPriceTotal())));

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
        menu.clear();
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
    public void onPause() {
        super.onPause();
        mEventBus.unregister(this);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mViewModel.saveState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mEventBus.register(this);
    }

    @Override
    public void onDismiss() {
        UiUtils.hideKeyboard(getActivity());
        getFragmentManager().popBackStack();
        mEventBus.post(new AppBarShowDoorStateEvent(true));
        mEventBus.post(new AppBarShowTitleEvent(true));
    }

    @Override
    public void onUpdatePriceAndAmount(double priceTotal, double amount) {
        mPriceTotalTextView.setText(Html.fromHtml(Formatter.formatPrice(priceTotal)));

        if (amount > 0) {
            mAmountTextView.setText(getString(R.string.add_to_cart_rounded_to) + " " + amount);
            mAmountTextView.setVisibility(View.VISIBLE);
        } else {
            mAmountTextView.setVisibility(View.INVISIBLE);
        }
    }

    @SuppressWarnings("unused")
    public void onEvent(BackButtonPressedEvent event) {
        UiUtils.hideKeyboard(getActivity());
        mEventBus.post(new AppBarShowDoorStateEvent(true));
        mEventBus.post(new AppBarShowTitleEvent(true));
    }
}
