package de.fau.cs.mad.fablab.android.view.fragments.inventory;

import android.os.Bundle;

import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.view.fragments.productsearch.ProductClickedEvent;
import de.fau.cs.mad.fablab.android.view.fragments.productsearch.ProductSearchFragment;
import de.fau.cs.mad.fablab.rest.core.User;

public class InventoryProductSearchFragment extends ProductSearchFragment {

    private User mUser;
    private boolean mShowCartFAB;

    public void setUser(User user)
    {
        mUser = user;
    }

    public User getUser()
    {
        return mUser;
    }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mUser = (User) args.getSerializable(getResources().getString(R.string.key_user));
        mShowCartFAB = args.getBoolean(getResources().getString(R.string.key_show_cart_fab));
    }

    @Override
    public void onResume() {
        super.onResume();
        setDisplayOptions(R.id.drawer_item_inventory, true, mShowCartFAB, mShowCartFAB);
    }

    @Override
    @SuppressWarnings("unused")
    public void onEvent(ProductClickedEvent event) {
        getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                AddToInventoryDialogFragment.newInstance(event.getProduct(), mUser)).addToBackStack(null)
                .commit();
    }
}
