package de.fau.cs.mad.fablab.android.model;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * The main storage fragment which initializes all other storage parts
 */
public class StorageFragment extends Fragment {
    private ICalModel mICalModel;
    private NewsModel mNewsModel;
    private CartModel mCartModel;
    private ProductModel mProductModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        RestClient restClient = new RestClient(getActivity().getApplicationContext());
        DatabaseHelper databaseHelper = DatabaseHelper.getHelper(getActivity());
        mICalModel = new ICalModel(restClient.getICalApi());
        mNewsModel = new NewsModel(restClient.getNewsApi());
        mCartModel = new CartModel(databaseHelper.getCartDao(), restClient.getCartApi());
        mProductModel = new ProductModel(databaseHelper.getProductDao(), restClient.getProductApi());
    }

    public NewsModel getNewsModel(){
        return mNewsModel;
    }

    public ICalModel getICalModel() {
        return mICalModel;
    }

    public CartModel getCartModel() {
        return mCartModel;
    }

    public ProductModel getProductModel() {
        return mProductModel;
    }
}
