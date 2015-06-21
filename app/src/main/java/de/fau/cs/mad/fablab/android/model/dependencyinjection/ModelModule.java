package de.fau.cs.mad.fablab.android.model.dependencyinjection;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.fau.cs.mad.fablab.android.model.ICalStorage;
import de.fau.cs.mad.fablab.android.model.NewsStorage;
import de.fau.cs.mad.fablab.android.model.StorageFragment;
import de.fau.cs.mad.fablab.android.view.fragments.barcodescanner.BarcodeScannerFragment;
import de.fau.cs.mad.fablab.android.view.fragments.icals.ICalViewPagerFragment;
import de.fau.cs.mad.fablab.android.view.fragments.news.NewsFragment;
import de.fau.cs.mad.fablab.rest.ICalApiClient;
import de.fau.cs.mad.fablab.rest.NewsApiClient;
import de.fau.cs.mad.fablab.rest.ProductApiClient;
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
    NewsApi provideNewsApi(Application application) {
        return new NewsApiClient(application).get();
    }

    @Provides @Singleton
    ICalApi provideICalApi(Application application) {
        return new ICalApiClient(application).get();
    }

    @Provides @Singleton
    ProductApi provideProductApi(Application application) {
        return new ProductApiClient(application).get();
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

