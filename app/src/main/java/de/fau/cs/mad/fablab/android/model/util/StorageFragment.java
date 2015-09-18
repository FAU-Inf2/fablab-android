package de.fau.cs.mad.fablab.android.model.util;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;

import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.model.AutoCompleteModel;
import de.fau.cs.mad.fablab.android.model.CartModel;
import de.fau.cs.mad.fablab.android.model.CategoryModel;
import de.fau.cs.mad.fablab.android.model.CheckoutModel;
import de.fau.cs.mad.fablab.android.model.DrupalModel;
import de.fau.cs.mad.fablab.android.model.MailModel;
import de.fau.cs.mad.fablab.android.model.ICalModel;
import de.fau.cs.mad.fablab.android.model.InventoryModel;
import de.fau.cs.mad.fablab.android.model.NewsModel;
import de.fau.cs.mad.fablab.android.model.ProductModel;
import de.fau.cs.mad.fablab.android.model.PushModel;
import de.fau.cs.mad.fablab.android.model.SpaceApiModel;
import de.fau.cs.mad.fablab.android.model.UserModel;
import de.fau.cs.mad.fablab.android.model.VersionCheckModel;

/**
 * The main storage fragment which initializes all other storage parts
 */
public class StorageFragment extends Fragment {
    private ICalModel mICalModel;
    private NewsModel mNewsModel;
    private CartModel mCartModel;
    private PushModel mPushModel;
    private CheckoutModel mCheckoutModel;
    private ProductModel mProductModel;
    private AutoCompleteModel mAutoCompleteModel;
    private SpaceApiModel mSpaceApiModel;
    private MailModel mMailModel;
    private DrupalModel mDrupalModel;
    private UserModel mUserModel;
    private InventoryModel mInventoryModel;
    private VersionCheckModel mVersionCheckModel;
    private CategoryModel mCategoryModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        RestClient restClient = new RestClient(getActivity().getApplicationContext());
        DatabaseHelper databaseHelper = DatabaseHelper.getHelper(getActivity().getApplicationContext());
        mICalModel = new ICalModel(restClient.getICalApi(), databaseHelper.getICalDao());
        mNewsModel = new NewsModel(restClient.getNewsApi(), databaseHelper.getNewsDao());
        mCartModel = new CartModel(databaseHelper.getCartDao());
        mPushModel = new PushModel(getActivity().getApplication(), restClient.getPushApi());
        mCheckoutModel = new CheckoutModel(mCartModel, restClient.getCartApi(), mPushModel);
        mProductModel = new ProductModel(databaseHelper.getProductDao(), restClient.getProductApi(),
                getActivity().getApplicationContext());
        mAutoCompleteModel = new AutoCompleteModel(databaseHelper.getAutoCompleteWordsDao(),
                restClient.getProductApi());
        long pollingFrequency = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(
                getActivity()).getString("spaceapi_polling_freq", "15")) * 60 * 1000;
        mSpaceApiModel = new SpaceApiModel(restClient.getSpaceApi(), getString(R.string.space_name),
                pollingFrequency);
        mMailModel = new MailModel(restClient.getDataApi());
        mDrupalModel = new DrupalModel(restClient.getDrupalApi());
        mUserModel = new UserModel(restClient.getRestAdapterBuilder());
        mInventoryModel = new InventoryModel(restClient.getRestAdapterBuilder());
        mVersionCheckModel = new VersionCheckModel(restClient.getVersionCheckApi(),
                getActivity().getApplicationContext());
        mCategoryModel = new CategoryModel(restClient.getCategoryApi(), mProductModel);
    }

    public NewsModel getNewsModel() {
        return mNewsModel;
    }

    public ICalModel getICalModel() {
        return mICalModel;
    }

    public CartModel getCartModel() {
        return mCartModel;
    }

    public PushModel getPushModel() {
        return mPushModel;
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

    public MailModel getFablabMailModel() {
        return mMailModel;
    }

    public DrupalModel getDrupalModel() {
        return mDrupalModel;
    }

    public UserModel getUserModel()
    {
        return mUserModel;
    }

    public InventoryModel getInventoryModel()
    {
        return mInventoryModel;
    }

    public VersionCheckModel getVersionCheckModel()
    {
        return mVersionCheckModel;
    }

    public CategoryModel getCategoryModel()
    {
        return mCategoryModel;
    }
}
