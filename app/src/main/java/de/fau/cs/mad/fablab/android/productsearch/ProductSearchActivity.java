package de.fau.cs.mad.fablab.android.productsearch;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.List;

import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.cart.AddToCartDialog;
import de.fau.cs.mad.fablab.android.cart.Cart;
import de.fau.cs.mad.fablab.android.cart.RecyclerItemClickListener;
import de.fau.cs.mad.fablab.android.cart.RecyclerViewAdapter;
import de.fau.cs.mad.fablab.android.navdrawer.AppbarDrawerInclude;
import de.fau.cs.mad.fablab.android.productMap.ProductMapActivity;
import de.fau.cs.mad.fablab.rest.ProductApiClient;
import de.fau.cs.mad.fablab.rest.core.CartEntry;
import de.fau.cs.mad.fablab.rest.core.Product;
import de.fau.cs.mad.fablab.rest.myapi.ProductApi;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ProductSearchActivity extends ActionBarActivity
        implements ProductDialog.ProductDialogListener, AddToCartDialog.DialogListener {

    private RecyclerView.LayoutManager layoutManager;
    private RecyclerViewAdapter productAdapter;
    private SlidingUpPanelLayout mLayout;
    private AppbarDrawerInclude appbarDrawer;
    //only for testing
    private ArrayList<Product> productEntries;
    //our rest-callback interface
    private ProductApi mProductApi;
    private ProductDialog productDialog;
    private Product selectedProduct;

    //This callback is used for product Search.
    private Callback<List<Product>> mSearchCallback = new Callback<List<Product>>() {
        @Override
        public void success(List<Product> products, Response response) {
            if (products.isEmpty()) {
                Toast.makeText(getBaseContext(), R.string.product_not_found, Toast.LENGTH_LONG).show();
            }
            for (Product product : products) {
                productAdapter.addProduct(new CartEntry(product, 1));
                productEntries.add(product);
            }
        }

        @Override
        public void failure(RetrofitError error) {
            Toast.makeText(getBaseContext(), R.string.retrofit_callback_failure, Toast.LENGTH_LONG).show();
        }
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_search);

        appbarDrawer = new AppbarDrawerInclude(this);
        appbarDrawer.create();

        // Setting up Basket and SlidingUpPanel
        Cart.MYCART.setSlidingUpPanel(this, findViewById(android.R.id.content), true);

        //get search view and set searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) findViewById(R.id.product_search_view);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        //do not iconify search view
        searchView.setIconified(false);

        //get recycler view and set layout manager and adapter
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.product_recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        productAdapter = new RecyclerViewAdapter(new ArrayList<CartEntry>());
        recyclerView.setAdapter(productAdapter);
        mProductApi = new ProductApiClient(this).get();

        //add listener to handle click events
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView,
                new RecyclerItemClickListener.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                //set selected product
                selectedProduct = productEntries.get(position);
                //show dialog
                Bundle arguments = new Bundle();
                arguments.putSerializable(ProductDialog.PRODUCT_KEY, selectedProduct);
                productDialog = new ProductDialog();
                productDialog.setArguments(arguments);
                productDialog.show(getFragmentManager(), "product_dialog");
            }

            @Override
            public void onItemLongClick(View view, int position) {
                //do nothing
                return;
            }

        }));


        //handle intent
        handleIntent(getIntent());
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

    @Override
    protected void onNewIntent(Intent intent) {
        //handle intent
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        //verify the action and get the query
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            search(query);
        }
    }

    private void search(String query) {
        //show products containing the query
        productEntries = new ArrayList<Product>();
        productAdapter.clear();
        //TODO maybe add a limit here?
        mProductApi.findByName(query, 0, 0, mSearchCallback);
    }

    @Override
    public void onShowLocationClick() {
        //show location
        Intent intent = new Intent(this, ProductMapActivity.class);
        startActivity(intent);
    }

    @Override
    public void onAddToCartClick() {
        //dismiss product dialog
        productDialog.dismiss();
        //show add to cart dialog
        AddToCartDialog.newInstance(selectedProduct.getName(), selectedProduct.getUnit(),
                selectedProduct.getPrice()).show(getSupportFragmentManager(), "add_to_cart_dialog");
    }

    @Override
    public void onReportClick() {
        //report missing product
    }


    @Override
    public void onDialogPositiveClick() {
        //add selected product to cart
        Cart.MYCART.addProduct(selectedProduct);
    }

    @Override
    public void onDialogNegativeClick() {
        //do nothing
        return;
    }
}
