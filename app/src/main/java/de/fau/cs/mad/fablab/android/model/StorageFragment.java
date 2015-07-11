package de.fau.cs.mad.fablab.android.model;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import de.fau.cs.mad.fablab.android.R;

/**
 * The main storage fragment which initializes all other storage parts
 */
public class StorageFragment extends Fragment {
    private ICalModel mICalModel;
    private NewsModel mNewsModel;
    private CartModel mCartModel;
    private ProductModel mProductModel;
    private AutoCompleteModel mAutoCompleteModel;
    private SpaceApiModel mSpaceApiModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        RestClient restClient = new RestClient(getActivity().getApplicationContext());
        DatabaseHelper databaseHelper = DatabaseHelper.getHelper(getActivity().getApplicationContext());
        mICalModel = new ICalModel(restClient.getICalApi());
        mNewsModel = new NewsModel(restClient.getNewsApi());
        PushModel pushModel = new PushModel(getActivity().getApplication(), restClient.getPushApi());
        mCartModel = new CartModel(databaseHelper.getCartDao(), restClient.getCartApi(), pushModel);
        mProductModel = new ProductModel(databaseHelper.getProductDao(), restClient.getProductApi());
        mAutoCompleteModel = new AutoCompleteModel(databaseHelper.getAutoCompleteWordsDao(),
                restClient.getProductApi());
        mSpaceApiModel = new SpaceApiModel(restClient.getSpaceApi(), getString(R.string.space_name));
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

    public AutoCompleteModel getAutoCompleteModel() {
        return mAutoCompleteModel;
    }

    public SpaceApiModel getSpaceApiModel() {
        return mSpaceApiModel;
    }
}
