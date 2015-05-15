package de.fau.cs.mad.fablab.android.basket;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.util.List;

import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.db.DatabaseHelper;
import de.fau.cs.mad.fablab.rest.core.CartEntry;
import de.fau.cs.mad.fablab.rest.core.Product;


public class BasketActivity extends ActionBarActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);

        // Get Table "cartentries"-object
        RuntimeExceptionDao<CartEntry, Integer> dao = DatabaseHelper.getHelper(
                getApplicationContext()).getCartEntryDao();

        // Set Layout and Recyclerview
        RecyclerView basket_rv = (RecyclerView)findViewById(R.id.basket_recycler_view);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        basket_rv.setLayoutManager(llm);
        basket_rv.setHasFixedSize(true);


        Product p = new Product(1, "Schraube", 0.10 , 3, "drei", "Stück");
        CartEntry item = new CartEntry(p, 1);
        dao.create(item);

        // Get all CartEntries of cart from db
        List<CartEntry> cart_entries = dao.queryForAll();;

        // Add Entries to view
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(cart_entries);
        basket_rv.setAdapter(adapter);

        // Set up Listeners to be able to change the quantity for a product in the cart
        basket_rv.addOnItemTouchListener(new RecyclerItemClickListener(this, basket_rv, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // TODO Handle item click
                Log.i("app", "item click");
            }
            @Override
            public void onItemLongClick(View view, int position)
            {
                // TODO Handle item long click
                Log.i("app", "item long click");
            }

        }));

        // Compute total price
        TextView total_price = (TextView) findViewById(R.id.basket_total_price);
        int total = 0;
        for(int i=0;i<cart_entries.size();i++){
            total += cart_entries.get(i).getPrice()*cart_entries.get(i).getAmount();
        }
        total_price.setText(total+" Euro");

        // Basket empty?
        if(cart_entries.size() == 0){
            TextView title = (TextView) findViewById(R.id.basket_empty);
            title.setText(Html.fromHtml(getString(R.string.basket_empty)));
            title.setVisibility(View.VISIBLE);
        }
    }
}