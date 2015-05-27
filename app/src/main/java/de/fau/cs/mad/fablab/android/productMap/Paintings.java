package de.fau.cs.mad.fablab.android.productMap;

import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Michael on 22.05.2015.
 */
public enum Paintings
{
    ROOM_PAINTING(Color.BLACK, Paint.Style.STROKE, 10),
    DOOR_PAINTING(Color.GREEN, Paint.Style.STROKE, 10),
    WINDOW_PAINTING(Color.argb(255,0,255,255), Paint.Style.STROKE, 10),

    GRAY_PAINTING(Color.GRAY, Paint.Style.FILL, 10),
    LOCATION_PAINTING(Color.RED, Paint.Style.FILL, 20),

    SHELF_STROKE_DARK_PAINTING(Color.argb(255,139,69,19), Paint.Style.STROKE, 10),
    SHELF_STROKE_LIGHT_PAINTING(Color.argb(255, 205, 133, 63), Paint.Style.STROKE, 10),

    SHELF_FILL_DARK_PAINTING(Color.argb(255,139,69,19), Paint.Style.FILL, 10),
    SHELF_FILL_LIGHT_PAINTING(Color.argb(255, 205, 133, 63), Paint.Style.FILL, 10),

    SHELF_PAINTING(Color.argb(255,139,69,19), Paint.Style.FILL_AND_STROKE, 10),
    TEXT_PAINTING_LARGE(Color.BLACK, Paint.Style.STROKE, 50),
    TEXT_PAINTING_SMALL(Color.BLACK, Paint.Style.STROKE, 30);

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
        }
        return result;
    }

    public int getStrokeWith(){return strokeWith;}

}
