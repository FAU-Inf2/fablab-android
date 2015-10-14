package de.fau.cs.mad.fablab.android.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.util.Formatter;
import de.fau.cs.mad.fablab.android.view.activities.MainActivity;

public class WidgetProviderSmall extends WidgetProviderBase
{
    @Override
    protected void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId, double lastChange, boolean isOpen)
    {

        long currentTimeSeconds = System.currentTimeMillis() / 1000L;
        double minutesSinceLastChange = (currentTimeSeconds - lastChange) / 60;
        long timeSinceLastChange = Double.valueOf(minutesSinceLastChange).longValue();
        String timeSinceLastChangeAsString;

        if(lastChange == 0.0) {
            timeSinceLastChangeAsString = "-";
        } else {
            timeSinceLastChangeAsString = Formatter.formatTimeWidget(timeSinceLastChange);
        }

        RemoteViews updateViews = new RemoteViews(context.getPackageName(), setRemoteViews());
        updateViews.setTextViewText(R.id.widget_tv, timeSinceLastChangeAsString);

        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntentClick = PendingIntent.getActivity(context, 0, intent, 0);
        updateViews.setOnClickPendingIntent(R.id.widget, pendingIntentClick);

        if(isOpen)
        {
            updateViews.setTextColor(R.id.widget_tv, 0xFF440000);
            updateViews.setInt(R.id.widget_tv, "setBackgroundResource", R.drawable.shape_rounded_rect_green_widget);
        }
        else
        {
            updateViews.setTextColor(R.id.widget_tv, 0xFF004400);
            updateViews.setInt(R.id.widget_tv, "setBackgroundResource", R.drawable.shape_rounded_rect_red_widget);
        }
        appWidgetManager.updateAppWidget(appWidgetId, updateViews);
    }

    @Override
    int setRemoteViews()
    {
        return R.layout.widget_layout_small;
    }
}
