package de.fau.cs.mad.fablab.android;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.util.regex.Pattern;

import de.fau.cs.mad.fablab.android.cart.AddToCartDialog;
import de.fau.cs.mad.fablab.android.db.DatabaseHelper;
import de.fau.cs.mad.fablab.rest.ProductApiClient;
import de.fau.cs.mad.fablab.rest.core.CartEntry;
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
    private Product scannedProduct;

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

            ProductApiClient productApiClient = new ProductApiClient(this);
            productApiClient.get().findById(productId, new Callback<Product>() {
                @Override
                public void success(Product product, Response response) {
                    scannedProduct = product;
                    AddToCartDialog.newInstance(product.getName(), product.getUnit(),
                            product.getPrice()).show(getSupportFragmentManager(), "add_to_cart");
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e(TAG, error.getMessage());
                    AddToCartDialog.newInstance().show(getSupportFragmentManager(),
                            "add_to_cart");
                }
            });
            return true;
        }
        isDialogStarted = false;
        return false;
    }

    @Override
    public void onScannerFailure(int cameraError) {

    }

    @Override
    public void onScannerReady() {

    }

    @Override
    public void onDialogPositiveClick() {
        Product product = scannedProduct;
        isDialogStarted = false;
        RuntimeExceptionDao<CartEntry, Long> dao = DatabaseHelper.getHelper(getApplicationContext())
                .getCartEntryDao();
        CartEntry cartEntry = dao.queryForId(product.getProductId());
        if (cartEntry != null) {
            cartEntry.setAmount(cartEntry.getAmount() + 1);
            dao.update(cartEntry);
        } else {
            cartEntry = new CartEntry(product, 1);
            dao.create(cartEntry);
        }
    }

    @Override
    public void onDialogNegativeClick() {
        isDialogStarted = false;
    }
}
