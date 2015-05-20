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
    private ScannerView scannerView;
    private String lastBarcodeScanned;
    private long lastTimeScanned;
    private boolean isDialogStarted;
    private Pattern barcodePattern = Pattern.compile("200(00000)?\\d{5}");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcodescanner);
        scannerView = (ScannerView) findViewById(R.id.scanner);
        scannerView.setScannerViewEventListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        scannerView.startScanner();
    }

    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopScanner();
    }

    @Override
    public boolean onCodeScanned(final String data) {
        if ((data.equals(lastBarcodeScanned) && (System.currentTimeMillis() - lastTimeScanned) < 5000)
                || isDialogStarted) {
            return false;
        }
        lastBarcodeScanned = data;
        lastTimeScanned = System.currentTimeMillis();
        isDialogStarted = true;

        if (barcodePattern.matcher(data).matches()) {
            long productId = Long.parseLong(data);
            if (data.length() == 13) {
                productId -= 2000000000000L;
            } else {
                productId -= 20000000L;
            }
            productId /= 10;

            final AddToCartDialog.DialogListener listener = this;
            ProductApiClient productApiClient = new ProductApiClient(this);
            productApiClient.get().findById(productId, new Callback<Product>() {
                @Override
                public void success(Product product, Response response) {
                    startAddToCartDialog(product, listener);
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e(TAG, error.getMessage());
                    startAddToCartDialog(null, listener);
                }
            });
            return true;
        }
        isDialogStarted = false;
        return false;
    }

    private void startAddToCartDialog(Product product, AddToCartDialog.DialogListener listener) {
        AddToCartDialog dialog = AddToCartDialog.newInstance(product);
        dialog.setDialogListener(listener);
        dialog.show(getSupportFragmentManager(), "add_to_cart_dialog");
    }

    @Override
    public void onScannerFailure(int cameraError) {

    }

    @Override
    public void onScannerReady() {

    }

    @Override
    public void onDialogDismissed() {
        isDialogStarted = false;
    }
}
