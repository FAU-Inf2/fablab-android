package de.fau.cs.mad.fablab.android.view.fragments.checkout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.InjectView;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.util.UiUtils;
import de.fau.cs.mad.fablab.android.view.common.binding.ViewCommandBinding;
import de.fau.cs.mad.fablab.android.view.common.fragments.BaseDialogFragment;

public class CheckoutFragment extends BaseDialogFragment implements CheckoutViewModel.Listener {
    static final String KEY_CART_CODE = "key_cart_code";

    @InjectView(R.id.spinner)
    RelativeLayout spinner_rl;
    @InjectView(R.id.spinner_image)
    ImageView spinner_iv;
    @InjectView(R.id.checkout_status_description)
    TextView status_description_tv;
    @InjectView(R.id.checkout_retry_button)
    Button retry_button;
    @InjectView(R.id.checkout_ok_button)
    Button ok_button;

    @Inject
    CheckoutViewModel mViewModel;

    public static CheckoutFragment newInstance(String cartCode) {
        CheckoutFragment checkoutFragment = new CheckoutFragment();
        Bundle args = new Bundle();
        args.putString(KEY_CART_CODE, cartCode);
        checkoutFragment.setArguments(args);
        return checkoutFragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel.setListener(this);

        new ViewCommandBinding().bind(retry_button, mViewModel.getRetryCommand());
        new ViewCommandBinding().bind(ok_button, mViewModel.getOkCommand());

        if (savedInstanceState == null) {
            mViewModel.initialize();
        }

        mViewModel.restoreState(getArguments(), savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_checkout, container, false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mViewModel.saveState(outState);
    }

    @Override
    public void onShowSpinner() {
        status_description_tv.setVisibility(View.GONE);
        UiUtils.showSpinner(spinner_rl, spinner_iv);
    }

    @Override
    public void onHideSpinner() {
        status_description_tv.setVisibility(View.VISIBLE);
        UiUtils.hideSpinner(spinner_rl, spinner_iv);
    }

    @Override
    public void onShowPayMessage() {
        status_description_tv.setText(R.string.checkout_pay);
    }

    @Override
    public void onShowErrorMessage() {
        status_description_tv.setText(R.string.checkout_error);
    }

    @Override
    public void onShowCancelledMessage() {
        status_description_tv.setText(R.string.checkout_cancelled);
    }

    @Override
    public void onShowPaidMessage() {
        status_description_tv.setText(R.string.checkout_paid);
    }

    @Override
    public void onDismiss() {
        dismiss();
    }
}
