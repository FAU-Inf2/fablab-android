package de.fau.cs.mad.fablab.android.model.dependencyinjection;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.fau.cs.mad.fablab.android.model.ICalStorage;
import de.fau.cs.mad.fablab.android.model.NewsStorage;
import de.fau.cs.mad.fablab.android.model.StorageFragment;
import de.fau.cs.mad.fablab.android.view.fragments.barcodescanner.BarcodeScannerFragment;
import de.fau.cs.mad.fablab.android.view.fragments.icals.ICalViewPagerFragment;
import de.fau.cs.mad.fablab.android.view.fragments.news.NewsFragment;
import de.fau.cs.mad.fablab.rest.myapi.ICalApi;
import de.fau.cs.mad.fablab.rest.myapi.NewsApi;
import de.fau.cs.mad.fablab.rest.myapi.ProductApi;

@SuppressWarnings("unused")
@Module(complete = false, injects = {BarcodeScannerFragment.class, NewsFragment.class, ICalViewPagerFragment.class})
public class ModelModule {
    private final StorageFragment mStorageFragment;

    public ModelModule(StorageFragment storageFragment) {
        mStorageFragment = storageFragment;
    }

    @Provides @Singleton
    NewsApi provideNewsApi() {
        return mStorageFragment.getRestClient().getNewsApi();
    }

    @Provides @Singleton
    ICalApi provideICalApi() {
        return mStorageFragment.getRestClient().getICalApi();
    }

    @Provides @Singleton
    ProductApi provideProductApi() {
        return mStorageFragment.getRestClient().getProductApi();
    }

    @Provides
    NewsStorage provideNewsStorage() {
        return mStorageFragment.getNewsStorage();
    }

    @Provides
    ICalStorage provideICalStorage()
    {
        return mStorageFragment.getICalStorage();
    }
}

