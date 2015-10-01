package de.fau.cs.mad.fablab.android.view.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import butterknife.ButterKnife;
import dagger.ObjectGraph;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.model.dependencyinjection.ModelModule;
import de.fau.cs.mad.fablab.android.model.events.NavigationEventBarcodeScannerInventory;
import de.fau.cs.mad.fablab.android.model.events.NavigationEventInventory;
import de.fau.cs.mad.fablab.android.model.events.NavigationEventProductSearchInventory;
import de.fau.cs.mad.fablab.android.model.events.NavigationEventReservation;
import de.fau.cs.mad.fablab.android.model.events.NavigationEventShowInventory;
import de.fau.cs.mad.fablab.android.model.events.ShowToastEvent;
import de.fau.cs.mad.fablab.android.model.events.UpdateAvailableEvent;
import de.fau.cs.mad.fablab.android.model.util.StorageFragment;
import de.fau.cs.mad.fablab.android.util.StackTraceReporter;
import de.fau.cs.mad.fablab.android.util.TopExceptionHandler;
import de.fau.cs.mad.fablab.android.view.actionbar.ActionBar;
import de.fau.cs.mad.fablab.android.view.cartpanel.CartSlidingUpPanel;
import de.fau.cs.mad.fablab.android.view.floatingbutton.FloatingFablabButton;
import de.fau.cs.mad.fablab.android.view.fragments.about.AboutFragment;
import de.fau.cs.mad.fablab.android.view.fragments.alert.AlertDialogFragment;
import de.fau.cs.mad.fablab.android.view.fragments.barcodescanner.BarcodeScannerFragment;
import de.fau.cs.mad.fablab.android.view.fragments.icalandnews.ICalAndNewsFragment;
import de.fau.cs.mad.fablab.android.view.fragments.inventory.InventoryBarcodeScannerFragment;
import de.fau.cs.mad.fablab.android.view.fragments.inventory.InventoryFragment;
import de.fau.cs.mad.fablab.android.view.fragments.inventory.InventoryLoginFragment;
import de.fau.cs.mad.fablab.android.view.fragments.inventory.InventoryProductSearchFragment;
import de.fau.cs.mad.fablab.android.view.fragments.inventory.ShowInventoryFragment;
import de.fau.cs.mad.fablab.android.view.fragments.reservation.ReservationFragment;
import de.fau.cs.mad.fablab.android.view.fragments.productsearch.ProductSearchFragment;
import de.fau.cs.mad.fablab.android.view.fragments.projects.ProjectFragment;
import de.fau.cs.mad.fablab.android.view.fragments.settings.SettingsFragment;
import de.fau.cs.mad.fablab.android.view.fragments.versioncheck.VersionCheckDialogFragment;
import de.fau.cs.mad.fablab.android.view.navdrawer.NavigationDrawer;
import de.fau.cs.mad.fablab.android.view.navdrawer.NavigationEvent;
import de.greenrobot.event.EventBus;

public class MainActivity extends AppCompatActivity {
    public static final int DISPLAY_NOTHING = 0;
    public static final int DISPLAY_LOGO = 1;
    public static final int DISPLAY_TITLE = 2;
    public static final int DISPLAY_NAVDRAWER = 4;
    public static final int DISPLAY_CART_PANEL = 8;
    public static final int DISPLAY_FAB = 16;
    public static final int DISPLAY_TIME = 32;

