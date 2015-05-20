package de.fau.cs.mad.fablab.android.cart;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.ScannerFragment;
import de.fau.cs.mad.fablab.rest.CartApiClient;
import de.fau.cs.mad.fablab.rest.core.CartEntry;
import eu.livotov.zxscan.ScannerView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CheckoutActivity extends ActionBarActivity implements ScannerView.ScannerViewEventListener {
    private static final String TAG = CheckoutActivity.class.getSimpleName();
    private ScannerFragment scannerFragment;
    private ProgressBar progressBar;
    private Pattern qrCodePattern = Pattern.compile("(-)?\\d{1,19}");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        if (savedInstanceState == null) {
            if (scannerFragment == null) {
                scannerFragment = new ScannerFragment();
                scannerFragment.setScannerViewEventListener(this);
            }

            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,
                    scannerFragment).commit();
        }
    }

    @Override
    public boolean onCodeScanned(String data) {
        if (qrCodePattern.matcher(data).matches()) {
            long cartId;
            try {
                cartId = Long.parseLong(data);
            } catch (NumberFormatException e) {
                return false;
            }
            getSupportFragmentManager().beginTransaction().remove(scannerFragment).commit();
            progressBar.setVisibility(View.VISIBLE);

            List<CartEntry> products = Cart.MYCART.getProducts();
            de.fau.cs.mad.fablab.rest.core.Cart cart = new de.fau.cs.mad.fablab.rest.core.Cart();
            cart.setProducts(new ArrayList<>(products));
            cart.setId(cartId);

            CartApiClient cartApiClient = new CartApiClient((this));
            cartApiClient.get().create(cart, new Callback<de.fau.cs.mad.fablab.rest.core.Cart>() {
                @Override
                public void success(de.fau.cs.mad.fablab.rest.core.Cart cart, Response response) {
                    progressBar.setVisibility(View.GONE);
                    getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,
                            CheckoutFragment.newInstance()).commit();
                }

                @Override
                public void failure(RetrofitError error) {
                    progressBar.setVisibility(View.GONE);
                    getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,
                            CheckoutFragment.newInstance(true)).commit();
                }
            });
            return true;
        }
        return false;
    }

    @Override
    public void onScannerFailure(int cameraError) {

    }

    @Override
    public void onScannerReady() {

    }

    public void finish(View view) {
        if (((Button) view).getText().toString().equals("Bezahlt")) {
            Cart.MYCART.removeAllEntries();
        }
        finish();
    }
}
