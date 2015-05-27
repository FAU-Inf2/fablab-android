package de.fau.cs.mad.fablab.android;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import de.fau.cs.mad.fablab.android.productsearch.ProductSearchActivity;

public enum FabButton {
    MYFABUTTON;

    private boolean isActive = false;

    FabButton() {

    }

    public void init(View view, final Context context) {
        FloatingActionButton scanButton = (FloatingActionButton) view.findViewById(R.id.scan_FAB);
        FloatingActionButton searchButton = (FloatingActionButton) view.findViewById(R.id.search_FAB);
        FloatingActionMenu shoppingCartButton = (FloatingActionMenu) view.findViewById(R.id.shopping_cart_FAM);

        shoppingCartButton.setIconAnimated(false);

        isActive = true;

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductSearchActivity.class);
                context.startActivity(intent);
            }
        });

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BarcodeScannerActivity.class);
                context.startActivity(intent);
            }
        });
    }

    public boolean getActice(){
        return isActive;
    }
}
