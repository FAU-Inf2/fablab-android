package de.fau.cs.mad.fablab.android.model;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/***
 * The main storage fragment which initializes all other storage parts
 */
public class StorageFragment extends Fragment {
    private RestClient mRestClient;
    private NavigationDrawerStorage navigationDrawerStorage;
    private NewsModel mNewsModel;
    private ICalStorage iCalStorage;
    private CartModel mCartModel;
    private ProductModel mProductModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mRestClient = new RestClient(getActivity().getApplicationContext());
        mNewsModel = new NewsModel(mRestClient.getNewsApi());
        iCalStorage = new ICalStorage();
        navigationDrawerStorage = new NavigationDrawerStorage();
        mCartModel = new CartModel(DatabaseHelper.getHelper(getActivity()).getCartDao());
        mProductModel = new ProductModel(DatabaseHelper.getHelper(getActivity()).getProductDao(),
                mRestClient.getProductApi());
    }

    public RestClient getRestClient() {
        return mRestClient;
    }

    public NewsModel getNewsModel(){
        return mNewsModel;
    }

    public ICalStorage getICalStorage()
    {
        return iCalStorage;
    }

    public NavigationDrawerStorage getNavigationDrawerStorage() {
        return navigationDrawerStorage;
    }

    public CartModel getCartModel() {
        return mCartModel;
    }

    public ProductModel getProductModel() {
        return mProductModel;
    }
}
