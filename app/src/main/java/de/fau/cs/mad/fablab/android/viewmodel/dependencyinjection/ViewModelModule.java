package de.fau.cs.mad.fablab.android.viewmodel.dependencyinjection;

import android.app.Activity;


import dagger.Module;
import dagger.Provides;

import de.fau.cs.mad.fablab.android.view.fragments.news.NewsFragmentViewModel;
import de.fau.cs.mad.fablab.android.view.fragments.news.NewsViewLauncher;
import de.fau.cs.mad.fablab.android.view.fragments.news.list.NewsViewHolderViewModel;

@Module(injects = {NewsViewHolderViewModel.class, NewsFragmentViewModel.class})
public class ViewModelModule {

    private Activity mActivity;

    public ViewModelModule(Activity activity){
        this.mActivity = activity;
    }

    @Provides
    NewsViewLauncher provideNewsViewLauncher(Activity activity){
        return new NewsViewLauncher(activity);
    }

    @Provides
    Activity provideActivity(){
        return mActivity;
    }

}