    private final static String TAG_STORAGE_FRAGMENT = "tag_storage_fragment";
    private final static String TAG_ICAL_AND_NEWS_FRAGMENT = "tag_ical_and_news_fragment";
    private final static String TAG_PRODUCTSEARCH_FRAGMENT = "tag_productsearch_fragment";
    private final static String TAG_BARCODE_FRAGMENT = "tag_barcode_fragment";
    private final static String TAG_ABOUT_FRAGMENT = "tag_about_fragment";
    private final static String TAG_SETTINGS_FRAGMENT = "tag_settings_fragment";
    private final static String TAG_RESERVATION_FRAGMENT = "tag_reservation_fragment";
    private final static String TAG_ALERT_FRAGMENT = "tag_alert_fragment";
    private final static String TAG_INVENTORY_FRAGMENT = "tag_inventory_fragment";
    private final static String TAG_BARCODE_INVENTORY_FRAGMENT = "tag_barcode_inventory_fragment";
    private final static String TAG_PRODUCTSEARCH_INVENTORY_FRAGMENT = "tag_productsearch_inventory_fragment";
    private final static String TAG_SHOWINVENTORY_INVENTORY_FRAGMENT = "tag_showinventory_inventory_fragment";
    private final static String TAG_CATEGORYSEARCH_FRAGMENT ="tag_categorysearch_fragment";
    private final static String TAG_INVENTORY_LOGIN_FRAGMENT = "tag_inventory_login_fragment";
    private final static String TAG_PROJECTS_FRAGMENT = "tag_projects_fragment";

    private ActionBar mActionBar;
    private NavigationDrawer mNavigationDrawer;
    private FloatingFablabButton mFablabButton;
    private CartSlidingUpPanel mCartSlidingUpPanel;

    private ObjectGraph mObjectGraph;
    private EventBus mEventBus = EventBus.getDefault();

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mEventBus.post(new BackButtonPressedEvent());
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mActionBar.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // register the TopExceptionHandler
        Thread.setDefaultUncaughtExceptionHandler(new TopExceptionHandler(this));
        StackTraceReporter.reportStackTraceIfAvailable(this);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        StorageFragment storageFragment = (StorageFragment) getSupportFragmentManager()
                .findFragmentByTag(TAG_STORAGE_FRAGMENT);
        if (storageFragment == null) {
            storageFragment = new StorageFragment();
            getSupportFragmentManager().beginTransaction().add(storageFragment,
                    TAG_STORAGE_FRAGMENT).commit();
            getSupportFragmentManager().executePendingTransactions();
        }

