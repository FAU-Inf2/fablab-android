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

    protected int padding = 10;



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

        //contours

        // fablab contour
        Rect outerWall = new Rect();
        outerWall.set(padding, padding, canvas.getWidth() - padding, canvas.getHeight() - padding);
        canvas.drawRect(outerWall, Paintings.ROOM_PAINTING.getPaint());

        int shelfStrokeWith = Paintings.ROOM_PAINTING.getStrokeWith();
        int positionX  = ((outerWall.right-shelfStrokeWith/2) - (outerWall.left+shelfStrokeWith/2))/4;
        int positionY = ((outerWall.centerY()-shelfStrokeWith/2) - (outerWall.top+shelfStrokeWith/2))/8;

        int leftShelfPosition = outerWall.left + shelfStrokeWith/2;
        int rightShelfPosition = outerWall.right - shelfStrokeWith/2;
        int horizontalMiddleShelfPosition = leftShelfPosition + positionX*3;



        // wall between rooms
        canvas.drawLine(outerWall.left, outerWall.centerY(), outerWall.right, outerWall.centerY(), Paintings.ROOM_PAINTING.getPaint());

        // entry door
        canvas.drawLine(outerWall.left, outerWall.centerY() + 100, outerWall.left, outerWall.centerY() + 250, Paintings.DOOR_PAINTING.getPaint());

        // room door
        canvas.drawLine((outerWall.left + shelfStrokeWith) + positionX*3, outerWall.centerY(), outerWall.right - 50, outerWall.centerY(), Paintings.DOOR_PAINTING.getPaint());

        //shelf top top
        Rect shelfTop = new Rect(leftShelfPosition, outerWall.top + shelfStrokeWith/2, rightShelfPosition, outerWall.top + shelfStrokeWith/2 + positionY);
        canvas.drawRect(shelfTop, Paintings.SHELF_FILL_DARK_PAINTING.getPaint());

        //shelf top middle
        Rect shelfTopMiddle = new Rect(leftShelfPosition, outerWall.top + shelfStrokeWith/2 + positionY*3, horizontalMiddleShelfPosition, outerWall.top + shelfStrokeWith/2 + positionY*4);
        canvas.drawRect(shelfTopMiddle, Paintings.SHELF_FILL_LIGHT_PAINTING.getPaint());

        Rect shelfBottomMiddle = new Rect(leftShelfPosition, outerWall.top + shelfStrokeWith/2 + positionY*4, horizontalMiddleShelfPosition, outerWall.top + shelfStrokeWith/2 + positionY*5);
        canvas.drawRect(shelfBottomMiddle, Paintings.SHELF_FILL_DARK_PAINTING.getPaint());

        // shelf top bottom
        Rect shelfTopBottom = new Rect(leftShelfPosition, outerWall.top + shelfStrokeWith/2 + positionY*7, horizontalMiddleShelfPosition, outerWall.centerY() - shelfStrokeWith/2);
        canvas.drawRect(shelfTopBottom, Paintings.SHELF_FILL_LIGHT_PAINTING.getPaint());

        // shelf bottom top
        Rect shelfBottomTop = new Rect(leftShelfPosition, outerWall.centerY() + shelfStrokeWith/2, horizontalMiddleShelfPosition, outerWall.centerY() + shelfStrokeWith/2 + positionY);
        canvas.drawRect(shelfBottomTop, Paintings.SHELF_FILL_DARK_PAINTING.getPaint());

    }

    protected void drawElectricWorkshop(Canvas canvas)
    {

        Paint electricWorkshopPaint = Paintings.SHELF_STROKE_DARK_PAINTING.getPaint();
        Rect electricWorkshopOutline = drawLocalViewBaseLayout(canvas, electricWorkshopPaint, "Elektrowerkstatt");

        //shelves
        if(electricWorkshopOutline.width() >= electricWorkshopOutline.height())
        {
            canvas.drawLine(padding, electricWorkshopOutline.centerY(), canvas.getWidth() - padding, electricWorkshopOutline.centerY(), electricWorkshopPaint);
            canvas.drawLine(electricWorkshopOutline.centerX(), padding, electricWorkshopOutline.centerX(), canvas.getHeight() - padding, electricWorkshopPaint);
            canvas.drawLine(electricWorkshopOutline.centerX()/2, electricWorkshopOutline.top, electricWorkshopOutline.centerX()/2, electricWorkshopOutline.bottom, electricWorkshopPaint);
            canvas.drawLine(electricWorkshopOutline.right - electricWorkshopOutline.centerX()/2,electricWorkshopOutline.top, electricWorkshopOutline.right - electricWorkshopOutline.centerX()/2, electricWorkshopOutline.bottom, electricWorkshopPaint );
        }
        else
        {
            canvas.drawLine(electricWorkshopOutline.centerX(), padding, electricWorkshopOutline.centerX(), canvas.getHeight() - padding, electricWorkshopPaint);
            canvas.drawLine(electricWorkshopOutline.left, electricWorkshopOutline.centerY(), electricWorkshopOutline.right, electricWorkshopOutline.centerY(), electricWorkshopPaint);
            canvas.drawLine(electricWorkshopOutline.left, electricWorkshopOutline.centerY() / 2, electricWorkshopOutline.right, electricWorkshopOutline.centerY() / 2, electricWorkshopPaint);
            canvas.drawLine(electricWorkshopOutline.left, electricWorkshopOutline.bottom - electricWorkshopOutline.centerY() / 2, electricWorkshopOutline.right, electricWorkshopOutline.bottom - electricWorkshopOutline.centerY() / 2, electricWorkshopPaint);
        }


    }

    protected Rect drawLocalViewBaseLayout(Canvas canvas, Paint shelfPaint, String viewName)
    {
        Rect localViewBaseShelf = new Rect();

        if(canvas.getWidth() >= canvas.getHeight())
        {
            int height = canvas.getHeight()/6;
            localViewBaseShelf.set(padding, height, canvas.getWidth() - padding, canvas.getHeight() - height);
        }
        else
        {
            int width = canvas.getWidth()/6;
            localViewBaseShelf.set(width, padding, canvas.getWidth() - width, canvas.getHeight() - padding);
        }
        canvas.drawRect(localViewBaseShelf, shelfPaint);


        if(canvas.getWidth() >= canvas.getHeight())
            canvas.drawText(viewName, padding, padding, Paintings.TEXT_PAINTING_LARGE.getPaint());
        else
        {
            int textOffsetX = Paintings.TEXT_PAINTING_LARGE.getStrokeWith() + padding;
            canvas.save();
            canvas.rotate(90, canvas.getWidth() - Paintings.TEXT_PAINTING_LARGE.getStrokeWith() - padding, padding);
            canvas.drawText(viewName, canvas.getWidth() - textOffsetX, padding, Paintings.TEXT_PAINTING_LARGE.getPaint());
            canvas.restore();

        }


        return localViewBaseShelf;

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
