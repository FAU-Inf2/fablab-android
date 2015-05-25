package de.fau.cs.mad.fablab.android;

import android.util.Log;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

public enum FabButton {
    MYFABUTTON;

    private boolean isActive = false;

    FabButton(){

    }

    public void init(View view){
        FloatingActionButton scanButton = (FloatingActionButton) view.findViewById(R.id.scan_FAB);
        FloatingActionButton searchButton = (FloatingActionButton) view.findViewById(R.id.search_FAB);
        FloatingActionMenu shoppingCartButton = (FloatingActionMenu) view.findViewById(R.id.shopping_cart_FAM);

        shoppingCartButton.setIconAnimated(false);

        isActive = true;

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public boolean getActice(){
        return isActive;
    }
}
