package de.fau.cs.mad.fablab.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.util.List;

import de.fau.cs.mad.fablab.android.basket.BasketItem;
import de.fau.cs.mad.fablab.android.db.DatabaseHelper;


public class BasketTestActivity extends ActionBarActivity {
    private List<BasketItem> basketItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket_test);
        populateListView();
        registerClickCallback();
    }

    private void populateListView(){
        RuntimeExceptionDao<BasketItem, Integer> dao = DatabaseHelper.getHelper(
                getApplicationContext()).getBasketItemDao();
        basketItems = dao.queryForAll();

        ListView listView = (ListView) findViewById(R.id.basket_items_list_view);
        listView.setAdapter(new ArrayAdapter<BasketItem>(getBaseContext(),
                android.R.layout.simple_list_item_2, android.R.id.text1, basketItems.toArray(
                new BasketItem[basketItems.size()])) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                TextView text1 = (TextView) v.findViewById(android.R.id.text1);
                TextView text2 = (TextView) v.findViewById(android.R.id.text2);

                text1.setText("ID: " + getItem(position).getProductId() + "");
                text2.setText("Quantity: " + getItem(position).getQuantity() + "");

                return v;
            }
        });
    }

    private void registerClickCallback(){
        ListView listView = (ListView) findViewById(R.id.basket_items_list_view);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), BasketTestDetailsActivity.class);
                intent.putExtra("BasketItem", basketItems.get(position).getProductId());
                startActivityForResult(intent, 1);
            }
        });
    }

    public void createNewEntryButtonClicked(View view){
        Intent intent = new Intent(getApplicationContext(), BasketTestDetailsActivity.class);
        startActivityForResult(intent, 1);
    }


    public void backButtonClicked(View v){
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        populateListView();
    }
}
