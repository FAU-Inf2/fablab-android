package de.fau.cs.mad.fablab.android;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import de.fau.cs.mad.fablab.android.basket.BasketActivity;
import de.fau.cs.mad.fablab.android.navdrawer.AppbarDrawerInclude;
import de.fau.cs.mad.fablab.android.ui.NewsActivity;

public class MainActivity extends ActionBarActivity {
    // Navigation Drawer
    private AppbarDrawerInclude appbarDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appbarDrawer = new AppbarDrawerInclude(this);
        appbarDrawer.create();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        appbarDrawer.createMenu(menu);
        Log.i("SET", "jo");
        appbarDrawer.startTimer();
        return true;
    }

    @Override
    public void onPause() {
        super.onPause();
        appbarDrawer.stopTimer();
    }

    @Override
    public void onResume() {
        super.onResume();
       appbarDrawer.startTimer();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_opened) {
            appbarDrawer.updateOpenState(item);
            Toast appbar_opened_toast = Toast.makeText(this, appbarDrawer.openedMessage, Toast.LENGTH_SHORT);
            appbar_opened_toast.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void startBarcodeScanner(View view) {
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Intent intent = new Intent(this, BarcodeScannerActivity.class);
            startActivity(intent);
        }
    }

    public void showBasket(View view){
        Intent intent = new Intent(this, BasketActivity.class);
        startActivity(intent);
    }

    public void startProductSearch(View view) {
        Intent intent = new Intent(this, ProductSearchActivity.class);
        startActivity(intent);
    }

    public void startNewsActivity(View view)
    {
        Intent intent = new Intent(this, NewsActivity.class);
        startActivity(intent);
    }


}
