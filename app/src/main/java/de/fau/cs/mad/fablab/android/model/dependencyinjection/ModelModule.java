package de.fau.cs.mad.fablab.android.model.dependencyinjection;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.fau.cs.mad.fablab.android.model.NewsStorage;
import de.fau.cs.mad.fablab.android.model.StorageFragment;
import de.fau.cs.mad.fablab.android.view.fragments.news.NewsFragment;
import de.fau.cs.mad.fablab.rest.NewsApiClient;
import de.fau.cs.mad.fablab.rest.myapi.NewsApi;

@SuppressWarnings("unused")
@Module(complete = false, injects = {NewsFragment.class})
public class ModelModule {
    private final StorageFragment mStorageFragment;

    public ModelModule(StorageFragment storageFragment) {
        mStorageFragment = storageFragment;
    }

    @Provides @Singleton
    NewsApi provideNewsApi(Application application) {
        return new NewsApiClient(application).get();
    }

    @Provides
    NewsStorage provideNewsStorage() {
        return mStorageFragment.getNewsStorage();
    }
}

