package de.fau.cs.mad.fablab.android.cart;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.util.List;

import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.db.DatabaseHelper;
import de.fau.cs.mad.fablab.android.navdrawer.AppbarDrawerInclude;
import de.fau.cs.mad.fablab.rest.core.CartEntry;


public class CartActivity extends ActionBarActivity{

    private List<CartEntry> cart_entries;
    private Dialog dialog;
    private RecyclerViewAdapter adapter;
    private AppbarDrawerInclude appbarDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        appbarDrawer = new AppbarDrawerInclude(this);
        appbarDrawer.create();

        // Set Layout and Recyclerview
        RecyclerView cart_rv = (RecyclerView)findViewById(R.id.cart_recycler_view);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        cart_rv.setLayoutManager(llm);
        cart_rv.setHasFixedSize(true);


        // Get all Cart Entries of cart from db
        cart_entries = Cart.MYCART.getProducts();

        // Add Entries to view
        adapter = new RecyclerViewAdapter(cart_entries);
        cart_rv.setAdapter(adapter);

        // Set up Listeners to be able to change the quantity for a product in the cart (popup)
        cart_rv.addOnItemTouchListener(new RecyclerItemClickListener(this, cart_rv, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(CartActivity.this, R.string.cart_entry_popup_info, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onItemLongClick(View view, final int position)
            {
                dialog = new Dialog(CartActivity.this);
                dialog.setContentView(R.layout.cart_entry_dialog);
                dialog.setTitle(R.string.cart_entry_popup_title);

                CartEntry temp = cart_entries.get(position);
                EditText amount = (EditText) dialog.findViewById(R.id.cart_entry_popup_input);
                amount.setText(String.valueOf(temp.getAmount()));
                TextView unit = (TextView) dialog.findViewById(R.id.cart_entry_popup_unit);
                if(temp.getUnit() == null){
                    unit.setText(R.string.cart_entry_unit_null_exception);
                }else{
                    unit.setText(temp.getUnit());
                }

                Button button_ok = (Button) dialog.findViewById(R.id.cart_entry_popup_ok);
                Button button_cancel = (Button) dialog.findViewById(R.id.cart_entry_popup_cancel);

                // close popup
                button_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                // save changes
                button_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView temp = (TextView) dialog.findViewById(R.id.cart_entry_popup_input);
                        if(temp.getText().toString().isEmpty()){
                            Toast.makeText(CartActivity.this,  R.string.cart_entry_popup_valid_amount, Toast.LENGTH_LONG).show();
                            return;
                        }
                        double amount_new = Double.parseDouble(temp.getText().toString());
                        cart_entries.get(position).setAmount(amount_new);

                        if(amount_new == 0.0){
                            Cart.MYCART.removeEntry(cart_entries.get(position));
                            adapter.notifyItemRemoved(position);
                        }else{
                            Cart.MYCART.updateEntry(cart_entries.get(position));
                            adapter.notifyItemChanged(position);
                        }
                        refreshTotalPrice();
                        dialog.dismiss();
                    }
                });

                // open popup
                dialog.show();
            }

        }));

        // set total price
        refreshTotalPrice();

        // Basket empty?
        if(cart_entries.size() == 0){
            Toast.makeText(CartActivity.this,  R.string.cart_empty, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        appbarDrawer.createMenu(menu);
        appbarDrawer.startTimer();
        return true;
    }

    @Override
    public void onPause() {
        super.onPause();
        appbarDrawer.stopTimer();
    }

    @Override
    public void onResume() {
        super.onResume();
        appbarDrawer.startTimer();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_opened) {
            appbarDrawer.updateOpenState(item);
            Toast appbar_opened_toast = Toast.makeText(this, appbarDrawer.openedMessage, Toast.LENGTH_SHORT);
            appbar_opened_toast.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void refreshTotalPrice(){
        TextView total_price = (TextView) findViewById(R.id.cart_total_price);
        total_price.setText(Cart.MYCART.totalPrice() + " €");
    }
}