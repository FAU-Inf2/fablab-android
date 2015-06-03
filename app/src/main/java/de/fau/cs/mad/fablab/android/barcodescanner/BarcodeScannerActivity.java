package de.fau.cs.mad.fablab.android.barcodescanner;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.util.regex.Pattern;

import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.cart.AddToCartDialog;
import de.fau.cs.mad.fablab.rest.ProductApiClient;
import de.fau.cs.mad.fablab.rest.core.Product;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class BarcodeScannerActivity extends ActionBarActivity
        implements ZXingScannerView.ResultHandler {
    private static final String TAG = BarcodeScannerActivity.class.getSimpleName();
    private Pattern mBarcodePattern = Pattern.compile("200(00000)?\\d{5}");
    private ScannerFragment mScannerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);

        if (savedInstanceState == null) {
            mScannerFragment = ScannerFragment.newInstance(getResources().getString(
                    R.string.title_scan_product));
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,
                    mScannerFragment, "scanner").commit();
        } else {
            mScannerFragment = (ScannerFragment) getSupportFragmentManager().findFragmentByTag(
                    "scanner");
        }
    }

    @Override
    public void handleResult(Result result) {
        String barcode = result.getText();
        boolean isEan8 = BarcodeFormat.EAN_8.equals(result.getBarcodeFormat());
        boolean isEan13 = BarcodeFormat.EAN_13.equals(result.getBarcodeFormat());

        if ((isEan8 || isEan13) && mBarcodePattern.matcher(barcode).matches()) {
            String productId;
            if (isEan13) {
                productId = barcode.substring(8, 12);
            } else {
                productId = barcode.substring(3, 7);
            }

            ProductApiClient productApiClient = new ProductApiClient(this);
            productApiClient.get().findById(productId, new Callback<Product>() {
                @Override
                public void success(Product product, Response response) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            AddToCartDialog.newInstance(product)).addToBackStack(null).commit();
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e(TAG, error.getMessage());
                    Toast.makeText(getApplicationContext(), R.string.product_not_found,
                            Toast.LENGTH_LONG).show();
                    mScannerFragment.startCamera();
                }
            });
        } else {
            Toast.makeText(this, R.string.barcode_scanner_invalid_barcode, Toast.LENGTH_LONG)
                    .show();
            mScannerFragment.startCamera();
        }
    }
}
