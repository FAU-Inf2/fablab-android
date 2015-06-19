package de.fau.cs.mad.fablab.android.cart;

import android.os.Bundle;
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

    @Override
    public void onCreate(final Bundle savedInstanceState) {
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

        getSupportFragmentManager().beginTransaction().remove(mScannerFragment).commit();
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //create random ID
        //Random random = new Random();
        //long cartIDLong = random.nextLong();
        //cartID = Long.toString(cartIDLong);
        cartID = qrCodeText;

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
        cartServer.setPushId("");

        //save cart in database
        cartDao = DatabaseHelper.getHelper(getApplicationContext()).getCartDao();
        //cartDao.create(cart);

        //send cartServer to server
        CartApiClient cartApiClient = new CartApiClient((this));
        cartApiClient.get().create(cartServer, new Callback<Response>() {
            @Override
            public void success(Response response1, Response response2) {
                Toast.makeText(getApplicationContext(), "bezahlen!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext(), "Retrofit error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
