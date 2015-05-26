package de.fau.cs.mad.fablab.android.productsearch;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.fau.cs.mad.fablab.android.BaseActivity;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.cart.AddToCartDialog;
import de.fau.cs.mad.fablab.android.cart.RecyclerItemClickListener;
import de.fau.cs.mad.fablab.android.cart.RecyclerViewAdapter;
import de.fau.cs.mad.fablab.android.productMap.ProductMapActivity;
import de.fau.cs.mad.fablab.android.ui.UiUtils;
import de.fau.cs.mad.fablab.rest.ProductApiClient;
import de.fau.cs.mad.fablab.rest.core.CartEntry;
import de.fau.cs.mad.fablab.rest.core.Product;
import de.fau.cs.mad.fablab.rest.myapi.ProductApi;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ProductSearchActivity extends BaseActivity
        implements ProductDialog.ProductDialogListener,AdapterView.OnItemClickListener,AdapterView.OnItemSelectedListener {

    private RecyclerView.LayoutManager layoutManager;
    private RecyclerViewAdapter productAdapter;
    //only for testing
    private ArrayList<Product> productEntries;
    //our rest-callback interface
    private ProductApi mProductApi;

    private ProductDialog productDialog;
    private Product selectedProduct;

    private UiUtils uiUtils;
    private View spinnerContainerView;
    private ImageView spinnerImageView;

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
            uiUtils.hideSpinner(spinnerContainerView, spinnerImageView);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }

        @Override
        public void failure(RetrofitError error) {
            Toast.makeText(getBaseContext(), R.string.retrofit_callback_failure, Toast.LENGTH_LONG).show();
            uiUtils.hideSpinner(spinnerContainerView, spinnerImageView);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    };


    @Override
    protected void baseSetContentView() {
        setContentView(R.layout.activity_product_search);
    }

    @Override
    protected void baseOnCreate(Bundle savedInstanceState) {
        AutoCompleteHelper.getInstance().loadProductNames(this);

        mProductApi = new ProductApiClient(this).get();

        uiUtils = new UiUtils(this);
        spinnerContainerView = (View) findViewById(R.id.spinner);
        spinnerImageView = (ImageView) findViewById(R.id.spinner_image);

        initCartPanel(true);
        initFabButton();

        //get search view and set searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        //For Autocomplete
        final AutoCompleteTextView searchView = (AutoCompleteTextView) findViewById(R.id.product_search_view);
        searchView.setThreshold(2); //min 2 chars before autocomplete

        //Set adapter to AutoCompleteTextView
        ArrayAdapter<String>adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, AutoCompleteHelper.getInstance().getPossibleAutoCompleteWords());
        searchView.setAdapter(adapter);
        searchView.setOnItemSelectedListener(this);
        searchView.setOnItemClickListener(this);
        searchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    search(searchView.getText().toString());
                    handled = true;
                }
                return handled;
            }
        });



        //searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        //do not iconify search view
        //searchView.setIconified(false);

        //get recycler view and set layout manager and adapter
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.product_recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        productAdapter = new RecyclerViewAdapter(getApplicationContext(),new ArrayList<CartEntry>());
        recyclerView.setAdapter(productAdapter);


        //add listener to handle click events
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView,
                new RecyclerItemClickListener.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position, MotionEvent e) {
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

            @Override
            public boolean inViewInBounds(View view, int x, int y){
                // used in cart, cf Cart.java for further information
                return false;
            }

        }));


        //handle intent
        handleIntent(getIntent());
    }

    //For Autocomplete
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        search(String.valueOf(parent.getItemAtPosition(position)));
    }

    //For Autocomplete
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        search(String.valueOf(parent.getItemAtPosition(position)));
    }

    //For Autocomplete
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        InputMethodManager imm = (InputMethodManager) getSystemService( INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
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
        productEntries = new ArrayList<>();
        productAdapter.clear();
        //TODO maybe add a limit here?

        //show spinner and disable input
        uiUtils.showSpinner(spinnerContainerView, spinnerImageView);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

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
        AddToCartDialog.newInstance(selectedProduct).show(getSupportFragmentManager(),
                "add_to_cart_dialog");
    }

    @Override
    public void onReportClick() {
        //report missing product
    }
}
