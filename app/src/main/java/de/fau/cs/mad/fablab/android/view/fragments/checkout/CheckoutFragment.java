package de.fau.cs.mad.fablab.android.view.fragments.checkout;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pedrogomez.renderers.RVRendererAdapter;

import javax.inject.Inject;

import butterknife.Bind;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.util.Formatter;
import de.fau.cs.mad.fablab.android.view.activities.MainActivity;
import de.fau.cs.mad.fablab.android.view.cartpanel.CartEntryViewModel;
import de.fau.cs.mad.fablab.android.view.cartpanel.CartEntryViewModelRendererBuilder;
import de.fau.cs.mad.fablab.android.view.common.binding.ViewCommandBinding;
import de.fau.cs.mad.fablab.android.view.common.fragments.BaseDialogFragment;

public class CheckoutFragment extends BaseDialogFragment implements CheckoutViewModel.Listener {
    static final String KEY_CART_CODE = "key_cart_code";

    @Bind(R.id.checkout_progress_bar)
    ProgressBar progress_bar;
    @Bind(R.id.checkout_status_description)
    TextView status_description_tv;
    @Bind(R.id.checkout_cart_preview)
    LinearLayout cart_preview_ll;
    @Bind(R.id.checkout_cart_recycler_view)
    RecyclerView cart_rv;
    @Bind(R.id.checkout_total_price)
    TextView total_price_tv;
    @Bind(R.id.checkout_retry_button)
    Button retry_button;
    @Bind(R.id.checkout_ok_button)
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

        cart_rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        RVRendererAdapter<CartEntryViewModel> adapter = new RVRendererAdapter<>(
                getLayoutInflater(savedInstanceState), new CartEntryViewModelRendererBuilder(),
                mViewModel.getCartEntryViewModelCollection());
        cart_rv.setAdapter(adapter);
        total_price_tv.setText(Formatter.formatPrice(mViewModel.getTotalPrice()));

        new ViewCommandBinding().bind(retry_button, mViewModel.getRetryCommand());
        new ViewCommandBinding().bind(ok_button, mViewModel.getOkCommand());

        mViewModel.initialize(getArguments());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        return inflater.inflate(R.layout.fragment_checkout, container, false);
    }

    @Override
    public void onPause() {
        super.onPause();
        mViewModel.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.resume();

        setDisplayOptions(MainActivity.DISPLAY_LOGO);
    }

    @Override
    public void onShowProgressBar() {
        status_description_tv.setVisibility(View.GONE);
        progress_bar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onHideProgressBar() {
        progress_bar.setVisibility(View.GONE);
        status_description_tv.setVisibility(View.VISIBLE);
    }

    @Override
    public void onShowCartPreview() {
        status_description_tv.setVisibility(View.GONE);
        cart_preview_ll.setVisibility(View.VISIBLE);
    }

    @Override
    public void onHideCartPreview() {
        cart_preview_ll.setVisibility(View.GONE);
        status_description_tv.setVisibility(View.VISIBLE);
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
        getFragmentManager().popBackStack();
    }
}
