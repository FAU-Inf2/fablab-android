package de.fau.cs.mad.fablab.android.basket;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.rest.entities.Product;

public class BasketActivity extends ActionBarActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);


        RecyclerView basket_rv = (RecyclerView)findViewById(R.id.basket_recycler_view);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        basket_rv.setLayoutManager(llm);
        basket_rv.setHasFixedSize(true);


        List<Product> basket = Basket.MYBASKET.getProducts();
        List<Integer> quantities = Basket.MYBASKET.getQuantities();

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(basket, quantities);
        basket_rv.setAdapter(adapter);

        TextView total_price = (TextView) findViewById(R.id.basket_total_price);

        int total = 0;
        for(int i=0;i<basket.size();i++){
            total += basket.get(i).getPrice()*quantities.get(i).intValue();
        }
        total_price.setText(total+" Euro");

        // Basket empty?
        if(basket.size() == 0){
            TextView title = (TextView) findViewById(R.id.basket_empty);
            title.setText(Html.fromHtml(getString(R.string.basket_empty)));
            title.setVisibility(View.VISIBLE);
        }
    }
}
