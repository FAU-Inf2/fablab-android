package de.fau.cs.mad.fablab.android.productMap;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;;
import android.graphics.Rect;
import android.view.View;


public class DrawingActivity extends View
{

    private FablabView fablabView;
    private double positionX;
    private double positionY;
    private Canvas canvas;
    private String locationName;



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
                break;

        }

    }

    protected int padding = 10;

    protected void drawFablabLayout(Canvas canvas)
    {

        int startLeftPoint = 0;
        int startRightPoint = 0;
        int startTopPoint = 0;
        int startBottomPoint = 0;
        int width = 0;
        int height = 0;


        //contours

        // draw fablab contour
        //startLeftPoint = padding;
        //startTopPoint = padding;
        //startRightPoint = canvas.getWidth() - padding;
        //startBottomPoint = canvas.getHeight() - padding;
        //drawRectFromLeftPointToRightPoint( canvas, startLeftPoint, startTopPoint, startRightPoint, startBottomPoint, Paintings.ROOM_PAINTING, "" );

        // important points for drawing
        int halfWallOffset = Paintings.ROOM_PAINTING.getStrokeWith()/2;

        int leftPointOfFablab = padding + halfWallOffset;
        int rightPointOfFablab = canvas.getWidth() - padding - halfWallOffset;
        int topPointOfFablab = padding + halfWallOffset;
        int bottomPointOfFablab = canvas.getHeight() - padding - halfWallOffset;

        int topPointOfLowerFablabRoom = canvas.getHeight()/2 + halfWallOffset;
        int bottomPointOfUpperFablabRoom = canvas.getHeight()/2 - halfWallOffset;

        int wallOffset = Paintings.ROOM_PAINTING.getStrokeWith()/2;
        int heightOfOneFablabRoom = bottomPointOfFablab - topPointOfLowerFablabRoom;
        int widthOfOneFablabRoom = rightPointOfFablab - leftPointOfFablab;

        int entryDoorBottomPoint = canvas.getHeight()/2 + heightOfOneFablabRoom /3*2;
        int entryDoorTopPoint = canvas.getHeight()/2 + heightOfOneFablabRoom /3;
        int doorLeftPoint = rightPointOfFablab - widthOfOneFablabRoom/5;
        int doorRightPoint = rightPointOfFablab - padding;


        // fablab with lines
        canvas.drawLine(padding/2, padding, canvas.getWidth() - padding/2, padding, Paintings.ROOM_PAINTING.getPaint()); // top wall
        canvas.drawLine(padding/2, canvas.getHeight() - padding, doorLeftPoint, canvas.getHeight() - padding, Paintings.ROOM_PAINTING.getPaint()); // bottom wall part 1
        canvas.drawLine(doorRightPoint, canvas.getHeight() - padding, canvas.getWidth() - padding/2, canvas.getHeight() - padding, Paintings.ROOM_PAINTING.getPaint()); // bottom wall part 1
        canvas.drawLine(canvas.getWidth() - padding, padding, canvas.getWidth() - padding, canvas.getHeight() - padding, Paintings.ROOM_PAINTING.getPaint()); // right wall
        canvas.drawLine(padding, padding, padding, entryDoorTopPoint, Paintings.ROOM_PAINTING.getPaint()); // left wall part 1
        canvas.drawLine(padding, entryDoorBottomPoint, padding, canvas.getHeight() - padding, Paintings.ROOM_PAINTING.getPaint()); // left wall part 2
        canvas.drawLine(leftPointOfFablab, canvas.getHeight() / 2, doorLeftPoint, canvas.getHeight() / 2, Paintings.ROOM_PAINTING.getPaint()); // wall between the two fablab rooms part 1
        canvas.drawLine(doorRightPoint, canvas.getHeight() / 2, rightPointOfFablab, canvas.getHeight() / 2, Paintings.ROOM_PAINTING.getPaint()); // wall between the two fablab rooms part 2

        // draw text "fablab" seperate because it should be in the top left corner
        canvas.drawText("FabLab", leftPointOfFablab + padding, topPointOfFablab + Paintings.TEXT_PAINTING_LARGE.getPaint().getTextSize() + padding, Paintings.TEXT_PAINTING_LARGE.getPaint());


        // draw doors
        int openDoorOffset = 20;

        // entry door
        canvas.drawLine(padding + openDoorOffset, entryDoorTopPoint + openDoorOffset, padding, entryDoorBottomPoint, Paintings.DOOR_PAINTING.getPaint());

        // inner doors

        // door between fablab rooms
        canvas.drawLine(doorLeftPoint, canvas.getHeight()/2, doorRightPoint - openDoorOffset, canvas.getHeight()/2 - openDoorOffset, Paintings.DOOR_PAINTING.getPaint());
        // room door bottom
        canvas.drawLine(doorLeftPoint, canvas.getHeight() - padding, doorRightPoint - openDoorOffset, canvas.getHeight() - padding - openDoorOffset, Paintings.DOOR_PAINTING.getPaint());

        // windows
        int windowWith = heightOfOneFablabRoom /6;

        canvas.drawLine(canvas.getWidth() - padding, topPointOfFablab + windowWith, canvas.getWidth() - padding, topPointOfFablab + windowWith*2, Paintings.WINDOW_PAINTING.getPaint());
        canvas.drawLine(canvas.getWidth() - padding, topPointOfFablab + windowWith*4, canvas.getWidth() - padding, topPointOfFablab + windowWith*5, Paintings.WINDOW_PAINTING.getPaint());

        canvas.drawLine(canvas.getWidth() - padding, bottomPointOfUpperFablabRoom + windowWith*(5/2), canvas.getWidth() - padding, bottomPointOfUpperFablabRoom + windowWith*(7/2), Paintings.WINDOW_PAINTING.getPaint());
        canvas.drawLine(canvas.getWidth() - padding, bottomPointOfUpperFablabRoom + windowWith*4, canvas.getWidth() - padding, bottomPointOfUpperFablabRoom + windowWith*5, Paintings.WINDOW_PAINTING.getPaint());

        int offsetLeftWall = wallOffset + padding;

        // inner rooms

        // bottom room

        // milling machine
        int millingMachineRightPoint = leftPointOfFablab + widthOfOneFablabRoom /4;

        startLeftPoint = leftPointOfFablab;
        startTopPoint = topPointOfLowerFablabRoom;
        startRightPoint = millingMachineRightPoint;
        startBottomPoint = entryDoorTopPoint;
        drawRectFromLeftPointToRightPoint(canvas, startLeftPoint, startTopPoint, startRightPoint, startBottomPoint, Paintings.MACHINE_PAINTING, "Fr\u00e4se");

        // workbench
        // part 1, main part, upper part
        startLeftPoint = millingMachineRightPoint;
        startTopPoint = topPointOfLowerFablabRoom;
        startRightPoint = doorLeftPoint;
        startBottomPoint = topPointOfLowerFablabRoom + heightOfOneFablabRoom /8;
        drawRectFromLeftPointToRightPoint(canvas, startLeftPoint, startTopPoint, startRightPoint, startBottomPoint, Paintings.SHELF_PAINTING_FILL, "Werkbank");
        // part 2, small right part below main part
        startLeftPoint = doorLeftPoint - widthOfOneFablabRoom / 5;
        startTopPoint = topPointOfLowerFablabRoom + heightOfOneFablabRoom/8;
        startRightPoint = doorLeftPoint;
        startBottomPoint = topPointOfLowerFablabRoom + heightOfOneFablabRoom /8*2;
        drawRectFromLeftPointToRightPoint(canvas, startLeftPoint, startTopPoint, startRightPoint, startBottomPoint, Paintings.SHELF_PAINTING_FILL, "");

        // chemistry bench

        // part 1, main part, bottom part
        startLeftPoint = leftPointOfFablab;
        startTopPoint = bottomPointOfFablab - heightOfOneFablabRoom / 8;
        startRightPoint = leftPointOfFablab + widthOfOneFablabRoom /5*2;
        startBottomPoint = bottomPointOfFablab;
        drawRectFromLeftPointToRightPoint(canvas, startLeftPoint, startTopPoint, startRightPoint, startBottomPoint, Paintings.SHELF_PAINTING_FILL, "Chemietisch");
        // part 2, left square part
        startLeftPoint = leftPointOfFablab;
        startTopPoint = entryDoorBottomPoint;
        startRightPoint = leftPointOfFablab + widthOfOneFablabRoom /5;
        startBottomPoint = bottomPointOfFablab - heightOfOneFablabRoom / 8;
        drawRectFromLeftPointToRightPoint(canvas, startLeftPoint, startTopPoint, startRightPoint, startBottomPoint, Paintings.SHELF_PAINTING_FILL, "");

        // engine lathe
        startLeftPoint = leftPointOfFablab + widthOfOneFablabRoom /5*2;
        startTopPoint = bottomPointOfFablab - heightOfOneFablabRoom / 8;
        startRightPoint = doorLeftPoint;
        startBottomPoint = bottomPointOfFablab;
        drawRectFromLeftPointToRightPoint(canvas, startLeftPoint, startTopPoint, startRightPoint, startBottomPoint, Paintings.MACHINE_PAINTING, "Drehbank");


        //tables in the room below
        startRightPoint = rightPointOfFablab;
        startTopPoint = bottomPointOfUpperFablabRoom + heightOfOneFablabRoom /7*3;
        width = widthOfOneFablabRoom /2;
        height = heightOfOneFablabRoom /7*2;
        drawRectFromRightPointToLeftSide(canvas, startRightPoint, startTopPoint, width, height, Paintings.TABLE_PAINTING, "Labortisch");

        // top room
        // acrylic shelf
        startLeftPoint = leftPointOfFablab;
        startTopPoint = bottomPointOfUpperFablabRoom - heightOfOneFablabRoom/6;
        startRightPoint = doorLeftPoint;
        startBottomPoint = bottomPointOfUpperFablabRoom;
        drawRectFromLeftPointToRightPoint(canvas, startLeftPoint, startTopPoint, startRightPoint, startBottomPoint, Paintings.SHELF_PAINTING_FILL, "Plexiglasregal");

        // screw shelf
        startLeftPoint = rightPointOfFablab - widthOfOneFablabRoom /8;
        startTopPoint = topPointOfFablab + windowWith * 2;
        width = widthOfOneFablabRoom /8;
        height = windowWith*2;
        drawRectFromLeftToRightSide(canvas, startLeftPoint, startTopPoint, width, height, Paintings.SHELF_PAINTING_FILL, "");

        // other shelf on the top
        startLeftPoint = rightPointOfFablab - widthOfOneFablabRoom /4;
        startTopPoint = topPointOfFablab;
        width = widthOfOneFablabRoom /4;
        height = heightOfOneFablabRoom /9;
        drawRectFromLeftToRightSide(canvas, startLeftPoint, startTopPoint, width, height, Paintings.SHELF_PAINTING_FILL, "");

        // tables
        startLeftPoint = leftPointOfFablab;
        startTopPoint = topPointOfFablab + heightOfOneFablabRoom /5;
        width = widthOfOneFablabRoom /3*2;
        height = heightOfOneFablabRoom /5*2;
        drawRectFromLeftToRightSide(canvas, startLeftPoint, startTopPoint, width, height, Paintings.TABLE_PAINTING, "Labortisch");

    }


    private void drawRectFromLeftToRightSide(Canvas canvas, int startPointLeft, int startPointTop, int width, int height, Paintings rectPainting, String rectDescription)
    {
        Rect myNewRect = new Rect(startPointLeft, startPointTop, startPointLeft + width, startPointTop + height);
        canvas.drawRect(myNewRect, rectPainting.getPaint());

        drawDescriptonInRect(canvas, myNewRect, rectPainting, rectDescription);
    }

    private void drawRectFromRightPointToLeftSide(Canvas canvas, int startPointRight, int startPointTop, int width, int height, Paintings rectPainting, String rectDescription)
    {
        Rect myNewRect = new Rect(startPointRight - width, startPointTop, startPointRight, startPointTop + height);
        canvas.drawRect(myNewRect, rectPainting.getPaint());

        drawDescriptonInRect(canvas, myNewRect, rectPainting, rectDescription);
    }

    private void drawRectFromLeftPointToRightPoint(Canvas canvas, int leftPoint, int topPoint, int rightPoint, int bottomPoint, Paintings rectPainting, String rectDescription)
    {
        Rect myNewRect = new Rect(leftPoint, topPoint, rightPoint, bottomPoint);
        canvas.drawRect(myNewRect, rectPainting.getPaint());

        drawDescriptonInRect(canvas, myNewRect, rectPainting, rectDescription);
    }

    private void drawDescriptonInRect(Canvas canvas, Rect rect, Paintings rectPainting, String rectDescription)
    {
        float descriptionLength = Paintings.TEXT_PAINTING_SMALL.getPaint().measureText(rectDescription);
        float descriptionHeight = Paintings.TEXT_PAINTING_SMALL.getPaint().getTextSize();
        canvas.drawText(rectDescription, rect.centerX() - descriptionLength/2, rect.centerY() + (descriptionHeight/2), Paintings.TEXT_PAINTING_SMALL.getPaint() );
    }


    protected void drawElectricWorkshop(Canvas canvas)
    {

        Paint electricWorkshopPaint = Paintings.SHELF_PAINTING_STROKE.getPaint();
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
        Paint acrylicShelfPaint = Paintings.SHELF_PAINTING_STROKE.getPaint();

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
        int circleRadius = Paintings.LOCATION_PAINTING.getStrokeWith();
        if(!isHorizontal)
        {
            locationX = (int) (canvas.getWidth() * positionX);
            locationY = (int) (canvas.getHeight() * positionY);
            canvas.drawCircle(locationX, locationY, circleRadius, Paintings.LOCATION_PAINTING.getPaint());

            float textLength = Paintings.TEXT_PAINTING_LOCATION.getPaint().measureText(locationName);
            if(positionX > 0.5)
            {
                canvas.drawText(locationName, locationX - (textLength+circleRadius), locationY + Paintings.TEXT_PAINTING_LOCATION.getPaint().getTextSize()/2, Paintings.TEXT_PAINTING_LOCATION.getPaint());
            }
            else
            {
                canvas.drawText(locationName, locationX + circleRadius, locationY + Paintings.TEXT_PAINTING_LOCATION.getPaint().getTextSize()/2, Paintings.TEXT_PAINTING_LOCATION.getPaint());
            }
        }
        else
        {
            locationY = (int) (canvas.getHeight() * positionX);
            locationX = (int) (canvas.getWidth() - canvas.getWidth() * positionY);
            canvas.drawCircle(locationX, locationY, circleRadius, Paintings.LOCATION_PAINTING.getPaint());
            canvas.save();
            canvas.rotate(90, locationX, locationY);
            canvas.drawText(locationName,  locationX, locationY, Paintings.TEXT_PAINTING_LOCATION.getPaint());
            canvas.restore();
        }

    }

}
