package de.fau.cs.mad.fablab.android.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import java.lang.annotation.Annotation;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.model.SpaceApiModel;
import de.fau.cs.mad.fablab.android.util.Formatter;
import de.fau.cs.mad.fablab.android.view.actionbar.ActionBarViewModel;
import de.fau.cs.mad.fablab.android.view.activities.MainActivity;




public class WidgetProvider extends AppWidgetProvider
{
//    @Inject
//    ActionBarViewModel mViewModel;
//
//    @Inject
//    SpaceApiModel mSpaceApiModel;

//    boolean isOpen;
//    String widgetTime;
//    final String SET_TIME = "set_time";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
    {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        //isOpen = mSpaceApiModel.getOpen();
        //widgetTime = Formatter.formatTime(mSpaceApiModel.getTime());

//        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
//        ComponentName widgetComponent = new ComponentName(context, WidgetProvider.class);
//
//        remoteViews.setTextViewText(R.id.widget_tv, widgetTime);
//        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);


    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        super.onReceive(context, intent);


    }




}
