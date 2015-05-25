package de.fau.cs.mad.fablab.android;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import de.fau.cs.mad.fablab.android.cart.Cart;
import de.fau.cs.mad.fablab.android.cart.CartActivity;
import de.fau.cs.mad.fablab.android.cart.CheckoutActivity;
import de.fau.cs.mad.fablab.android.productsearch.AutoCompleteHelper;
import de.fau.cs.mad.fablab.android.productsearch.ProductSearchActivity;
import de.fau.cs.mad.fablab.android.ui.NewsActivity;

public class MainActivity extends BaseActivity {

    public void baseOnCreate(Bundle savedInstanceState) {
        //Load Autocompleteionwords
        AutoCompleteHelper.getInstance().loadProductNames(this);

        // init db and cart - always do this on app start
        Cart.MYCART.init(getApplication());
        //initCartPanel(false);
        initFabButton();
    }

    @Override
    protected void baseSetContentView() {
        setContentView(R.layout.activity_main);
    }


    @Override
    protected void baseOnResume() {
        //Load Autocompleteionwords
        AutoCompleteHelper.getInstance().loadProductNames(this);
    }

    public void startBarcodeScanner(View view) {
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Intent intent = new Intent(this, BarcodeScannerActivity.class);
            startActivity(intent);
        }
    }

    public void showBasket(View view){
        Intent intent = new Intent(this, CartActivity.class);
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

    public void startCheckout(View view) {
        Intent intent = new Intent(this, CheckoutActivity.class);
        startActivity(intent);
    }
}
