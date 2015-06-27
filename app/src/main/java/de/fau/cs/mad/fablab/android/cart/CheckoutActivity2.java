package de.fau.cs.mad.fablab.android.cart;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;

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
    final long REFRESH_MILLIS = 1 * 1000;
    private Cart cart;

    private static FragmentManager mFragmentManager;

    private final static String CART = "CART";

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentManager = getSupportFragmentManager();

        if(!iHaveNoAndroidDeviceUseNumberInsteadOfBarcodeScanner) {
            setContentView(R.layout.fragment_container);
            if (savedInstanceState == null) {
                mScannerFragment = ScannerFragment.newInstance(getResources().getString(
                        R.string.title_scan_qr_code));
                mFragmentManager.beginTransaction().add(R.id.fragment_container,
                        mScannerFragment, "scanner").commit();
            } else {
                mScannerFragment = (ScannerFragment) mFragmentManager.findFragmentByTag(
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

            mFragmentManager.beginTransaction().remove(mScannerFragment).commit();
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            cartID = qrCodeText;
        }else {
            cartID = idIfScannerIsNotUsed;
        }

        //get cart and cartEntries from CartSingleton
        cartDao = DatabaseHelper.getHelper(getApplication()).getCartDao();
        RuntimeExceptionDao<CartEntry, Long> productDao = DatabaseHelper.getHelper(getApplication()).getCartEntryDao();
        cart = CartSingleton.MYCART.getCart();
        cartDao.delete(cart);
        ForeignCollection<CartEntry> products = cart.getProducts();
        cart.setStatus(CartStatusEnum.PENDING);
        cart.setCartCode(cartID);
        cartDao.create(cart);

        //create from cart and cartEntries and cartServer and cartEntriesServer
        CartServer cartServer = new CartServer();
        List<CartEntryServer> productsServer = new ArrayList();
        for(CartEntry e : products)
        {
            CartEntryServer es = new CartEntryServer();
            productDao.delete(e);
            e.setCart(cart);
            productDao.create(e);
            es.setProductId(e.getProduct().getProductId());
            es.setAmount(e.getAmount());
            productsServer.add(es);
        }
        cartServer.setItems(productsServer);
        cartServer.setStatus(CartStatusEnum.PENDING);
        cartServer.setCartCode(cartID);
        cartServer.setPushId("000");



        //send cartServer to server
        final CartApiClient cartApiClient = new CartApiClient((this));
        System.out.println("SENDING CHART WITH ID: " + cartServer.getCartCode());

        cartApiClient.get().create(cartServer, new Callback<Response>() {
            @Override
            public void success(Response response1, Response response2) {
                BarCodeScannedDialogFragment fragment = new BarCodeScannedDialogFragment();
                fragment.show(mFragmentManager, "barcode scanned");
                startTimer(cartID);
            }

            @Override
            public void failure(RetrofitError error) {
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
                        PaidDialogFragment fragment = new PaidDialogFragment();
                        Bundle args = new Bundle();
                        args.putSerializable(CART, cart);
                        fragment.setArguments(args);
                        fragment.show(mFragmentManager, "fragment paid");
                        stopTimer();
                    }
                    else if(cartStatusEnum.equals(CartStatusEnum.CANCELLED))
                    {
                        CanceledDialogFragment fragment = new CanceledDialogFragment();
                        Bundle args = new Bundle();
                        args.putSerializable(CART, cart);
                        fragment.setArguments(args);
                        fragment.show(mFragmentManager, "fragment canceled");
                        stopTimer();
                    }
                    else
                    {
                        cartStatusHandler.postDelayed(cartStatusRunner, REFRESH_MILLIS);
                    }
                }

                @Override
                public void failure(RetrofitError error) {
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

