package de.fau.cs.mad.fablab.android.view.dependencyinjection;

import android.app.Activity;
import android.app.Application;

import dagger.Module;
import dagger.Provides;
import de.fau.cs.mad.fablab.android.model.dependencyinjection.ModelModule;
import de.fau.cs.mad.fablab.android.view.floatingbutton.FloatingFablabButton;
import de.fau.cs.mad.fablab.android.view.fragments.news.recyclerview.NewsViewHolder;

@SuppressWarnings("unused")
@Module(includes = ModelModule.class, injects = {FloatingFablabButton.class, NewsViewHolder.class})
public class ActivityModule {
    private final Activity mActivity;

    public ActivityModule(Activity activity) {
        mActivity = activity;
    }

    @Provides
    Activity provideActivity() {
        return mActivity;
    }

    @Provides
    Application provideApplication() {
        return mActivity.getApplication();
    }
}
