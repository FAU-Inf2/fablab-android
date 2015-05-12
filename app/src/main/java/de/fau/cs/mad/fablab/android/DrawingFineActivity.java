package de.fau.cs.mad.fablab.android;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

/**
 * Created by Michael on 11.05.2015.
 */
public class DrawingFineActivity extends View {

    public DrawingFineActivity(Context context) {
        super(context);



    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int locationX = 75;
        int locationY = 75;

        // fablab map
        Rect outerWall = new Rect();
        outerWall.set(0, 0, canvas.getWidth(), canvas.getHeight()/2);

        Rect cupboard = new Rect();
        cupboard.set(canvas.getWidth() - 100, canvas.getHeight() / 50, canvas.getWidth() - 20, canvas.getHeight()/2 - 20 );


        Paint room = new Paint();
        room.setColor(Color.BLACK);
        room.setStyle(Paint.Style.STROKE);
        room.setStrokeWidth(10);

        Paint locationBrush = new Paint();
        locationBrush.setColor(Color.RED);
        locationBrush.setStyle(Paint.Style.FILL);
        locationBrush.setStrokeWidth(20);
        locationBrush.setAlpha(200);


        //Backgroundimage --> unwanted scale
        //setBackgroundResource(R.drawable.fablab);



        canvas.drawRect(outerWall, room);

    }
}
