package de.fau.cs.mad.fablab.android.model.util;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.model.AutoCompleteModel;
import de.fau.cs.mad.fablab.android.model.CartModel;
import de.fau.cs.mad.fablab.android.model.CheckoutModel;
import de.fau.cs.mad.fablab.android.model.FablabMailModel;
import de.fau.cs.mad.fablab.android.model.ICalModel;
import de.fau.cs.mad.fablab.android.model.NewsModel;
import de.fau.cs.mad.fablab.android.model.ProductModel;
import de.fau.cs.mad.fablab.android.model.PushModel;
import de.fau.cs.mad.fablab.android.model.SpaceApiModel;

/**
 * The main storage fragment which initializes all other storage parts
 */
public class StorageFragment extends Fragment {
    private ICalModel mICalModel;
    private NewsModel mNewsModel;
    private CartModel mCartModel;
    private CheckoutModel mCheckoutModel;
    private ProductModel mProductModel;
    private AutoCompleteModel mAutoCompleteModel;
    private SpaceApiModel mSpaceApiModel;
    private FablabMailModel mFablabMailModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        RestClient restClient = new RestClient(getActivity().getApplicationContext(), false);
        RestClient restClientString = new RestClient(getActivity().getApplicationContext(), true);
        DatabaseHelper databaseHelper = DatabaseHelper.getHelper(getActivity().getApplicationContext());
        mICalModel = new ICalModel(restClient.getICalApi());
        mNewsModel = new NewsModel(restClient.getNewsApi());
        mCartModel = new CartModel(databaseHelper.getCartDao());
        PushModel pushModel = new PushModel(getActivity().getApplication(), restClient.getPushApi());
        mCheckoutModel = new CheckoutModel(mCartModel, restClient.getCartApi(), pushModel);
        mProductModel = new ProductModel(databaseHelper.getProductDao(), restClient.getProductApi());
        mAutoCompleteModel = new AutoCompleteModel(databaseHelper.getAutoCompleteWordsDao(),
                restClient.getProductApi());
        mSpaceApiModel = new SpaceApiModel(restClient.getSpaceApi(), getString(R.string.space_name));
        mFablabMailModel = new FablabMailModel(restClientString.getDataApi());
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

    public CheckoutModel getCheckoutModel() {
        return mCheckoutModel;
    }

    public ProductModel getProductModel() {
        return mProductModel;
    }

    public AutoCompleteModel getAutoCompleteModel() {
        return mAutoCompleteModel;
    }

    public SpaceApiModel getSpaceApiModel() {
        return mSpaceApiModel;
    }

    public FablabMailModel getFablabMailModel()
    {
        return mFablabMailModel;
    }
}
