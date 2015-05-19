package de.fau.cs.mad.fablab.android.productMap;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;;
import android.graphics.Rect;
import android.view.View;


/**
 * Created by Michael on 11.05.2015.
 */
public class DrawingActivity extends View
{

    private FablabView view;
    private int xCoord;
    private int yCoord;

    public DrawingActivity(Context context, int xCoord, int yCoord)
    {
        super(context);
        view = FablabView.MAIN_ROOM;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
    }

    public void setCoords(int xCoord, int yCoord)
    {
        this.xCoord = xCoord;
        this.yCoord = yCoord;
    }

    public void setView(FablabView view)
    {
        this.view = view;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        draw(canvas, xCoord, yCoord);

    }

    protected void draw(Canvas canvas, int x, int y)
    {

        switch (view)
        {
            case MAIN_ROOM:
                drawFablabLayout(canvas);
                drawLocation(canvas, x, y);
                break;
            case ELECTRIC_WORKSHOP:
                drawElectricWorkshop(canvas);
                drawLocation(canvas, x, y);
                break;
        }



    }

    protected void drawFablabLayout(Canvas canvas)
    {
        int padding = 10;

        // paintings
        // room painting
        Paint roomPaint = new Paint();
        roomPaint.setColor(Color.BLACK);
        roomPaint.setStyle(Paint.Style.STROKE);
        roomPaint.setStrokeWidth(10);

        // door painting
        Paint doorPaint = new Paint();
        doorPaint.setColor(Color.GREEN);
        doorPaint.setStyle(Paint.Style.STROKE);
        doorPaint.setStrokeWidth(10);

        // shelf painting
        Paint shelfPaint = new Paint();
        shelfPaint.setColor(Color.GRAY);
        shelfPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        shelfPaint.setStrokeWidth(10);

        //contours

        // fablab contour
        Rect outerWall = new Rect();
        outerWall.set(padding, padding, canvas.getWidth() - padding, canvas.getHeight() - padding);
        canvas.drawRect(outerWall, roomPaint);

        // wall between rooms
        canvas.drawLine(outerWall.left, outerWall.centerY(), outerWall.right, outerWall.centerY(), roomPaint);

        // entry door
        canvas.drawLine(outerWall.left, outerWall.centerY() + 100, outerWall.left, outerWall.centerY() + 250, doorPaint);

        // room door
        canvas.drawLine(outerWall.right - 100, outerWall.centerY(), outerWall.right - 250, outerWall.centerY(), doorPaint);

        //shelf top top
        Rect shelfTop = new Rect(outerWall.left + padding, outerWall.top + 10, outerWall.right - 10, outerWall.top + 100);
        canvas.drawRect(shelfTop, shelfPaint);

        //shelf top middle
        int centreOfRoom = (outerWall.centerY() - outerWall.top)/2;
        Rect shelfTopMiddle = new Rect(outerWall.left + padding, centreOfRoom - 50, outerWall.right - 200, centreOfRoom + 50);
        canvas.drawRect(shelfTopMiddle, shelfPaint);

        // shelf top bottom
        Rect shelfTopBottom = new Rect(outerWall.left + padding, outerWall.centerY() - 100, outerWall.right - 250, outerWall.centerY()-padding);
        canvas.drawRect(shelfTopBottom, shelfPaint);

    }

    private void drawElectricWorkshop(Canvas canvas)
    {
        int padding = 10;
        // paintings
        // room painting
        Paint roomPaint = new Paint();
        roomPaint.setColor(Color.BLACK);
        roomPaint.setStyle(Paint.Style.STROKE);
        roomPaint.setStrokeWidth(10);

        Rect outerWall = new Rect();
        outerWall.set(padding, padding, canvas.getWidth() - padding, canvas.getHeight() - padding);
        canvas.drawRect(outerWall, roomPaint);
    }

    private void drawLocation(Canvas canvas, int x, int y)
    {
        Paint locationBrush = new Paint();
        locationBrush.setColor(Color.RED);
        locationBrush.setStyle(Paint.Style.FILL);
        locationBrush.setStrokeWidth(20);
        locationBrush.setAlpha(200);

        Paint textBrush = new Paint();
        textBrush.setColor(Color.BLACK);
        textBrush.setTextSize(30);


        //canvas.drawRect(outerWall, room);
        //canvas.drawRect(cupboard, room);
        canvas.drawCircle(canvas.getWidth() - x, canvas.getHeight()/2 - y, 20, locationBrush);
        canvas.drawText("Regal 41", canvas.getWidth() - x, canvas.getHeight()/2 - y, textBrush );
    }



}
