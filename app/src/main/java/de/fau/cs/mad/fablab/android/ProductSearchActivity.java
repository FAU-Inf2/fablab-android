package de.fau.cs.mad.fablab.android;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.SearchView;
import java.util.ArrayList;
import java.util.List;
import de.fau.cs.mad.fablab.android.basket.RecyclerViewAdapter;
import de.fau.cs.mad.fablab.rest.core.Product;

public class ProductSearchActivity extends ActionBarActivity {

    private RecyclerView.LayoutManager layoutManager;
    private RecyclerViewAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_search);

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
        productAdapter = new RecyclerViewAdapter(new ArrayList<Product>());
        recyclerView.setAdapter(productAdapter);

        //handle intent
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        //handle intent
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        //verify the action and get the query
        if(Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            search(query);
        }
    }

    private void search(String query) {
        //show products containing the query
        productAdapter.clear();
        List<Product> products = getDummyProducts();
        for(Product product : products) {
            if(product.getName().contains(query)) {
                productAdapter.addProduct(product);
            }
        }
    }

    private List<Product> getDummyProducts() {
        //create dummy products
        List<Product> products= new ArrayList<Product>();
        Product p1 = new Product(1,"Produkt1",1,1,"Category1", "Unit1");
        products.add(p1);
        Product p2 = new Product(2,"Produkt2",2,2,"Category2", "Unit2");
        products.add(p2);
        Product p3 = new Product(3,"Produkt3",3,3,"Category3", "Unit3");
        products.add(p3);
        Product p4 = new Product(4,"Produkt4",4,4,"Category4", "Unit4");
        products.add(p4);
        Product p5 = new Product(5,"Produkt5",5,5,"Category5", "Unit5");
        products.add(p5);
        return products;
    }

}
