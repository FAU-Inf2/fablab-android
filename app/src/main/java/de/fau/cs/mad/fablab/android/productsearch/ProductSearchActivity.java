package de.fau.cs.mad.fablab.android.productsearch;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.fau.cs.mad.fablab.android.BaseActivity;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.productMap.LocationParser;
import de.fau.cs.mad.fablab.android.productMap.ProductMapActivity;
import de.fau.cs.mad.fablab.android.ui.UiUtils;
import de.fau.cs.mad.fablab.rest.core.Product;
import de.fau.cs.mad.fablab.rest.myapi.ProductApi;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ProductSearchActivity extends BaseActivity
        implements ProductDialog.ProductDialogListener,AdapterView.OnItemClickListener,AdapterView.OnItemSelectedListener {

    public static final String  KEY_LOCATION            = "location";
    private final String        KEY_SEARCHED_PRODUCTS   = "searched_products";
    private final String        KEY_SELECTED_PRODUCT    = "selected_product";
    private final String        KEY_PRODUCT_DIALOG      = "product_dialog";
    private final String        KEY_ERROR_DIALOG        = "error_dialog";

    private RecyclerView.LayoutManager layoutManager;
    private ProductAdapter productAdapter;
    //our rest-callback interface
    private ProductApi mProductApi;

    private ProductDialog productDialog;
    private Product selectedProduct;

    private ErrorDialog errorDialog;

    private View spinnerContainerView;
    private ImageView spinnerImageView;

    //This callback is used for product Search.
    private Callback<List<Product>> mSearchCallback = new Callback<List<Product>>() {
        @Override
        public void success(List<Product> products, Response response) {
            if (products.isEmpty()) {
                Toast.makeText(getBaseContext(), R.string.barcode_scanner_product_not_found, Toast.LENGTH_LONG).show();
            }

            ArrayList<Product> results = new ArrayList<Product>();
            for (Product product : products) {
                results.add(product);
            }
            Collections.sort(results, new ProductSort());
            productAdapter.addAll(results);
            productAdapter.notifyDataSetChanged();

            UiUtils.hideSpinner(spinnerContainerView, spinnerImageView);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }

        @Override
        public void failure(RetrofitError error) {
            Toast.makeText(getBaseContext(), R.string.retrofit_callback_failure, Toast.LENGTH_LONG).show();
            UiUtils.hideSpinner(spinnerContainerView, spinnerImageView);
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

        //mProductApi = new ProductApiClient(this).get();

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
                    searchView.dismissDropDown();
                    search(searchView.getText().toString());
                    handled = true;
                }
                return handled;
            }
        });



        //searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        //do not iconify search view
        //searchView.setIconified(false);

        //get indexable list view and set adapter
        IndexableListView indexableListView = (IndexableListView)
                findViewById(R.id.product_indexable_list_view);
        productAdapter = new ProductAdapter(getApplicationContext(), R.layout.product_entry);
        indexableListView.setAdapter(productAdapter);
        indexableListView.setFastScrollEnabled(true);

        //add listener to handle click events
        indexableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //set selected product
                selectedProduct = productAdapter.getItem(position);
                //show product dialog
                productDialog = ProductDialog.newInstance(selectedProduct);
                productDialog.show(getFragmentManager(), "product_dialog");
            }

        });

        if (savedInstanceState != null) {
            //recreate saved instance state
            productAdapter.addAll((ArrayList<Product>) savedInstanceState
                    .getSerializable(KEY_SEARCHED_PRODUCTS));
            selectedProduct = (Product) savedInstanceState.getSerializable(KEY_SELECTED_PRODUCT);
            if(selectedProduct != null && savedInstanceState.getBoolean(KEY_PRODUCT_DIALOG)) {
                productDialog = ProductDialog.newInstance(selectedProduct);
                productDialog.show(getFragmentManager(),"product_dialog");
            }
            if(savedInstanceState.getBoolean(KEY_ERROR_DIALOG)) {
                errorDialog = ErrorDialog.newInstance(getResources().getString(
                        R.string.invalid_location));
                errorDialog.show(getFragmentManager(),"error_dialog");
            }
        }

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
        UiUtils.hideKeyboard(this);
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
        productAdapter.clear();
        //TODO maybe add a limit here?

        //show spinner and disable input
        UiUtils.showSpinner(spinnerContainerView, spinnerImageView);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        UiUtils.hideKeyboard(this);

        mProductApi.findByName(query, 0, 0, mSearchCallback);
    }

    @Override
    public void onShowLocationClick() {
        //TODO get location of the selected product
        String location = "tatsaechliche Lagerorte / FAU FabLab / Elektrowerkstatt / " +
                "Regal / Plexiglas";

        //check if location is valid
        if(LocationParser.getLocation(location) != null) {
            //show location
            Intent intent = new Intent(this, ProductMapActivity.class);
            intent.putExtra(KEY_LOCATION, location);
            startActivity(intent);
        } else {
            //show error dialog
            productDialog.dismiss();
            errorDialog = ErrorDialog.newInstance(getResources().getString(R.string.invalid_location));
            errorDialog.show(getFragmentManager(), "error_dialog");
        }
    }

    @Override
    public void onAddToCartClick() {
        //dismiss product dialog
        productDialog.dismiss();
        //show add to cart dialog
    }

    @Override
    public void onReportClick() {
        //report missing product
    }

    @Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);

        //save searched products
        outState.putSerializable(KEY_SEARCHED_PRODUCTS, productAdapter.getAllItems());
        //save selected product
        outState.putSerializable(KEY_SELECTED_PRODUCT, selectedProduct);
        //save product dialog
        boolean productDialogIsShowing = false;
        if(productDialog != null) {
            Dialog dialog = productDialog.getDialog();
            if(dialog != null && dialog.isShowing()) {
                productDialogIsShowing = true;
            }
        }
        outState.putBoolean(KEY_PRODUCT_DIALOG, productDialogIsShowing);
        //save error dialog
        boolean errorDialogIsShowing = false;
        if(errorDialog != null) {
            Dialog dialog = errorDialog.getDialog();
            if(dialog != null && dialog.isShowing()) {
                errorDialogIsShowing = true;
            }
        }
        outState.putBoolean(KEY_ERROR_DIALOG, errorDialogIsShowing);

    }
}
