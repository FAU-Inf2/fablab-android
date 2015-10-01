package de.fau.cs.mad.fablab.android.view.fragments.projects;

import com.pedrogomez.renderers.AdapteeCollection;
import com.pedrogomez.renderers.ListAdapteeCollection;

import java.util.List;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.model.CartModel;
import de.fau.cs.mad.fablab.android.model.entities.Cart;

public class CartChooserDialogFragmentViewModel {

    private CartModel mModel;
    private ListAdapteeCollection<CartViewModel> mCartViewModelCollection;
    private Listener mListener;

    @Inject
    CartChooserDialogFragmentViewModel(CartModel model)
    {
        mModel = model;
        mCartViewModelCollection = new ListAdapteeCollection<>();
        update();
    }

    public AdapteeCollection<CartViewModel> getCartViewModelCollection() {
        return mCartViewModelCollection;
    }

    public void setListener(Listener listener)
    {
        mListener = listener;
    }

    public void update()
    {
        List<Cart> carts = mModel.getAllPaidCarts();
        mCartViewModelCollection.clear();
        for(Cart c : carts)
        {
            mCartViewModelCollection.add(new CartViewModel(c));
        }
        if (mListener != null) {
            System.out.println("UPDATE SIZE: " + mCartViewModelCollection.size());
            mListener.onDataChanged();
        }
    }

    public interface Listener{
        void onDataChanged();
    }
}
