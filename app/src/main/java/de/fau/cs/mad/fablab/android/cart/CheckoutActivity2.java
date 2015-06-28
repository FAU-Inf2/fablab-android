package de.fau.cs.mad.fablab.android.cart;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import java.util.Random;

public class CheckoutActivity2 extends ActionBarActivity {

    private String cartID;
    //private RuntimeExceptionDao<Cart, String> cartDao;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //create random ID
        Random random = new Random();
        long cartIDLong = random.nextLong();
        cartID = Long.toString(cartIDLong);

        //create cart
        /*Cart cart = CartSingleton.MYCART.getCart();
        cart.setStatus(CartStatus.PENDING);
        cart.setCartCode(cartID);

        //save cart in database
        cartDao = DatabaseHelper.getHelper(getApplicationContext()).getCartDao();
        cartDao.create(cart);*/
        
        //TODO adapt to updated common classes here
/*
        CartApiClient cartApiClient = new CartApiClient((this));
        cartApiClient.get().create(cart, new Callback<Response>() {
            @Override
            public void success(Response response1, Response response2) {
                Toast.makeText(getApplicationContext(), "bezahlen!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext(), "Retrofit error", Toast.LENGTH_SHORT).show();
            }
        });
*/

    }

}
