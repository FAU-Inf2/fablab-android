package de.fau.cs.mad.fablab.android.view;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import butterknife.ButterKnife;
import dagger.ObjectGraph;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.model.StorageFragment;
import de.fau.cs.mad.fablab.android.model.dependencyinjection.ModelModule;
import de.fau.cs.mad.fablab.android.view.fragments.settings.SettingsFragment;
import de.fau.cs.mad.fablab.android.util.StackTraceReporter;
import de.fau.cs.mad.fablab.android.util.TopExceptionHandler;
import de.fau.cs.mad.fablab.android.view.actionbar.ActionBar;
import de.fau.cs.mad.fablab.android.view.floatingbutton.FloatingFablabButton;
import de.fau.cs.mad.fablab.android.view.fragments.ICalAndNewsFragment;
import de.fau.cs.mad.fablab.android.view.fragments.about.AboutFragment;
import de.fau.cs.mad.fablab.android.view.fragments.barcodescanner.BarcodeScannerFragment;
import de.fau.cs.mad.fablab.android.view.fragments.cart.CartSlidingUpPanel;
import de.fau.cs.mad.fablab.android.view.fragments.productsearch.ProductSearchFragment;
import de.fau.cs.mad.fablab.android.view.navdrawer.NavigationDrawer;
import de.fau.cs.mad.fablab.android.view.navdrawer.NavigationEvent;
import de.greenrobot.event.EventBus;

public class MainActivity extends AppCompatActivity {
    private final static String TAG_STORAGE_FRAGMENT = "tag_storage_fragment";
    private final static String TAG_ICAL_AND_NEWS_FRAGMENT = "tag_ical_and_news_fragment";
    private final static String TAG_PRODUCTSEARCH_FRAGMENT = "tag_productsearch_fragment";
    private final static String TAG_BARCODE_FRAGMENT = "tag_barcode_fragment";
    private final static String TAG_ABOUT_FRAGMENT = "tag_about_fragment";
    private final static String TAG_SETTINGS_FRAGMENT = "tag_settings_fragment";

    private ActionBar mActionBar;
    private NavigationDrawer mNavigationDrawer;
    private FloatingFablabButton mFablabButton;
    private CartSlidingUpPanel mCartSlidingUpPanel;

    private ObjectGraph mObjectGraph;
    private EventBus mEventBus = EventBus.getDefault();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //uiUtils = new UiUtils();

        // register the TopExceptionHandler
        Thread.setDefaultUncaughtExceptionHandler(new TopExceptionHandler(this));
        StackTraceReporter.reportStackTraceIfAvailable(this);

        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        StorageFragment storageFragment = (StorageFragment) getSupportFragmentManager()
                .findFragmentByTag(TAG_STORAGE_FRAGMENT);
        if (storageFragment == null) {
            storageFragment = new StorageFragment();
            getSupportFragmentManager().beginTransaction().add(storageFragment,
                    TAG_STORAGE_FRAGMENT).commit();
        }

        mObjectGraph = ObjectGraph.create(new ModelModule(storageFragment));

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,
                    new ICalAndNewsFragment(), TAG_ICAL_AND_NEWS_FRAGMENT).commit();
        }

        mFablabButton = new FloatingFablabButton(this, findViewById(android.R.id.content));
        mNavigationDrawer = new NavigationDrawer(this, findViewById(android.R.id.content));
        mNavigationDrawer.restoreState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mActionBar.bindMenuItems();
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mEventBus.unregister(this);
        mCartSlidingUpPanel.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mActionBar = new ActionBar(this, findViewById(android.R.id.content));
        mCartSlidingUpPanel = new CartSlidingUpPanel(this, findViewById(android.R.id.content));
        mEventBus.register(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mNavigationDrawer.saveState(outState);
    }

    public void inject(Object object) {
        mObjectGraph.inject(object);
    }

    public void enableNavigationDrawer(boolean enable) {
        mNavigationDrawer.enableDrawer(enable);
    }

    public void setNavigationDrawerSelection(int menuItemId) {
        mNavigationDrawer.setSelection(menuItemId);
    }

    public void showCartSlidingUpPanel(boolean show) {
        mCartSlidingUpPanel.setVisibility(show);
    }

    public void showFloatingActionButton(boolean show) {
        mFablabButton.setVisibility(show);
    }

    @SuppressWarnings("unused")
    public void onEvent(NavigationEvent destination) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        String currentFragmentTag = getSupportFragmentManager().findFragmentById(
                R.id.fragment_container).getTag();

        switch(destination) {
            case News:
                if (!TAG_ICAL_AND_NEWS_FRAGMENT.equals(currentFragmentTag)) {
                    ICalAndNewsFragment iCalAndNewsFragment =
                            (ICalAndNewsFragment) getSupportFragmentManager().findFragmentByTag(
                                    TAG_ICAL_AND_NEWS_FRAGMENT);
                    if (iCalAndNewsFragment == null) {
                        iCalAndNewsFragment = new ICalAndNewsFragment();
                    }
                    fragmentTransaction.replace(R.id.fragment_container, iCalAndNewsFragment,
                            TAG_ICAL_AND_NEWS_FRAGMENT).addToBackStack(null).commit();
                }
                break;

            case BarcodeScanner:
                if (!TAG_BARCODE_FRAGMENT.equals(currentFragmentTag)) {
                    BarcodeScannerFragment barcodeScannerFragment =
                            (BarcodeScannerFragment) getSupportFragmentManager().findFragmentByTag(
                                    TAG_BARCODE_FRAGMENT);
                    if (barcodeScannerFragment == null) {
                        barcodeScannerFragment = new BarcodeScannerFragment();
                    }
                    fragmentTransaction.replace(R.id.fragment_container, barcodeScannerFragment,
                            TAG_BARCODE_FRAGMENT).addToBackStack(null).commit();
                }
                break;

            case ProductSearch:
                if (!TAG_PRODUCTSEARCH_FRAGMENT.equals(currentFragmentTag)) {
                    ProductSearchFragment productSearchFragment =
                            (ProductSearchFragment) getSupportFragmentManager().findFragmentByTag(
                                    TAG_PRODUCTSEARCH_FRAGMENT);
                    if (productSearchFragment == null) {
                        productSearchFragment = new ProductSearchFragment();
                    }
                    fragmentTransaction.replace(R.id.fragment_container, productSearchFragment,
                            TAG_PRODUCTSEARCH_FRAGMENT).addToBackStack(null).commit();
                }
                break;

            case Settings:
                if (!TAG_SETTINGS_FRAGMENT.equals(currentFragmentTag)) {
                    SettingsFragment settingsFragment =
                            (SettingsFragment) getSupportFragmentManager().findFragmentByTag(
                                    TAG_SETTINGS_FRAGMENT);
                    if (settingsFragment == null) {
                        settingsFragment = new SettingsFragment();
                    }
                    fragmentTransaction.replace(R.id.fragment_container, settingsFragment,
                            TAG_SETTINGS_FRAGMENT).addToBackStack(null).commit();
                }
                break;

            case About:
                if (!TAG_ABOUT_FRAGMENT.equals(currentFragmentTag)) {
                    AboutFragment aboutFragment =
                            (AboutFragment) getSupportFragmentManager().findFragmentByTag(
                                    TAG_ABOUT_FRAGMENT);
                    if (aboutFragment == null) {
                        aboutFragment = new AboutFragment();
                    }
                    fragmentTransaction.replace(R.id.fragment_container, aboutFragment,
                            TAG_ABOUT_FRAGMENT).addToBackStack(null).commit();
                }
                break;
        }
    }
}
