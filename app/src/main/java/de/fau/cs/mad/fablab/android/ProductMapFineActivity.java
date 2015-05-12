package de.fau.cs.mad.fablab.android;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Michael on 11.05.2015.
 */
public class ProductMapFineActivity extends ActionBarActivity {
    private ImageView imageView_productFirst;
    private Drawable drawable;
    private Drawable[] drawables = null;

    public DrawingFineActivity drawFineView;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        drawFineView = new DrawingFineActivity(this);
        drawFineView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        setContentView(drawFineView);
    }



}
