package de.fau.cs.mad.fablab.android.view.fragments.cart;

import android.content.res.Resources;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pedrogomez.renderers.RVRendererAdapter;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.util.Formatter;
import de.fau.cs.mad.fablab.android.view.MainActivity;
import de.fau.cs.mad.fablab.android.view.common.binding.SwipeableRecyclerViewCommandBinding;
import de.fau.cs.mad.fablab.android.view.common.binding.ViewCommandBinding;
import de.fau.cs.mad.fablab.android.view.fragments.checkout.QrCodeScannerFragment;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.greenrobot.event.EventBus;

public class CartSlidingUpPanel implements CartSlidingUpPanelViewModel.Listener {
    @InjectView(R.id.sliding_layout)
    SlidingUpPanelLayout sliding_up_pl;
    @InjectView(R.id.dragPart)
    LinearLayout drag_part_ll;
    @InjectView(R.id.cart_total_price_preview)
    TextView total_price_preview_tv;
    @InjectView(R.id.cart_recycler_view)
    RecyclerView cart_rv;
    @InjectView(R.id.cart_total_price)
    TextView total_price_tv;
    @InjectView(R.id.cart_button_checkout)
    Button checkout_button;

    @Inject
    CartSlidingUpPanelViewModel mViewModel;

    private RVRendererAdapter<CartEntryViewModel> mAdapter;

    private MainActivity mActivity;

    private int mPanelHeight;
    private int mPanelHeightDiff;
    private int mDragBgStrokeMargin;

    public CartSlidingUpPanel(MainActivity activity, View view) {
        mActivity = activity;

        activity.inject(this);
        ButterKnife.inject(this, view);

        final Resources res = sliding_up_pl.getResources();
        mPanelHeight = (int) res.getDimension(R.dimen.slidinguppanel_panel_height);
        mPanelHeightDiff = mPanelHeight - (int) res.getDimension(
                R.dimen.slidinguppanel_panel_height_opened);
        mDragBgStrokeMargin = (int) res.getDimension(R.dimen.slidinguppanel_drag_bg_stroke_margin);

        sliding_up_pl.setPanelHeight(mPanelHeight);
        sliding_up_pl.setPanelSlideListener(new SlidingUpPanelLayout.SimplePanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                updatePanelHeaderSize(slideOffset);
            }
        });

        final LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        cart_rv.setLayoutManager(layoutManager);

        mAdapter = new RVRendererAdapter<>(activity.getLayoutInflater(),
                new CartEntryViewModelRendererBuilder(),
                mViewModel.getCartEntryViewModelCollection());
        cart_rv.setAdapter(mAdapter);
        new SwipeableRecyclerViewCommandBinding().bind(cart_rv,
                mViewModel.getRemoveCartEntryCommand());

        new ViewCommandBinding().bind(checkout_button, mViewModel.getStartCheckoutCommand());

        mViewModel.setListener(this);
        mViewModel.initialize();
    }

    private void updatePanelHeaderSize(float slideOffset) {
        total_price_preview_tv.setAlpha(1 - slideOffset);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                mPanelHeight - (int) (mPanelHeightDiff * slideOffset));
        layoutParams.setMargins(mDragBgStrokeMargin, 0, mDragBgStrokeMargin, 0);
        drag_part_ll.setLayoutParams(layoutParams);
    }

    @Override
    public void onDataChanged() {
        mAdapter.notifyDataSetChanged();
        refreshPrice();
        updateVisibility();
    }

    @Override
    public void onShowUndoSnackbar() {
        Snackbar.make(sliding_up_pl, R.string.cart_product_removed, Snackbar.LENGTH_LONG)
                .setAction(R.string.cart_product_undo, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Command<Void> command = mViewModel.getUndoRemoveCartEntryCommand();
                        if (command.isExecutable()) {
                            command.execute(null);
                        }
                    }
                }).show();
    }

    @Override
    public void onVisibilityChanged() {
        updateVisibility();
    }

    @Override
    public void onStartCheckout() {
        setVisibility(false);
        mActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new QrCodeScannerFragment()).addToBackStack(null).commit();
    }

    private void refreshPrice() {
        String totalPrice = Formatter.formatPrice(mViewModel.getTotalPrice());

        total_price_preview_tv.setText(Html.fromHtml("<b>" + mViewModel.getCartEntriesCount()
                + "&nbsp;" + total_price_preview_tv.getResources().getString(
                R.string.cart_article_label) + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;"
                + "&nbsp;&nbsp;" + totalPrice + "</b>"));

        total_price_tv.setText(totalPrice);
    }

    private void updateVisibility() {
        if (mViewModel.isVisible()) {
            if (sliding_up_pl.getPanelState() != SlidingUpPanelLayout.PanelState.EXPANDED) {
                sliding_up_pl.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        } else {
            sliding_up_pl.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        }
    }

    public void setVisibility(boolean visible) {
        mViewModel.setVisibility(visible);
    }

    public void pause() {
        mViewModel.pause();
    }

    public void resume() {
        // Adapt Panel height when user rotates device
        if (sliding_up_pl.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
            updatePanelHeaderSize(1);
        }
        mViewModel.resume();
    }
}
