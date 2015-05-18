package de.fau.cs.mad.fablab.android.ProductMap;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Michael on 07.05.2015.
 */
public class ProductMapActivity extends ActionBarActivity
{
    private ImageView imageView_productFirst;
    private Drawable drawable;
    private Drawable[] drawables = null;

    public DrawingActivity drawView;
    public DrawingFineActivity drawFineView;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        drawView = new DrawingActivity(this);
        drawView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(ProductMapActivity.this, ProductMapFineActivity.class);
                startActivity(intent);
            }
        });

        setContentView(drawView);
    }


}