        mObjectGraph = ObjectGraph.create(new ModelModule(storageFragment));

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,
                    new ICalAndNewsFragment(), TAG_ICAL_AND_NEWS_FRAGMENT).commit();
        }

        mActionBar = new ActionBar(this, findViewById(android.R.id.content));
        mNavigationDrawer = new NavigationDrawer(this, findViewById(android.R.id.content));
        mNavigationDrawer.restoreState(savedInstanceState);
        mFablabButton = new FloatingFablabButton(this, findViewById(android.R.id.content));
        mCartSlidingUpPanel = new CartSlidingUpPanel(this, findViewById(android.R.id.content));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mActionBar.bindMenuItems();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mActionBar.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case android.R.id.home:
                mEventBus.post(new BackButtonPressedEvent());
                getSupportFragmentManager().popBackStack();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mEventBus.unregister(this);
        mActionBar.pause();
        mCartSlidingUpPanel.pause();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mActionBar.onPostCreate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mEventBus.register(this);
        mActionBar.resume();
        mCartSlidingUpPanel.resume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mNavigationDrawer.saveState(outState);
    }

    public void inject(Object object) {
        mObjectGraph.inject(object);
    }

    public void setDisplayOptions(int options) {
        mActionBar.showLogo((options & DISPLAY_LOGO) == DISPLAY_LOGO);
        mActionBar.showTitle((options & DISPLAY_TITLE) == DISPLAY_TITLE);
        mActionBar.showTime((options & DISPLAY_TIME) == DISPLAY_TIME);
        boolean displayNavdrawer = (options & DISPLAY_NAVDRAWER) == DISPLAY_NAVDRAWER;
        mNavigationDrawer.enableDrawer(displayNavdrawer);
        mActionBar.showNavdrawerIcon(displayNavdrawer);
        mCartSlidingUpPanel.setVisibility((options & DISPLAY_CART_PANEL) == DISPLAY_CART_PANEL);
        mFablabButton.setVisibility((options & DISPLAY_FAB) == DISPLAY_FAB);
    }

    public void setNavigationDrawerSelection(int menuItemId) {
        mNavigationDrawer.setSelection(menuItemId);
    }

    public void setTitle(String title) {
        mActionBar.setTitle(title);
    }

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
                        Bundle args = new Bundle();
                        args.putSerializable(getResources().getString(R.string.key_show_cart_fab), true);
                        args.putSerializable(getResources().getString(R.string.key_product_search), true);
                        productSearchFragment = new ProductSearchFragment();
                        productSearchFragment.setArguments(args);
                    }
                    fragmentTransaction.replace(R.id.fragment_container, productSearchFragment,
                            TAG_PRODUCTSEARCH_FRAGMENT).addToBackStack(null).commit();
                }
                break;

            case CategorySearch:
                if (!TAG_CATEGORYSEARCH_FRAGMENT.equals(currentFragmentTag)) {
                    ProductSearchFragment productSearchFragment =
                            (ProductSearchFragment) getSupportFragmentManager().findFragmentByTag(
                                    TAG_CATEGORYSEARCH_FRAGMENT);
                    if (productSearchFragment == null) {
                        Bundle args = new Bundle();
                        args.putSerializable(getResources().getString(R.string.key_show_cart_fab), true);
                        args.putSerializable(getResources().getString(R.string.key_product_search), false);
                        productSearchFragment = new ProductSearchFragment();
                        productSearchFragment.setArguments(args);
                    }
                    fragmentTransaction.replace(R.id.fragment_container, productSearchFragment,
                            TAG_CATEGORYSEARCH_FRAGMENT).addToBackStack(null).commit();
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

            case Reservation:
                if (!TAG_RESERVATION_FRAGMENT.equals(currentFragmentTag)) {
                    ReservationFragment reservationFragment =
                            (ReservationFragment) getSupportFragmentManager().findFragmentByTag(
                                    TAG_RESERVATION_FRAGMENT);
                    if (reservationFragment == null) {
                        reservationFragment = new ReservationFragment();
                    }
                    fragmentTransaction.replace(R.id.fragment_container, reservationFragment,
                            TAG_RESERVATION_FRAGMENT).addToBackStack(null).commit();
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

            case Alert:
                if(!TAG_ALERT_FRAGMENT.equals(currentFragmentTag))
                {
                    fragmentTransaction.replace(R.id.fragment_container, new AlertDialogFragment(),
                            TAG_ALERT_FRAGMENT).addToBackStack(null).commit();
                }
                break;

            case InventoryLogin:
                if(!TAG_INVENTORY_LOGIN_FRAGMENT.equals(currentFragmentTag))
                {
                    fragmentTransaction.replace(R.id.fragment_container, new InventoryLoginFragment(),
                            TAG_INVENTORY_LOGIN_FRAGMENT).addToBackStack(null).commit();
                }
                break;

            case Projects:
                if(!TAG_PROJECTS_FRAGMENT.equals(currentFragmentTag))
                {
                    fragmentTransaction.replace(R.id.fragment_container, new ProjectFragment(),
                            TAG_PROJECTS_FRAGMENT).addToBackStack(null).commit();
                }
                break;

        }
        mNavigationDrawer.closeDrawer();
    }

    public void onEvent(NavigationEventInventory event)
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        String currentFragmentTag = getSupportFragmentManager().findFragmentById(
                R.id.fragment_container).getTag();
        if (!TAG_INVENTORY_FRAGMENT.equals(currentFragmentTag)) {

            Bundle args = new Bundle();
            args.putSerializable(getResources().getString(R.string.key_user), event.getUser());
            InventoryFragment inventoryFragment = new InventoryFragment();
            inventoryFragment.setArguments(args);

            fragmentTransaction.replace(R.id.fragment_container, inventoryFragment,
                    TAG_INVENTORY_FRAGMENT).addToBackStack(null).commit();
        }
        mNavigationDrawer.closeDrawer();
    }

    public void onEvent(NavigationEventReservation event)
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        String currentFragmentTag = getSupportFragmentManager().findFragmentById(
                R.id.fragment_container).getTag();
        if (!TAG_RESERVATION_FRAGMENT.equals(currentFragmentTag)) {

            Bundle args = new Bundle();
            args.putSerializable(getResources().getString(R.string.key_user), event.getUser());
            ReservationFragment reservationFragment = new ReservationFragment();
            reservationFragment.setArguments(args);

            fragmentTransaction.replace(R.id.fragment_container, reservationFragment,
                    TAG_RESERVATION_FRAGMENT).addToBackStack(null).commit();
        }
        mNavigationDrawer.closeDrawer();
    }

    public void onEvent(NavigationEventBarcodeScannerInventory event)
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        String currentFragmentTag = getSupportFragmentManager().findFragmentById(
                R.id.fragment_container).getTag();
        if (!TAG_BARCODE_INVENTORY_FRAGMENT.equals(currentFragmentTag)) {

            InventoryBarcodeScannerFragment barcodeScannerFragment = new InventoryBarcodeScannerFragment();

            Bundle args = new Bundle();
            args.putSerializable(getResources().getString(R.string.key_user), event.getUser());
            barcodeScannerFragment.setArguments(args);

            fragmentTransaction.replace(R.id.fragment_container, barcodeScannerFragment,
                    TAG_BARCODE_INVENTORY_FRAGMENT).addToBackStack(null).commit();
        }
        mNavigationDrawer.closeDrawer();
    }

    public void onEvent(NavigationEventProductSearchInventory event)
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        String currentFragmentTag = getSupportFragmentManager().findFragmentById(
                R.id.fragment_container).getTag();
        if (!TAG_PRODUCTSEARCH_INVENTORY_FRAGMENT.equals(currentFragmentTag)) {

            InventoryProductSearchFragment productSearchFragment = new InventoryProductSearchFragment();

            Bundle args = new Bundle();
            args.putSerializable(getResources().getString(R.string.key_user), event.getUser());
            args.putSerializable(getResources().getString(R.string.key_show_cart_fab), false);
            args.putSerializable(getResources().getString(R.string.key_product_search), event.getProductSearch());
            productSearchFragment.setArguments(args);

            fragmentTransaction.replace(R.id.fragment_container, productSearchFragment,
                    TAG_PRODUCTSEARCH_INVENTORY_FRAGMENT).addToBackStack(null).commit();
        }
        mNavigationDrawer.closeDrawer();
    }

    public void onEvent(NavigationEventShowInventory event)
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        String currentFragmentTag = getSupportFragmentManager().findFragmentById(
                R.id.fragment_container).getTag();
        if (!TAG_SHOWINVENTORY_INVENTORY_FRAGMENT.equals(currentFragmentTag)) {

            ShowInventoryFragment showInventoryFragment = new ShowInventoryFragment();

            Bundle args = new Bundle();
            args.putSerializable(getResources().getString(R.string.key_user), event.getUser());
            showInventoryFragment.setArguments(args);

            fragmentTransaction.replace(R.id.fragment_container, showInventoryFragment,
                    TAG_SHOWINVENTORY_INVENTORY_FRAGMENT).addToBackStack(null).commit();
        }
        mNavigationDrawer.closeDrawer();
    }

    public void onEvent(UpdateAvailableEvent event) {
        VersionCheckDialogFragment.newInstance(event.isRequired(), event.getMessage()).show(
                getSupportFragmentManager(), "versioncheck");
    }

    public void onEvent(ShowToastEvent event) {
        if(event.getLength() == Toast.LENGTH_SHORT)
            Toast.makeText(this, event.getMsg(), Toast.LENGTH_SHORT).show();
        else if(event.getLength() == Toast.LENGTH_LONG)
            Toast.makeText(this, event.getMsg(), Toast.LENGTH_LONG).show();
    }
}
