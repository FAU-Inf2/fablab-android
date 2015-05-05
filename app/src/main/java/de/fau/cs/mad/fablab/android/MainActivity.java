package de.fau.cs.mad.fablab.android;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import de.fau.cs.mad.fablab.android.ORM.DBObjectView;
import de.fau.cs.mad.fablab.rest.TestClient;
import de.fau.cs.mad.fablab.common.WelcomeUser;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // REST-Client - example usage
        TestClient testClient = new TestClient(this);
        testClient.get().getWelcomeUser("FablabUser", new Callback<WelcomeUser>() {
            @Override
            public void success(WelcomeUser welcomeUser, Response response) {
                // success
                Log.i("App", welcomeUser.getMessage());
            }

            @Override
            public void failure(RetrofitError error) {
                // something went wrong
                Log.i("App", error.getMessage());
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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


    public void showORMButtonClicked(View view){
        Intent intent = new Intent(getApplicationContext(), DBObjectView.class);
        startActivity(intent);
    }

    public void startBarcodeScanner(View view) {
        Intent intent = new Intent(this, BarcodeScannerActivity.class);
        startActivity(intent);
    }
}
