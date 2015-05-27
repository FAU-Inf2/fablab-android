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
    private double positionX;
    private double positionY;
    private Canvas canvas;
    private String locationName;

    protected int padding = 10;



    public DrawingActivity(Context context, FablabView fablabView, double positionX, double positionY, String locationName)
    {
        super(context);
        this.positionX = positionX;
        this.positionY = positionY;
        this.fablabView = fablabView;
        this.locationName = locationName;
    }

    public void setDrawingParameter(FablabView fablabView, double positionX, double positionY)
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
                drawLocation(canvas, positionX, positionY, false);
                break;
            case ELECTRIC_WORKSHOP:
                drawAcrylicShelf(canvas);
                drawLocation(canvas, positionX, positionY, true);
                break;
            case ACRYLIC_GLAS_SHELF:
                drawAcrylicShelf(canvas);
                drawLocation(canvas, positionX, positionY, true);
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
        canvas.drawText("FabLab", outerWall.left + padding, outerWall.top + Paintings.TEXT_PAINTING_LARGE.getPaint().getTextSize() + padding, Paintings.TEXT_PAINTING_LARGE.getPaint());

        int wallOffset = Paintings.ROOM_PAINTING.getStrokeWith()/2;
        int roomHeight = (outerWall.centerY() - outerWall.top);
        int roomWidth = (outerWall.right - outerWall.left) - Paintings.ROOM_PAINTING.getStrokeWith();



        // new version
        int halfWallOffset = Paintings.ROOM_PAINTING.getStrokeWith()/2;

        int leftPoint = padding + halfWallOffset;
        int rightPoint = outerWall.right - halfWallOffset;

        int topPoint = outerWall.top + halfWallOffset;
        int topBottomPoint = outerWall.centerY() - halfWallOffset;

        int bottomTopPoint = outerWall.centerY() + halfWallOffset;
        int bottomPoint = outerWall.bottom - halfWallOffset;

        // wall between the two fablab rooms
        canvas.drawLine(outerWall.left, outerWall.centerY(), outerWall.right, outerWall.centerY(), Paintings.ROOM_PAINTING.getPaint());

        // doors
        // entry door
        int entryDoorBottomPoint = outerWall.centerY() + roomHeight/3*2;
        int entryDoorTopPoint = outerWall.centerY() + roomHeight/3;
        canvas.drawLine(outerWall.left, entryDoorTopPoint, outerWall.left, entryDoorBottomPoint, Paintings.DOOR_PAINTING.getPaint());

        // room door top
        int doorLeftPoint = outerWall.right - outerWall.width()/5 - padding*2;
        int doorRightPoint = outerWall.right - padding*2;
        canvas.drawLine(doorLeftPoint, outerWall.centerY(), doorRightPoint, outerWall.centerY(), Paintings.DOOR_PAINTING.getPaint());

        // room door bottom
        canvas.drawLine(doorLeftPoint, outerWall.bottom, doorRightPoint, outerWall.bottom, Paintings.DOOR_PAINTING.getPaint());

        // windows
        int windowWith = roomHeight/6;

        canvas.drawLine(outerWall.right, topPoint + windowWith, outerWall.right, topPoint + windowWith*2, Paintings.WINDOW_PAINTING.getPaint());
        canvas.drawLine(outerWall.right, topPoint + windowWith*4, outerWall.right, topPoint + windowWith*5, Paintings.WINDOW_PAINTING.getPaint());

        canvas.drawLine(outerWall.right, bottomTopPoint + windowWith*(5/2), outerWall.right, bottomTopPoint + windowWith*(7/2), Paintings.WINDOW_PAINTING.getPaint());
        canvas.drawLine(outerWall.right, bottomTopPoint + windowWith*4, outerWall.right, bottomTopPoint + windowWith*5, Paintings.WINDOW_PAINTING.getPaint());


        int offsetLeftWall = wallOffset + padding;

        // inner rooms

        // bottom room

        // milling machine
        int millingMachineRightPoint = offsetLeftWall + roomWidth/4;
        Rect millingMachine = new Rect(leftPoint, bottomTopPoint, millingMachineRightPoint, entryDoorTopPoint);
        canvas.drawRect(millingMachine, Paintings.GRAY_PAINTING.getPaint());
        canvas.drawText("Fr\u00e4se", leftPoint + padding, millingMachine.centerY(), Paintings.TEXT_PAINTING_SMALL.getPaint());

        // workbench
        int workbenchBottomPoint = bottomTopPoint + roomHeight/8;
        Rect workbench = new Rect(millingMachineRightPoint, bottomTopPoint, doorLeftPoint, workbenchBottomPoint );
        canvas.drawRect(workbench, Paintings.SHELF_FILL_LIGHT_PAINTING.getPaint());
        canvas.drawText("Werkbank", workbench.left + padding, workbench.centerY(), Paintings.TEXT_PAINTING_SMALL.getPaint());

        Rect workbench2 = new Rect(doorLeftPoint - roomWidth/5, workbenchBottomPoint, doorLeftPoint, bottomTopPoint + roomHeight/8*2);
        canvas.drawRect(workbench2, Paintings.SHELF_FILL_LIGHT_PAINTING.getPaint());

        // chemistry bench
        int chemistryBenchRightPoint = outerWall.left + Paintings.ROOM_PAINTING.getStrokeWith()/2 + roomWidth/5;
        Rect chemistryBench = new Rect(leftPoint, entryDoorBottomPoint, chemistryBenchRightPoint, bottomPoint);
        canvas.drawRect(chemistryBench, Paintings.SHELF_FILL_LIGHT_PAINTING.getPaint());

        int chemistryBench2RightPoint = leftPoint + roomWidth/5*2;
        int chemistryBench2TopPoint = bottomPoint - roomHeight/8;
        Rect chemistryBench2 = new Rect(chemistryBenchRightPoint, chemistryBench2TopPoint, chemistryBench2RightPoint, bottomPoint);
        canvas.drawRect(chemistryBench2, Paintings.SHELF_FILL_LIGHT_PAINTING.getPaint());
        canvas.drawText("Chemietisch", chemistryBench.left + padding, chemistryBench.bottom - padding, Paintings.TEXT_PAINTING_SMALL.getPaint());


        // engine lathe
        Rect engineLathe = new Rect(chemistryBench2RightPoint, chemistryBench2TopPoint, doorLeftPoint, bottomPoint );
        canvas.drawRect(engineLathe, Paintings.GRAY_PAINTING.getPaint());
        canvas.drawText("Drehbank", engineLathe.left + padding, engineLathe.bottom - padding, Paintings.TEXT_PAINTING_SMALL.getPaint());

        //tables
        Rect tablesBottomRoom = new Rect(leftPoint + roomWidth/2, bottomTopPoint + roomHeight/7*3, rightPoint, bottomTopPoint + roomHeight/7*5);
        canvas.drawRect(tablesBottomRoom, Paintings.SHELF_FILL_DARK_PAINTING.getPaint());
        canvas.drawText("Labortisch", tablesBottomRoom.left + padding, tablesBottomRoom.centerY(), Paintings.TEXT_PAINTING_SMALL.getPaint());

        // top room
        // acrylic shelf
        Rect acrylicShelf = new Rect(leftPoint,topBottomPoint - roomHeight/6, doorLeftPoint, topBottomPoint );
        canvas.drawRect(acrylicShelf, Paintings.SHELF_FILL_LIGHT_PAINTING.getPaint());
        canvas.drawText("Plexiglasregal", acrylicShelf.left + padding, acrylicShelf.bottom - padding, Paintings.TEXT_PAINTING_SMALL.getPaint());

        // screw shelf
        Rect screwShelf = new Rect(rightPoint - roomWidth/8, topPoint + windowWith*2, rightPoint, topPoint + windowWith*4);
        canvas.drawRect(screwShelf, Paintings.SHELF_FILL_LIGHT_PAINTING.getPaint());

        // other shelf
        Rect otherShelf = new Rect(rightPoint - roomWidth/4, topPoint, rightPoint, topPoint + roomHeight/9);
        canvas.drawRect(otherShelf, Paintings.SHELF_FILL_LIGHT_PAINTING.getPaint());

        // tables
        Rect tablesTopRoom = new Rect(leftPoint, topPoint + roomHeight/5, leftPoint + roomWidth/3*2, topPoint + roomHeight/5*3);
        canvas.drawRect(tablesTopRoom, Paintings.SHELF_FILL_DARK_PAINTING.getPaint());
        canvas.drawText("Labortisch", tablesTopRoom.left + padding, tablesTopRoom.centerY(), Paintings.TEXT_PAINTING_SMALL.getPaint());



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

    protected  void drawAcrylicShelf(Canvas canvas)
    {

        if(canvas.getWidth() > canvas.getHeight())
            drawAcrylicShelfVerticalView(canvas);

        else
        {
            drawAcrylicShelfVerticalView(canvas);
        }
    }

    protected  void drawAcrylicShelfVerticalView(Canvas canvas)
    {
        Paint acrylicShelfPaint = Paintings.SHELF_STROKE_LIGHT_PAINTING.getPaint();

        int rightPoint = canvas.getWidth() - canvas.getWidth()/8;
        int leftPoint = canvas.getWidth()/8;
        int topPoint = padding;
        int bottomPoint = canvas.getHeight() - padding;
        int shelfHeight = rightPoint - leftPoint;
        int shelfWidth = bottomPoint - topPoint;

        // horizontal lines
        canvas.drawLine(leftPoint, topPoint + shelfWidth, rightPoint, bottomPoint, acrylicShelfPaint);
        canvas.drawLine(leftPoint, topPoint + shelfWidth/4*3, rightPoint, topPoint + shelfWidth/4*3, acrylicShelfPaint);
        canvas.drawLine(leftPoint, topPoint + shelfWidth/4*2, rightPoint, topPoint + shelfWidth/4*2, acrylicShelfPaint);
        canvas.drawLine(leftPoint, topPoint + shelfWidth/4*1, rightPoint, topPoint + shelfWidth/4*1, acrylicShelfPaint);

        canvas.drawLine(leftPoint, topPoint, leftPoint + shelfHeight/2, topPoint, acrylicShelfPaint);
        canvas.drawLine(leftPoint, topPoint + shelfWidth/8*1, leftPoint + shelfHeight/4*3, topPoint + shelfWidth/8*1, acrylicShelfPaint);


        // vertical lines
        // lines on the right
        canvas.drawLine(leftPoint + shelfHeight/3, bottomPoint - shelfWidth/4*2, leftPoint + shelfHeight/3, bottomPoint, acrylicShelfPaint);
        canvas.drawLine(leftPoint + shelfHeight/5*4, bottomPoint - shelfWidth/4*2, leftPoint + shelfHeight/5*4, bottomPoint, acrylicShelfPaint);

        canvas.drawLine(leftPoint + shelfHeight/2, bottomPoint - shelfWidth/4*2, leftPoint + shelfHeight/2, bottomPoint - shelfWidth/4, acrylicShelfPaint);
        canvas.drawLine(leftPoint + shelfHeight/9*5, bottomPoint - shelfWidth/4*1, leftPoint + shelfHeight/9*5, bottomPoint, acrylicShelfPaint);

        for(int i = 1; i<=9; i++)
        {
            canvas.drawLine(leftPoint + shelfHeight / 9 * i, topPoint + shelfWidth/4*1, leftPoint + shelfHeight / 9 * i, topPoint + shelfWidth/4*2, acrylicShelfPaint );
        }

        canvas.drawLine(leftPoint + shelfHeight/4*2, topPoint, leftPoint + shelfHeight/4*2, topPoint + shelfWidth/4, acrylicShelfPaint);
        canvas.drawLine(leftPoint + shelfHeight/4, topPoint, leftPoint + shelfHeight/4, topPoint + shelfWidth/4, acrylicShelfPaint);
        canvas.drawLine(leftPoint + shelfHeight/4*3, topPoint + shelfWidth/8, leftPoint + shelfHeight/4*3, topPoint + shelfWidth/4*1, acrylicShelfPaint);

        canvas.drawLine(leftPoint + shelfHeight, topPoint + shelfWidth/4*2, leftPoint + shelfHeight, topPoint + shelfWidth, acrylicShelfPaint );


        // text
        float textOffsetX = Paintings.TEXT_PAINTING_LARGE.getPaint().getTextSize() + padding;
        canvas.save();
        canvas.rotate(90, canvas.getWidth() - Paintings.TEXT_PAINTING_LARGE.getStrokeWith() - padding, padding);
        canvas.drawText("Plexiglasregal", canvas.getWidth() - textOffsetX, padding, Paintings.TEXT_PAINTING_LARGE.getPaint());
        canvas.restore();

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


    private void drawLocation(Canvas canvas, double positionX, double positionY, boolean isHorizontal)
    {
        int locationX;
        int locationY;
        if(!isHorizontal)
        {
            locationX = (int) (canvas.getWidth() * positionX);
            locationY = (int) (canvas.getHeight() * positionY);
            canvas.drawCircle(locationX, locationY, 20, Paintings.LOCATION_PAINTING.getPaint());
            canvas.drawText(locationName,  locationX, locationY, Paintings.TEXT_PAINTING_SMALL.getPaint());
        }
        else
        {
            locationY = (int) (canvas.getHeight() * positionX);
            locationX = (int) (canvas.getWidth() - canvas.getWidth() * positionY);
            canvas.drawCircle(locationX, locationY, 20, Paintings.LOCATION_PAINTING.getPaint());
            canvas.save();
            canvas.rotate(90, locationX, locationY);
            canvas.drawText(locationName,  locationX, locationY, Paintings.TEXT_PAINTING_SMALL.getPaint());
            canvas.restore();
        }

    }

}
