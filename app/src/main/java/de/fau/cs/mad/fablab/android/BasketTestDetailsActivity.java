package de.fau.cs.mad.fablab.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.j256.ormlite.dao.RuntimeExceptionDao;

import de.fau.cs.mad.fablab.android.basket.BasketItem;
import de.fau.cs.mad.fablab.android.db.DatabaseHelper;


public class BasketTestDetailsActivity extends ActionBarActivity {
    private RuntimeExceptionDao<BasketItem, Integer> dao;
    private BasketItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket_test_details);
        dao = DatabaseHelper.getHelper(getApplicationContext()).getBasketItemDao();
        item = dao.queryForId(getIntent().getIntExtra("BasketItem", -1));

        if (item == null) {
            findViewById(R.id.btnDelete).setVisibility(View.INVISIBLE);
        } else {
            TextView productId = (TextView) findViewById(R.id.productId);
            TextView quantity = (TextView) findViewById(R.id.quantity);

            productId.setText(item.getProductId() + "");
            productId.setEnabled(false);
            quantity.setText(item.getQuantity() + "");
        }
    }

    public void backButtonClicked(View v) {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    public void deleteButtonClicked(View v) {
        dao.delete(item);
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    public void saveButtonClicked(View v) {
        String productId = ((EditText) findViewById(R.id.productId)).getText().toString();
        String quantity = ((EditText) findViewById(R.id.quantity)).getText().toString();

        if (!productId.isEmpty() && !quantity.isEmpty()) {
            if (item == null) {
                item = new BasketItem(Integer.parseInt(productId), Integer.parseInt(quantity));
                dao.create(item);
            } else {
                item.setQuantity(Integer.parseInt(quantity));
                dao.update(item);
            }
        }

        Intent intent = new Intent(getApplicationContext(), BasketTestActivity.class);
        setResult(RESULT_OK, intent);
        finish();
    }
}
