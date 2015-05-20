package de.fau.cs.mad.fablab.android;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import java.util.regex.Pattern;

import de.fau.cs.mad.fablab.android.cart.AddToCartDialog;
import de.fau.cs.mad.fablab.rest.ProductApiClient;
import de.fau.cs.mad.fablab.rest.core.Product;
import eu.livotov.zxscan.ScannerView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class BarcodeScannerActivity extends ActionBarActivity
        implements ScannerView.ScannerViewEventListener, AddToCartDialog.DialogListener {
    private static final String TAG = BarcodeScannerActivity.class.getSimpleName();
    private ScannerFragment scannerFragment;
    private Pattern barcodePattern = Pattern.compile("200(00000)?\\d{5}");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);

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
    public boolean onCodeScanned(final String data) {
        scannerFragment.stopDecoding();

        if (barcodePattern.matcher(data).matches()) {
            long productId = Long.parseLong(data);
            if (data.length() == 13) {
                productId -= 2000000000000L;
            } else {
                productId -= 20000000L;
            }
            productId /= 10;

            ProductApiClient productApiClient = new ProductApiClient(this);
            productApiClient.get().findById(productId, new Callback<Product>() {
                @Override
                public void success(Product product, Response response) {
                    AddToCartDialog.newInstance(product).show(getSupportFragmentManager(),
                            "add_to_cart");
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e(TAG, error.getMessage());
                    AddToCartDialog.newInstance(null).show(getSupportFragmentManager(),
                            "add_to_cart");
                }
            });
            return true;
        }
        scannerFragment.startDecoding();
        return false;
    }

    @Override
    public void onScannerFailure(int cameraError) {

    }

    @Override
    public void onScannerReady() {

    }

    @Override
    public void onDialogDismissed() {
        scannerFragment.startDecoding();
    }
}