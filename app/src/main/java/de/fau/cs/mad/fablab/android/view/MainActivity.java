package de.fau.cs.mad.fablab.android.view;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.ButterKnife;
import dagger.ObjectGraph;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.actionbar.ActionBar;
import de.fau.cs.mad.fablab.android.eventbus.NavigationEvent;
import de.fau.cs.mad.fablab.android.model.StorageFragment;
import de.fau.cs.mad.fablab.android.model.dependencyinjection.ModelModule;
import de.fau.cs.mad.fablab.android.view.fragments.barcodescanner.BarcodeScannerFragment;
import de.fau.cs.mad.fablab.android.view.fragments.icals.ICalViewPagerFragment;
import de.fau.cs.mad.fablab.android.view.navdrawer.NavigationDrawer;
import de.fau.cs.mad.fablab.android.view.dependencyinjection.ActivityModule;
import de.fau.cs.mad.fablab.android.view.floatingbutton.FloatingFablabButton;
import de.fau.cs.mad.fablab.android.view.fragments.news.NewsFragment;
import de.greenrobot.event.EventBus;

public class MainActivity extends AppCompatActivity {

    private final static String TAG_NEWS_FRAGMENT = "tag_news_fragment";
    private final static String TAG_BARCODE_FRAGMENT = "tag_barcode_fragment";
    private final static String TAG_PRODUCTSEARCH_FRAGEMENT = "tag_productsearch_fragment";

    private ObjectGraph mObjectGraph;

    FloatingFablabButton fablabButton;
    NavigationDrawer navigationDrawer;
    ActionBar actionBar;

    EventBus eventbus = EventBus.getDefault();

    public void inject(Object object) {
        mObjectGraph.inject(object);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        StorageFragment storageFragment = (StorageFragment) getSupportFragmentManager()
                .findFragmentByTag("storage");
        if (storageFragment == null) {
            storageFragment = new StorageFragment();
            getSupportFragmentManager().beginTransaction().add(storageFragment, "storage").commit();
        }

        mObjectGraph = ObjectGraph.create(new ActivityModule(this), new ModelModule(storageFragment));

        if(savedInstanceState == null) {
            //getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new NewsFragment(), TAG_NEWS_FRAGMENT).commit();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new ICalViewPagerFragment(), TAG_NEWS_FRAGMENT).commit();
        }

        actionBar = new ActionBar(this, findViewById(android.R.id.content));
        fablabButton = new FloatingFablabButton(this, findViewById(android.R.id.content));
        navigationDrawer = new NavigationDrawer(this, findViewById(android.R.id.content));
    }

    private void navigate(NavigationEvent destination) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        switch(destination) {
            case News:
                NewsFragment newsFragment = (NewsFragment) getSupportFragmentManager().findFragmentByTag(TAG_NEWS_FRAGMENT);
                if(newsFragment == null) newsFragment = new NewsFragment();
                fragmentTransaction.replace(R.id.fragment_container, newsFragment, TAG_NEWS_FRAGMENT).commit();
                break;

            case BarcodeScanner:
                BarcodeScannerFragment barcodeScannerFragment = (BarcodeScannerFragment) getSupportFragmentManager().findFragmentByTag(TAG_BARCODE_FRAGMENT);
                if(barcodeScannerFragment == null) barcodeScannerFragment = new BarcodeScannerFragment();
                fragmentTransaction.replace(R.id.fragment_container, barcodeScannerFragment, TAG_BARCODE_FRAGMENT).commit();
                break;

            case ProductSearch:
                break;
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        eventbus.unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        eventbus.register(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        actionBar.bindMenuItems();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void onEvent(NavigationEvent destination) {
        navigate(destination);
    }
}
