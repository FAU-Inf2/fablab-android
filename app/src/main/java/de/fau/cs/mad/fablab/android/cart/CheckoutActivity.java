package de.fau.cs.mad.fablab.android.cart;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.util.List;
import java.util.regex.Pattern;

import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.barcodescanner.ScannerFragment;
import de.fau.cs.mad.fablab.rest.core.CartEntry;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class CheckoutActivity extends ActionBarActivity implements ZXingScannerView.ResultHandler {
    private Pattern mQrCodePattern = Pattern.compile("(-)?\\d{1,19}");
    private ScannerFragment mScannerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);

        if (savedInstanceState == null) {
            mScannerFragment = ScannerFragment.newInstance(getResources().getString(
                    R.string.title_scan_qr_code));
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,
                    mScannerFragment, "scanner").commit();
        } else {
            mScannerFragment = (ScannerFragment) getSupportFragmentManager().findFragmentByTag(
                    "scanner");
        }
    }

    @Override
    public void handleResult(Result result) {
        String qrCodeText = result.getText();

        if (mQrCodePattern.matcher(qrCodeText).matches()
                && BarcodeFormat.QR_CODE.equals(result.getBarcodeFormat())) {
            getSupportFragmentManager().beginTransaction().remove(mScannerFragment).commit();
            getSupportActionBar().setDisplayShowTitleEnabled(false);

            List<CartEntry> products = CartSingleton.MYCART.getProducts();
            de.fau.cs.mad.fablab.rest.core.Cart cart = new de.fau.cs.mad.fablab.rest.core.Cart();
            //cart.setProducts(new ArrayList<>(products));
            //cart.setId(Long.toString(cartId));

            /*
            CartApiClient cartApiClient = new CartApiClient((this));
            cartApiClient.get().create(cart, new Callback<de.fau.cs.mad.fablab.rest.core.Cart>() {
                @Override
                public void success(de.fau.cs.mad.fablab.rest.core.Cart cart, Response response) {
                    getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,
                            CheckoutFragment.newInstance()).commit();
                }

                @Override
                public void failure(RetrofitError error) {
                    getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,
                            CheckoutFragment.newInstance(true)).commit();
                }
            });
            */
        } else {
            Toast.makeText(this, R.string.qr_code_scanner_invalid_qr_code, Toast.LENGTH_LONG).show();
            mScannerFragment.startCamera();
        }
    }

    public void finish(View view) {
        if (((Button) view).getText().toString().equals("Bezahlt")) {
            CartSingleton.MYCART.removeAllEntries();
        }
        finish();
    }
}
