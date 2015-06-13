package de.fau.cs.mad.fablab.android.view;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.model.StorageFragment;
import de.fau.cs.mad.fablab.android.view.floatingbutton.FloatingFablabButton;
import de.fau.cs.mad.fablab.android.view.fragments.news.NewsFragment;

public class MainActivity extends ActionBarActivity {

    FloatingFablabButton fablabButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new NewsFragment()).commit();
            //initialize our storage
            StorageFragment.initializeStorage(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        StorageFragment.initializeStorage(this);
        fablabButton = new FloatingFablabButton(this, findViewById(android.R.id.content));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
