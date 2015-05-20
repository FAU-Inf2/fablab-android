package de.fau.cs.mad.fablab.android.productMap;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.navdrawer.AppbarDrawerInclude;

/**
 * Created by Michael on 07.05.2015.
 */
public class ProductMapActivity extends ActionBarActivity
{

    private int xWerkstatt = 75;
    private int yWerkstatt = 75;
    private int xSchrank = 10;
    private int ySchrank =10;
    private boolean show ;

    private DrawingActivity drawView;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.activity_product_map);

        //location interface
        //getIntent().getExtras().getString("location");
        show = true;
        createView(this, xWerkstatt, yWerkstatt);

    }


    protected void createView(Context context, int xCoord, int yCoord)
    {
        drawView = new DrawingActivity(context, xCoord, yCoord);
        drawView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(show == true)
                {
                    drawView.setCoords(xSchrank, ySchrank);
                    drawView.setView(FablabView.ELECTRIC_WORKSHOP);
                    drawView.invalidate();
                    show = false;
                }
                else
                {
                    drawView.setCoords(xWerkstatt, yWerkstatt);
                    drawView.setView(FablabView.MAIN_ROOM);
                    drawView.invalidate();
                    show = true;
                }
            }
        });

        setContentView(drawView);

    }

}

