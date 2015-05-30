package de.fau.cs.mad.fablab.android.productMap;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import de.fau.cs.mad.fablab.android.productsearch.ProductSearchActivity;

public class ProductMapActivity extends ActionBarActivity
{

    private boolean isMainViewActive ;

    private DrawingActivity drawView;
    private ProductLocation productLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.activity_product_map);

        //get location
        String locationString = getIntent().getStringExtra(ProductSearchActivity.KEY_LOCATION);
        isMainViewActive = true;


        productLocation = LocationParser.getLocation(locationString);
        createView(this);

    }


    protected void createView(Context context)
    {

        //assert productLocation != null;

        try
        {

            drawView = new DrawingActivity(context, FablabView.MAIN_ROOM, productLocation.getMainPositionX(), productLocation.getMainPositionY(), productLocation.getLocationName());
            drawView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (isMainViewActive == true)
                    {
                        drawView.setDrawingParameter(productLocation.getView(), productLocation.getViewPositionX(), productLocation.getViewPositionY());
                        drawView.invalidate();
                        isMainViewActive = false;
                    } else
                    {
                        drawView.setDrawingParameter(FablabView.MAIN_ROOM, productLocation.getMainPositionX(), productLocation.getMainPositionY());
                        drawView.invalidate();
                        isMainViewActive = true;
                    }
                }
            });
            throw new IllegalArgumentException("ob das wohl geht");
        }
        catch(IllegalArgumentException exception)
        {
            new AlertDialog.Builder(this)
                    .setTitle("test")
                    .setMessage("something went wrong");
        }

        setContentView(drawView);

    }

}

