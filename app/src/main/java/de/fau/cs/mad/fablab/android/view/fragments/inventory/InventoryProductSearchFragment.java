package de.fau.cs.mad.fablab.android.view.fragments.inventory;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.view.fragments.productsearch.ProductClickedEvent;
import de.fau.cs.mad.fablab.android.view.fragments.productsearch.ProductSearchFragment;

public class InventoryProductSearchFragment extends ProductSearchFragment {

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onResume() {
        super.onResume();
        setDisplayOptions(R.id.drawer_item_productsearch, false, false);
    }

    @Override
    @SuppressWarnings("unused")
    public void onEvent(ProductClickedEvent event) {
        getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                AddToInventoryDialogFragment.newInstance(event.getProduct())).addToBackStack(null)
                .commit();
    }
}
