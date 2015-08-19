package de.fau.cs.mad.fablab.android.model.dependencyinjection;

import dagger.Module;
import dagger.Provides;
import de.fau.cs.mad.fablab.android.model.AutoCompleteModel;
import de.fau.cs.mad.fablab.android.model.CartModel;
import de.fau.cs.mad.fablab.android.model.CheckoutModel;
import de.fau.cs.mad.fablab.android.model.DrupalModel;
import de.fau.cs.mad.fablab.android.model.FablabMailModel;
import de.fau.cs.mad.fablab.android.model.ICalModel;
import de.fau.cs.mad.fablab.android.model.NewsModel;
import de.fau.cs.mad.fablab.android.model.ProductModel;
import de.fau.cs.mad.fablab.android.model.PushModel;
import de.fau.cs.mad.fablab.android.model.SpaceApiModel;
import de.fau.cs.mad.fablab.android.model.UserModel;
import de.fau.cs.mad.fablab.android.model.util.StorageFragment;
import de.fau.cs.mad.fablab.android.view.actionbar.ActionBar;
import de.fau.cs.mad.fablab.android.view.cartpanel.CartSlidingUpPanel;
import de.fau.cs.mad.fablab.android.view.floatingbutton.FloatingFablabButton;
import de.fau.cs.mad.fablab.android.view.fragments.about.AboutFragment;
import de.fau.cs.mad.fablab.android.view.fragments.alert.AlertDialogFragment;
import de.fau.cs.mad.fablab.android.view.fragments.barcodescanner.BarcodeScannerFragment;
import de.fau.cs.mad.fablab.android.view.fragments.cart.AddToCartDialogFragment;
import de.fau.cs.mad.fablab.android.view.fragments.checkout.CheckoutFragment;
import de.fau.cs.mad.fablab.android.view.fragments.checkout.QrCodeScannerFragment;
import de.fau.cs.mad.fablab.android.view.fragments.icalandnews.ICalAndNewsFragment;
import de.fau.cs.mad.fablab.android.view.fragments.icals.ICalDetailsDialogFragment;
import de.fau.cs.mad.fablab.android.view.fragments.icals.ICalFragment;
import de.fau.cs.mad.fablab.android.view.fragments.inventory.AddToInventoryDialogFragment;
import de.fau.cs.mad.fablab.android.view.fragments.inventory.InventoryBarcodeScannerFragment;
import de.fau.cs.mad.fablab.android.view.fragments.inventory.InventoryFragment;
import de.fau.cs.mad.fablab.android.view.fragments.inventory.InventoryProductSearchFragment;
import de.fau.cs.mad.fablab.android.view.fragments.news.NewsDetailsDialogFragment;
import de.fau.cs.mad.fablab.android.view.fragments.news.NewsFragment;
import de.fau.cs.mad.fablab.android.view.fragments.productmap.ProductMapFragment;
import de.fau.cs.mad.fablab.android.view.fragments.productsearch.ProductDialogFragment;
import de.fau.cs.mad.fablab.android.view.fragments.productsearch.ProductSearchFragment;
import de.fau.cs.mad.fablab.android.view.fragments.settings.SettingsFragment;
import de.fau.cs.mad.fablab.android.view.navdrawer.NavigationDrawer;

@SuppressWarnings("unused")
@Module(
        injects = {
                AboutFragment.class, ActionBar.class, AddToCartDialogFragment.class,
                AlertDialogFragment.class, BarcodeScannerFragment.class, CartSlidingUpPanel.class,
                CheckoutFragment.class, FloatingFablabButton.class, ICalAndNewsFragment.class,
                ICalDetailsDialogFragment.class, ICalFragment.class, NavigationDrawer.class,
                NewsDetailsDialogFragment.class, NewsFragment.class, ProductDialogFragment.class,
                ProductMapFragment.class, ProductSearchFragment.class, QrCodeScannerFragment.class,
                AlertDialogFragment.class, InventoryFragment.class, InventoryBarcodeScannerFragment.class,
                InventoryProductSearchFragment.class, AddToInventoryDialogFragment.class,
                SettingsFragment.class
        })
public class ModelModule {
    private final StorageFragment mStorageFragment;

    public ModelModule(StorageFragment storageFragment) {
        mStorageFragment = storageFragment;
    }

    @Provides
    NewsModel provideNewsModel() {
        return mStorageFragment.getNewsModel();
    }

    @Provides
    ICalModel provideICalModel() {
        return mStorageFragment.getICalModel();
    }

    @Provides
    CartModel provideCartModel() {
        return mStorageFragment.getCartModel();
    }

    @Provides
    CheckoutModel provideCheckoutModel() {
        return mStorageFragment.getCheckoutModel();
    }

    @Provides
    ProductModel provideProductModel() {
        return mStorageFragment.getProductModel();
    }

    @Provides
    AutoCompleteModel provideAutoCompleteModel() {
        return mStorageFragment.getAutoCompleteModel();
    }

    @Provides
    SpaceApiModel provideSpaceApiModel() {
        return mStorageFragment.getSpaceApiModel();
    }

    @Provides
    PushModel providePushModel() {
        return mStorageFragment.getPushModel();
    }

    @Provides
    FablabMailModel provideFablabMailModel()
    {
        return mStorageFragment.getFablabMailModel();
    }

    @Provides
    DrupalModel provideDrupalModel()
    {
        return mStorageFragment.getDrupalModel();
    }

    @Provides
    UserModel provideUserModel()
    {
        return mStorageFragment.getUserModel();
    }
}
