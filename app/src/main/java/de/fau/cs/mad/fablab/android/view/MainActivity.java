package de.fau.cs.mad.fablab.android.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import butterknife.ButterKnife;
import dagger.ObjectGraph;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.actionbar.ActionBar;
import de.fau.cs.mad.fablab.android.eventbus.NavigationEvent;
import de.fau.cs.mad.fablab.android.model.StorageFragment;
import de.fau.cs.mad.fablab.android.model.dependencyinjection.ModelModule;
import de.fau.cs.mad.fablab.android.util.TopExceptionHandler;
import de.fau.cs.mad.fablab.android.view.floatingbutton.FloatingFablabButton;
import de.fau.cs.mad.fablab.android.view.fragments.ICalAndNewsFragment;
import de.fau.cs.mad.fablab.android.view.fragments.barcodescanner.BarcodeScannerFragment;
import de.fau.cs.mad.fablab.android.view.fragments.cart.CartSlidingUpPanel;
import de.fau.cs.mad.fablab.android.view.fragments.productsearch.ProductSearchFragment;
import de.fau.cs.mad.fablab.android.view.navdrawer.NavigationDrawer;
import de.greenrobot.event.EventBus;

public class MainActivity extends AppCompatActivity {

    private final static String TAG_ICAL_AND_NEWS_FRAGMENT = "tag_ical_and_news_fragment";
    private final static String TAG_NEWS_FRAGMENT = "tag_news_fragment";
    private final static String TAG_BARCODE_FRAGMENT = "tag_barcode_fragment";
    private final static String TAG_PRODUCTSEARCH_FRAGMENT = "tag_productsearch_fragment";

    private ObjectGraph mObjectGraph;

    FloatingFablabButton fablabButton;
    NavigationDrawer navigationDrawer;
    ActionBar actionBar;
    CartSlidingUpPanel cartSlidingUpPanel;

    EventBus eventbus = EventBus.getDefault();

    public void inject(Object object) {
        mObjectGraph.inject(object);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // register the TopExceptionHandler
        Thread.setDefaultUncaughtExceptionHandler(new TopExceptionHandler(this));

        // see if stracktrace file is available
        String line;
        String trace = "";
        boolean traceAvailable = true;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(MainActivity.this.openFileInput("stack.trace")));
            while ((line = reader.readLine()) != null) {
                trace += line + "\n";
            }
        } catch (FileNotFoundException fnfe) {
            traceAvailable = false;
        } catch (IOException ioe) {
        }

        // if yes ask user to send stacktrace
        if (traceAvailable) {

            final String traceFinal = trace;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setPositiveButton(R.string.stacktrace_messaging_yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button
                    Intent sendIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto", " fablab@i2.cs.fau.de", null));
                    String subject = getResources().getString(R.string.stacktrace_messaging_subject);
                    String body = traceFinal + "\n\n";

                    sendIntent.putExtra(Intent.EXTRA_TEXT, body);
                    sendIntent.putExtra(Intent.EXTRA_SUBJECT, subject);

                    MainActivity.this.startActivity(Intent.createChooser(sendIntent, getResources().getString(R.string.stacktrace_messaging_chooser_title)));
                }
            });
            builder.setNegativeButton(R.string.stacktrace_messaging_no, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                }
            });

            builder.setTitle(getResources().getString(R.string.stacktrace_messaging_dialog_title));
            builder.setMessage(getResources().getString(R.string.stacktrace_messaging_dialog_text));
            AlertDialog dialog = builder.create();
            dialog.show();


            MainActivity.this.deleteFile("stack.trace");
        }



        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        StorageFragment storageFragment = (StorageFragment) getSupportFragmentManager()
                .findFragmentByTag("storage");
        if (storageFragment == null) {
            storageFragment = new StorageFragment();
            getSupportFragmentManager().beginTransaction().add(storageFragment, "storage").commit();
        }

        mObjectGraph = ObjectGraph.create(new ModelModule(storageFragment));

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new ICalAndNewsFragment(), TAG_ICAL_AND_NEWS_FRAGMENT).commit();
        }

        actionBar = new ActionBar(this, findViewById(android.R.id.content));
        fablabButton = new FloatingFablabButton(this, findViewById(android.R.id.content));
        navigationDrawer = new NavigationDrawer(this, findViewById(android.R.id.content));
    }

    private void navigate(NavigationEvent destination) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        switch(destination) {
            case News:
                cartSlidingUpPanel.setVisibility(true);
                ICalAndNewsFragment iCalAndNewsFragment = (ICalAndNewsFragment) getSupportFragmentManager().findFragmentByTag(TAG_ICAL_AND_NEWS_FRAGMENT);
                if(iCalAndNewsFragment == null) iCalAndNewsFragment = new ICalAndNewsFragment();
                fragmentTransaction.replace(R.id.fragment_container, iCalAndNewsFragment, TAG_ICAL_AND_NEWS_FRAGMENT).commit();
                break;

            case BarcodeScanner:
                cartSlidingUpPanel.setVisibility(false);
                BarcodeScannerFragment barcodeScannerFragment = (BarcodeScannerFragment) getSupportFragmentManager().findFragmentByTag(TAG_BARCODE_FRAGMENT);
                if(barcodeScannerFragment == null) barcodeScannerFragment = new BarcodeScannerFragment();
                fragmentTransaction.replace(R.id.fragment_container, barcodeScannerFragment, TAG_BARCODE_FRAGMENT).commit();
                break;

            case ProductSearch:
                cartSlidingUpPanel.setVisibility(true);
                ProductSearchFragment productSearchFragment = (ProductSearchFragment) getSupportFragmentManager().findFragmentByTag(TAG_PRODUCTSEARCH_FRAGMENT);
                if(productSearchFragment == null) productSearchFragment = new ProductSearchFragment();
                fragmentTransaction.replace(R.id.fragment_container, productSearchFragment, TAG_PRODUCTSEARCH_FRAGMENT).commit();
                break;
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        eventbus.unregister(this);
        cartSlidingUpPanel.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cartSlidingUpPanel = new CartSlidingUpPanel(this, findViewById(android.R.id.content));
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
