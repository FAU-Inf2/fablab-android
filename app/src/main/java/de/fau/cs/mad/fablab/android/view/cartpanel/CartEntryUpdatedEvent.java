package de.fau.cs.mad.fablab.android.view.cartpanel;

public class CartEntryUpdatedEvent {
    private final CartEntryViewModel mViewModel;

    public CartEntryUpdatedEvent(CartEntryViewModel viewModel) {
        mViewModel = viewModel;
    }

    public CartEntryViewModel getViewModel() {
        return mViewModel;
    }
}
