package de.fau.cs.mad.fablab.android.view.fragments.cart;

public class CartEntryUpdatedEvent {
    private final CartEntryViewModel mViewModel;

    public CartEntryUpdatedEvent(CartEntryViewModel viewModel) {
        mViewModel = viewModel;
    }

    public CartEntryViewModel getViewModel() {
        return mViewModel;
    }
}
