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
import de.fau.cs.mad.fablab.android.model.entities.CartEntry;
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
    TextView name_tv;
    @Bind(R.id.add_to_cart_price)
    TextView price_tv;
    @Bind(R.id.add_to_cart_amount)
    EditText amount_et;
    @Bind(R.id.add_to_cart_amount_tv)
    TextView amount_tv;
    @Bind(R.id.add_to_cart_price_total)
    TextView price_total_tv;

    @Inject
    AddToCartDialogFragmentViewModel mViewModel;

    private EventBus mEventBus = EventBus.getDefault();

    public static AddToCartDialogFragment newInstance(CartEntry entry) {
        AddToCartDialogFragment dialogFragment = new AddToCartDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(AddToCartDialogFragmentViewModel.KEY_CART_ENTRY, entry);
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    public static AddToCartDialogFragment newInstance(Product product) {
        return newInstance(new CartEntry(product, 0));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mEventBus.post(new AppBarShowDoorStateEvent(false));
        mEventBus.post(new AppBarShowTitleEvent(false));

        mViewModel.restoreState(getArguments(), savedInstanceState);

        name_tv.setText(mViewModel.getName());
        price_tv.setText(Html.fromHtml(Formatter.formatPrice(mViewModel.getPrice())) + " "
                + getString(R.string.add_to_cart_price_per_unit) + " " + mViewModel.getUnit());

        if (mViewModel.isDecimalAmount()) {
            amount_et.setInputType(InputType.TYPE_CLASS_NUMBER
                    | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        } else {
            amount_et.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        if (amount_et.length() == 0 && mViewModel.isUpdate()) {
            if (mViewModel.isDecimalAmount()) {
                amount_et.setText("" + mViewModel.getAmount());
            } else {
                amount_et.setText("" + (int) mViewModel.getAmount());
            }
        }
        amount_et.requestFocus();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                UiUtils.showKeyboard(getActivity(), amount_et);
            }
        }, 250);

        new EditTextCommandBinding().bind(amount_et, mViewModel.getChangeAmountCommand());
        new EnterKeyCommandBinding().bind(amount_et, mViewModel.isUpdate() ?
                mViewModel.getUpdateCartEntryCommand() : mViewModel.getAddToCartCommand());

        price_total_tv.setText(Html.fromHtml(Formatter.formatPrice(
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

        if (mViewModel.isUpdate()) {
            inflater.inflate(R.menu.menu_update, menu);
            new MenuItemCommandBinding().bind(menu.findItem(R.id.action_update),
                    mViewModel.getUpdateCartEntryCommand());
        } else {
            inflater.inflate(R.menu.menu_add_to_cart, menu);
            new MenuItemCommandBinding().bind(menu.findItem(R.id.action_add_to_cart),
                    mViewModel.getAddToCartCommand());
        }
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
        price_total_tv.setText(Html.fromHtml(Formatter.formatPrice(priceTotal)));

        if (amount > 0) {
            amount_tv.setText(getString(R.string.add_to_cart_rounded_to) + " " + amount);
            amount_tv.setVisibility(View.VISIBLE);
        } else {
            amount_tv.setVisibility(View.INVISIBLE);
        }
    }

    @SuppressWarnings("unused")
    public void onEvent(BackButtonPressedEvent event) {
        UiUtils.hideKeyboard(getActivity());
        mEventBus.post(new AppBarShowDoorStateEvent(true));
        mEventBus.post(new AppBarShowTitleEvent(true));
    }
}
