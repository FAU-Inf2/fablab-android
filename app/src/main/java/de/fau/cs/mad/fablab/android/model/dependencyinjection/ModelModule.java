package de.fau.cs.mad.fablab.android.model.dependencyinjection;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.fau.cs.mad.fablab.android.model.CartModel;
import de.fau.cs.mad.fablab.android.model.ICalStorage;
import de.fau.cs.mad.fablab.android.model.NewsModel;
import de.fau.cs.mad.fablab.android.model.ProductModel;
import de.fau.cs.mad.fablab.android.model.StorageFragment;
import de.fau.cs.mad.fablab.android.view.fragments.barcodescanner.BarcodeScannerFragment;
import de.fau.cs.mad.fablab.android.view.fragments.cart.AddToCartDialogFragment;
import de.fau.cs.mad.fablab.android.view.fragments.cart.CartSlidingUpPanel;
import de.fau.cs.mad.fablab.android.view.fragments.icals.ICalViewPagerFragment;
import de.fau.cs.mad.fablab.android.view.fragments.news.NewsFragment;
import de.fau.cs.mad.fablab.rest.myapi.ICalApi;

@SuppressWarnings("unused")
@Module(
        complete = false,
        injects = {
                AddToCartDialogFragment.class, BarcodeScannerFragment.class, CartSlidingUpPanel.class,
                NewsFragment.class, ICalViewPagerFragment.class
        })
public class ModelModule {
    private final StorageFragment mStorageFragment;

    public ModelModule(StorageFragment storageFragment) {
        mStorageFragment = storageFragment;
    }

    @Provides @Singleton
    ICalApi provideICalApi() {
        return mStorageFragment.getRestClient().getICalApi();
    }

    @Provides
    NewsModel provideNewsModel() {
        return mStorageFragment.getNewsModel();
    }

    @Provides
    ICalStorage provideICalStorage()
    {
        return mStorageFragment.getICalStorage();
    }

    @Provides
    CartModel provideCartModel() {
        return mStorageFragment.getCartModel();
    }

    @Provides
    ProductModel provideProductModel() {
        return mStorageFragment.getProductModel();
    }
}

