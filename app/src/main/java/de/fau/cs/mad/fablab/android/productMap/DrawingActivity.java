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

    private FablabView fablabView;
    private int positionX;
    private int positionY;
    private Canvas canvas;
    private String locationName;



    public DrawingActivity(Context context, FablabView fablabView, int positionX, int positionY, String locationName)
    {
        super(context);
        this.positionX = positionX;
        this.positionY = positionY;
        this.fablabView = fablabView;
        this.locationName = locationName;
    }

    public void setDrawingParameter(FablabView fablabView, int positionX, int positionY)
    {
        this.positionX = positionX;
        this.positionY = positionY;
        this.fablabView = fablabView;
    }


    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        this.canvas = canvas;
        draw();
    }

    protected void draw()
    {
        switch (fablabView)
        {
            case MAIN_ROOM:
                drawFablabLayout(canvas);
                drawLocation(canvas, positionX, positionY);
                break;
            case ELECTRIC_WORKSHOP:
                drawElectricWorkshop(canvas);
                drawLocation(canvas, positionX, positionY);
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

        // text painting
        Paint textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(30);

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
        canvas.drawText(FablabView.ELECTRIC_WORKSHOP.getProductName(), shelfTop.centerX(), shelfTop.centerY(), textPaint);

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

        Rect electricWorkshopOutline = new Rect();

        if(canvas.getWidth() >= canvas.getHeight())
        {
            int height = canvas.getHeight()/2;
            electricWorkshopOutline.set(padding, height/2, canvas.getWidth() - padding, canvas.getHeight() - height/2);
        }
        else
        {
            int width = canvas.getWidth()/2;
            electricWorkshopOutline.set(width/2, padding, canvas.getWidth() - width/2, canvas.getHeight() - padding);
        }
        canvas.drawRect(electricWorkshopOutline, roomPaint);

        //shelves
        if(electricWorkshopOutline.width() >= electricWorkshopOutline.height())
        {
            canvas.drawLine(padding, electricWorkshopOutline.centerY(), canvas.getWidth() - padding, electricWorkshopOutline.centerY(), roomPaint);
            canvas.drawLine(electricWorkshopOutline.centerX(), padding, electricWorkshopOutline.centerX(), canvas.getHeight() - padding, roomPaint);
            canvas.drawLine(electricWorkshopOutline.centerX()/2, electricWorkshopOutline.top, electricWorkshopOutline.centerX()/2, electricWorkshopOutline.bottom, roomPaint);
            canvas.drawLine(electricWorkshopOutline.right - electricWorkshopOutline.centerX()/2,electricWorkshopOutline.top, electricWorkshopOutline.right - electricWorkshopOutline.centerX()/2, electricWorkshopOutline.bottom, roomPaint );
        }
        else
        {
            canvas.drawLine(electricWorkshopOutline.centerX(), padding, electricWorkshopOutline.centerX(), canvas.getHeight() - padding, roomPaint);
            canvas.drawLine(electricWorkshopOutline.left, electricWorkshopOutline.centerY(), electricWorkshopOutline.right, electricWorkshopOutline.centerY(), roomPaint);
            canvas.drawLine(electricWorkshopOutline.left, electricWorkshopOutline.centerY()/2, electricWorkshopOutline.right, electricWorkshopOutline.centerY()/2, roomPaint);
            canvas.drawLine(electricWorkshopOutline.left, electricWorkshopOutline.bottom - electricWorkshopOutline.centerY()/2, electricWorkshopOutline.right, electricWorkshopOutline.bottom - electricWorkshopOutline.centerY()/2, roomPaint );
        }

        // Description
        Paint textBrush = new Paint();
        textBrush.setColor(Color.BLACK);
        textBrush.setTextSize(50);

        String description = "Electric Workshop";

        if(canvas.getWidth() >= canvas.getHeight())
            canvas.drawText(description, electricWorkshopOutline.centerX(), padding, textBrush);
        else
        {
            canvas.save();
            canvas.rotate(-90, padding + 50, electricWorkshopOutline.bottom - padding);
            canvas.drawText(description, padding+50, electricWorkshopOutline.bottom - padding, textBrush);
            canvas.restore();
        }
    }

    private void drawLocation(Canvas canvas, int positionX, int positionY)
    {
        Paint locationBrush = new Paint();
        locationBrush.setColor(Color.RED);
        locationBrush.setStyle(Paint.Style.FILL);
        locationBrush.setStrokeWidth(20);
        locationBrush.setAlpha(200);

        Paint textBrush = new Paint();
        textBrush.setColor(Color.BLACK);
        textBrush.setTextSize(30);

        canvas.drawCircle(positionX, positionY, 20, locationBrush);
        canvas.drawText(locationName, positionX, positionY, textBrush );
    }



}
