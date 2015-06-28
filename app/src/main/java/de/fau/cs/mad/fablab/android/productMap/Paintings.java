package de.fau.cs.mad.fablab.android.productMap;

import android.app.Notification;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

public enum Paintings
{
    ROOM_PAINTING(Color.BLACK, Paint.Style.STROKE, 10),
    DOOR_PAINTING(Color.argb(255, 153, 0, 0), Paint.Style.STROKE, 10),
    WINDOW_PAINTING(Color.argb(255, 100, 100, 255), Paint.Style.STROKE, 10),

    MACHINE_PAINTING(Color.argb(150, 0x78, 0x94, 0x87), Paint.Style.FILL, 10),
    TABLE_PAINTING(Color.argb(150, 0x78, 0x8F, 94), Paint.Style.FILL, 10),
    SHELF_PAINTING_FILL(Color.argb(150, 0x9C, 0x51, 0x51), Paint.Style.FILL, 10),
    SHELF_PAINTING_STROKE(Color.argb(150, 0x9C, 0x51, 0x51), Paint.Style.STROKE, 10),


    LOCATION_PAINTING(Color.argb(255, 0xCC, 00, 00), Paint.Style.FILL, 15),

    TEXT_PAINTING_LARGE(Color.BLACK, Paint.Style.STROKE, 50),
    TEXT_PAINTING_SMALL(Color.BLACK, Paint.Style.STROKE, 30),
    TEXT_PAINTING_LOCATION(Color.argb(255, 0xCC, 00, 00), Paint.Style.STROKE, 35);


    private static int opacity = 200;


    private int color;
    private Paint.Style style;
    private int strokeWith;

    private Paint result;

    private Paintings(int color, Paint.Style style, int strokeWith)
    {
        this.color = color;
        this.style = style;
        this.strokeWith = strokeWith;

        result = new Paint();
    }

    public Paint getPaint()
    {
        result.setColor(color);

        if(!this.name().startsWith("TEXT_PAINTING_"))
        {
            result.setStrokeWidth(strokeWith);
            result.setStyle(style);
        }
        else
        {
            result.setTextSize(strokeWith);
            if(this.name().startsWith(("TEXT_PAINTING_LOCATION")))
            {
                Typeface locationTypeface = Typeface.create("Helvetica", Typeface.BOLD);
                result.setTypeface(locationTypeface);
            }
        }
        return result;
    }

    public int getStrokeWith(){return strokeWith;}

}
