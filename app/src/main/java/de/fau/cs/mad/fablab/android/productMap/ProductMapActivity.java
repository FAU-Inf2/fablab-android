package de.fau.cs.mad.fablab.android.productMap;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

/**
 * Created by Michael on 07.05.2015.
 */
public class ProductMapActivity extends ActionBarActivity
{

    private boolean isMainViewActive ;

    private DrawingActivity drawView;
    private ProductLocation productLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.activity_product_map);

        //TODO: get location
        //getIntent().getExtras().getString("location");
        String locationString = "tatsächliche Lagerorte / FAU FabLab / Elektrowerkstatt / Regal / Plexiglas";
        isMainViewActive = true;


        productLocation = LocationParser.getLocation(locationString);
        createView(this);

    }


    protected void createView(Context context)
    {
        drawView = new DrawingActivity(context, FablabView.MAIN_ROOM, productLocation.getMainPositionX(), productLocation.getMainPositionY(), productLocation.getLocationName());
        drawView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(isMainViewActive == true)
                {
                    drawView.setDrawingParameter(productLocation.getView(), productLocation.getViewPositionX(), productLocation.getViewPositionY());
                    drawView.invalidate();
                    isMainViewActive = false;
                }
                else
                {
                    drawView.setDrawingParameter(FablabView.MAIN_ROOM, productLocation.getMainPositionX(), productLocation.getMainPositionY());
                    drawView.invalidate();
                    isMainViewActive = true;
                }
            }
        });

        setContentView(drawView);

    }

}

