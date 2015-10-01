package de.fau.cs.mad.fablab.android.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

import net.spaceapi.HackerSpace;
import net.spaceapi.State;

import java.lang.annotation.Annotation;
import java.security.Provider;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.model.SpaceApiModel;
import de.fau.cs.mad.fablab.android.model.util.RestClient;
import de.fau.cs.mad.fablab.android.util.Formatter;
import de.fau.cs.mad.fablab.android.view.actionbar.ActionBarViewModel;
import de.fau.cs.mad.fablab.android.view.activities.MainActivity;
import de.fau.cs.mad.fablab.rest.myapi.SpaceApi;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class WidgetProvider extends AppWidgetProvider
{
    RestClient restClient;
    SpaceApiModel mSpaceApiModel;
    SpaceApi mSpaceApi;

    private boolean mOpen;
    private long mTime;

    public static final String FABLAB_WIDGET_UPDATE = "de.fau.cs.mad.fablab.android.widget.WidgetProvider.FABLAB_WIDGET_UPDATE";


    private Callback<HackerSpace> mSpaceApiCallback = new Callback<HackerSpace>() {
        @Override
        public void success(HackerSpace hackerSpace, Response response) {
            State state = hackerSpace.getState();
            updateState(state.getOpen(), state.getLastchange());
        }

        @Override
        public void failure(RetrofitError error) {

        }
    };

    private void updateState(boolean open, double lastChange)
    {
        mOpen = open;

        long currentTimeSeconds = System.currentTimeMillis() / 1000L;
        double minutesSinceLastChange = (currentTimeSeconds - lastChange) / 60;
        mTime = Double.valueOf(minutesSinceLastChange).longValue();
    }



    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
    {
        final int N = appWidgetIds.length;
        for(int i = 0; i<N; i++)
        {
            int appWidgetId = appWidgetIds[i];

            // update a label
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            views.setTextViewText(R.id.widget_tv, "yeah");
        }


        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Timer timer = new Timer();
        new MyTime(context, appWidgetManager);
    }

    private class MyTime extends TimerTask
    {

        RemoteViews remoteViews;
        AppWidgetManager appWidgetManager;
        ComponentName thisWidget;
        SpaceApiModel spaceApiModel;
        DateFormat format = SimpleDateFormat.getTimeInstance(SimpleDateFormat.MEDIUM, Locale.getDefault());

        public MyTime(Context context, AppWidgetManager appWidgetManager)
        {
            this.appWidgetManager = appWidgetManager;
            remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            thisWidget = new ComponentName(context, WidgetProvider.class);

//            restClient = new RestClient(context, false);
//            mSpaceApiModel = new SpaceApiModel(restClient.getSpaceApi(), context.getString(R.string.space_name), 6000);
//            mTime = mSpaceApiModel.getTime();
        }

        @Override
        public void run()
        {

            remoteViews.setTextViewText(R.id.widget_tv, "TIME = " + format.format(new Date())); //time test


//            String time = Formatter.formatTime(mTime);
//            remoteViews.setTextViewText(R.id.widget_tv, time);

            appWidgetManager.updateAppWidget(thisWidget, remoteViews);
        }


    }
}
