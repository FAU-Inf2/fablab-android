package de.fau.cs.mad.fablab.android.view.dependencyinjection;

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import dagger.Module;
import dagger.Provides;
import de.fau.cs.mad.fablab.android.actionbar.ActionBar;
import de.fau.cs.mad.fablab.android.model.dependencyinjection.ModelModule;
import de.fau.cs.mad.fablab.android.view.floatingbutton.FloatingFablabButton;
import de.fau.cs.mad.fablab.android.view.fragments.ICalAndNewsFragment;
import de.fau.cs.mad.fablab.android.view.fragments.cart.AddToCartDialogFragment;
import de.fau.cs.mad.fablab.android.view.fragments.icals.viewpager.ICalDetailFragment;
import de.fau.cs.mad.fablab.android.view.fragments.icals.viewpager.ICalFragment;
import de.fau.cs.mad.fablab.android.view.fragments.news.NewsDetailsDialogFragment;
import de.fau.cs.mad.fablab.android.view.fragments.news.recyclerview.NewsViewHolder;
import de.fau.cs.mad.fablab.android.view.navdrawer.NavigationDrawer;

@SuppressWarnings("unused")
@Module(
        includes = ModelModule.class,
        injects = {
                ActionBar.class, AddToCartDialogFragment.class, FloatingFablabButton.class,
                ICalAndNewsFragment.class, ICalFragment.class, ICalDetailFragment.class,
                NavigationDrawer.class, NewsDetailsDialogFragment.class, NewsViewHolder.class
        })
public class ActivityModule {
    private final FragmentActivity mActivity;

    public ActivityModule(FragmentActivity activity) {
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

    @Provides
    FragmentManager provideFragmentManager() {
        return mActivity.getSupportFragmentManager();
    }
    
}
