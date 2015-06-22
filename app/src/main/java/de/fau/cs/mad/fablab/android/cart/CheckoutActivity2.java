package de.fau.cs.mad.fablab.android.cart;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import com.google.zxing.Result;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.util.ArrayList;
import java.util.List;

import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.barcodescanner.ScannerFragment;
import de.fau.cs.mad.fablab.android.db.DatabaseHelper;
import de.fau.cs.mad.fablab.rest.CartApiClient;
import de.fau.cs.mad.fablab.rest.core.Cart;
import de.fau.cs.mad.fablab.rest.core.CartEntry;
import de.fau.cs.mad.fablab.rest.core.CartEntryServer;
import de.fau.cs.mad.fablab.rest.core.CartServer;
import de.fau.cs.mad.fablab.rest.core.CartStatusEnum;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CheckoutActivity2 extends ActionBarActivity implements ZXingScannerView.ResultHandler{

    private String cartID;
    private RuntimeExceptionDao<Cart, String> cartDao;
    private ScannerFragment mScannerFragment;
    //Maybe we can/should set this if Emulator is used?
    private boolean iHaveNoAndroidDeviceUseNumberInsteadOfBarcodeScanner = false;
    private String idIfScannerIsNotUsed = "256";
    private Handler cartStatusHandler = new Handler();
    private Runnable cartStatusRunner;
    final long REFRESH_MILLIS = 30 * 1000;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!iHaveNoAndroidDeviceUseNumberInsteadOfBarcodeScanner) {
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
        }else{
            handleResult(null);
        }

    }

    @Override
    public void handleResult(Result result) {

        if(!iHaveNoAndroidDeviceUseNumberInsteadOfBarcodeScanner) {
            String qrCodeText = result.getText();

            getSupportFragmentManager().beginTransaction().remove(mScannerFragment).commit();
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            cartID = qrCodeText;
        }else {
            cartID = idIfScannerIsNotUsed;
        }

        //get cart and cartEntries from CartSingleton
        Cart cart = CartSingleton.MYCART.getCart();
        ForeignCollection<CartEntry> products = cart.getProducts();
        //cart.setStatus(CartStatusEnum.PENDING);
        //cart.setCartCode(cartID);

        //create from cart and cartEntries and cartServer and cartEntriesServer
        CartServer cartServer = new CartServer();
        List<CartEntryServer> productsServer = new ArrayList();
        for(CartEntry e : products)
        {
            CartEntryServer es = new CartEntryServer();
            es.setProductId(e.getProduct().getProductId());
            es.setAmount(e.getAmount());
            productsServer.add(es);
        }
        cartServer.setItems(productsServer);
        cartServer.setStatus(CartStatusEnum.PENDING);
        cartServer.setCartCode(cartID);
        cartServer.setPushId("000");

        //save cart in database
        cartDao = DatabaseHelper.getHelper(getApplicationContext()).getCartDao();
        //cartDao.create(cart);

        //send cartServer to server
        final CartApiClient cartApiClient = new CartApiClient((this));
        System.out.println("SENDING CHART WITH ID: " + cartServer.getCartCode());

        cartApiClient.get().create(cartServer, new Callback<Response>() {
            @Override
            public void success(Response response1, Response response2) {
                Toast.makeText(getApplicationContext(), "Bitte am Kassenterinal bezahlen, oder Bezahlvorgang abbrechen.", Toast.LENGTH_SHORT).show();
                startTimer(cartID);
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext(), "Retrofit error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    class CartStatusRunner implements Runnable
    {
        final CartApiClient cartApiClient;
        String id;

        public CartStatusRunner(String id) {
            this.cartApiClient =new CartApiClient(getApplication());
            this.id = id;
        }

        @Override
        public void run() {
            cartApiClient.get().getStatus(id, new Callback<CartStatusEnum>() {
                @Override
                public void success(CartStatusEnum cartStatusEnum, Response response) {
                    if(cartStatusEnum.equals(CartStatusEnum.PAID))
                    {
                        Toast.makeText(getApplicationContext(), "bezahlt", Toast.LENGTH_SHORT).show();
                        stopTimer();
                    }
                    else if(cartStatusEnum.equals(CartStatusEnum.CANCELLED))
                    {
                        Toast.makeText(getApplicationContext(), "abgebrochen!", Toast.LENGTH_SHORT).show();
                        stopTimer();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "else", Toast.LENGTH_SHORT).show();
                        cartStatusHandler.postDelayed(cartStatusRunner, REFRESH_MILLIS);
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(getApplicationContext(), "Retrofit Error Status", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void startTimer(String id) {
        cartStatusRunner = new CartStatusRunner(id);
        cartStatusHandler.post(cartStatusRunner);
    }

    public void stopTimer() {
        cartStatusHandler.removeCallbacks(cartStatusRunner);
    }
}
