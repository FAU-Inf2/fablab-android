package de.fau.cs.mad.fablab.android.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.ButterKnife;
import butterknife.InjectView;
import dagger.ObjectGraph;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.eventbus.NavigationEvent;
import de.fau.cs.mad.fablab.android.model.StorageFragment;
import de.fau.cs.mad.fablab.android.model.dependencyinjection.ModelModule;
import de.fau.cs.mad.fablab.android.view.fragments.barcodescanner.BarcodeScannerFragment;
import de.fau.cs.mad.fablab.android.view.navdrawer.NavigationDrawer;
import de.fau.cs.mad.fablab.android.view.dependencyinjection.ActivityModule;
import de.fau.cs.mad.fablab.android.view.floatingbutton.FloatingFablabButton;
import de.fau.cs.mad.fablab.android.view.fragments.news.NewsFragment;
import de.greenrobot.event.EventBus;

public class MainActivity extends AppCompatActivity {

    private ObjectGraph mObjectGraph;

    FloatingFablabButton fablabButton;
    NavigationDrawer navigationDrawer;

    @InjectView(R.id.appbar)
    Toolbar toolbar;

    EventBus eventbus = EventBus.getDefault();

    NewsFragment newsfragment = new NewsFragment();
    BarcodeScannerFragment barcodeScannerFragment = new BarcodeScannerFragment();

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

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new NewsFragment()).commit();
        }

        eventbus.register(this);
    }

    private void navigate(NavigationEvent destination) {
        switch(destination) {
            case News:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, newsfragment).commit();
                break;

            case BarcodeScanner:

                break;

            case ProductSearch:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, barcodeScannerFragment).commit();
                break;
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fablabButton = new FloatingFablabButton(this, findViewById(android.R.id.content));
        navigationDrawer = new NavigationDrawer(this, findViewById(android.R.id.content));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
