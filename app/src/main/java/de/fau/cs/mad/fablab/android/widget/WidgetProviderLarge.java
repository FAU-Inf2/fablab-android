package de.fau.cs.mad.fablab.android.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.widget.RemoteViews;

import net.spaceapi.HackerSpace;
import net.spaceapi.State;

import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.model.util.RestClient;
import de.fau.cs.mad.fablab.android.util.Formatter;
import de.fau.cs.mad.fablab.rest.myapi.SpaceApi;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class WidgetProviderLarge extends AppWidgetProvider
{
    private RestClient restClient;
    private SpaceApi mSpaceApi;
    private static String DOOR_STATE_WIDGET_UPDATE = "de.fau.cs.mad.fablab.android.widget.DOOR_STATE_WIDGET_UPDATE";
    private static final int space_name = 0x7f0700af;
    private boolean mOpen;
    private double mTime;
    private String mSpaceName;

    String FILENAME = "last_change";
    String FILENAME_IS_OPEN = "is_open";


    private Handler mSpaceApiHandler = new Handler();
    private Runnable mSpaceApiRunner = new SpaceApiRunner();

    private static final String LOG_TAG = "widget";
    public static final String FABLAB_WIDGET_UPDATE = "de.fau.cs.mad.fablab.android.widget.WidgetProvider.FABLAB_WIDGET_UPDATE";
    private static final DateFormat df = new SimpleDateFormat("hh:mm:ss");


    private Callback<HackerSpace> mSpaceApiCallback = new Callback<HackerSpace>()
    {
        @Override
        public void success(HackerSpace hackerSpace, Response response)
        {
            State state = hackerSpace.getState();
            updateState(state.getOpen(), state.getLastchange());

        }

        @Override
        public void failure(RetrofitError error)
        {

        }
    };

    private PendingIntent createDoorStateIntent(Context context)
    {
        Intent intent = new Intent(DOOR_STATE_WIDGET_UPDATE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;


    }

    @Override
    public void onEnabled(Context context)
    {
        super.onEnabled(context);
        Log.d(LOG_TAG, "widget Provider enabled. Starting timer to update widget every second");
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        mTime = 0.0;
        mOpen = false;

        restClient = new RestClient(context, false);
        mSpaceApi = restClient.getSpaceApi();
        mSpaceName = context.getString(R.string.space_name);

        mSpaceApiHandler.removeCallbacks(mSpaceApiRunner);
        mSpaceApi.getSpace(mSpaceName, mSpaceApiCallback);

        Calendar calender = Calendar.getInstance();
        calender.setTimeInMillis(System.currentTimeMillis());
        calender.add(Calendar.SECOND, 1);

        // update alarmManager each minute because the text view can display each minute
        // creats an intent which calls onReceive each time interval
        alarmManager.setRepeating(AlarmManager.RTC, calender.getTimeInMillis(), 10000, createDoorStateIntent(context));
    }


    @Override
    public void onDisabled(Context context)
    {
        super.onDisabled(context);
        Log.d(LOG_TAG, "Widget Provider disabled. Turning off timer");
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(createDoorStateIntent(context));
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        super.onReceive(context, intent);
        Log.d(LOG_TAG, "Received intent " + intent);

        restClient = new RestClient(context, false);
        mSpaceApi = restClient.getSpaceApi();
        mSpaceName = context.getString(R.string.space_name);

        mSpaceApiHandler.removeCallbacks(mSpaceApiRunner);
        mSpaceApi.getSpace(mSpaceName, mSpaceApiCallback);


        if (DOOR_STATE_WIDGET_UPDATE.equals(intent.getAction()))
        {
            Log.d(LOG_TAG, "Door state update");
            // Get the widget manager and ids for this widget provider, then call the shared
            // door state update method
            ComponentName thisAppWidget = new ComponentName(context.getPackageName(), getClass().getName());
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int ids[] = appWidgetManager.getAppWidgetIds(thisAppWidget);
            for (int appWidgetID : ids)
            {
                updateAppWidget(context, appWidgetManager, appWidgetID, mTime, mOpen);
            }
        }
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
    {
        super.onUpdate(context, appWidgetManager, appWidgetIds);


        for (int appWidgetID : appWidgetIds)
        {
            updateAppWidget(context, appWidgetManager, appWidgetID, mTime, mOpen);
        }

    }

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId, double lastChange, boolean isOpen)
    {

        long currentTimeSeconds = System.currentTimeMillis() / 1000L;
        double minutesSinceLastChange = (currentTimeSeconds - lastChange) / 60;
        long timeSinceLastChange = Double.valueOf(minutesSinceLastChange).longValue();
        String timeSinceLastChangeAsString;

        if(lastChange == 0.0)
            timeSinceLastChangeAsString = "0m";
        else
            timeSinceLastChangeAsString = Formatter.formatTime(timeSinceLastChange);

        RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout_large);
        updateViews.setTextViewText(R.id.widget_tv, timeSinceLastChangeAsString);

        if(isOpen)
        {
            updateViews.setTextColor(R.id.widget_tv, context.getResources().getColor(R.color.appbar_color_opened));
            updateViews.setImageViewResource(R.id.widget_iv, R.drawable.opened);
        }
        else
        {
            updateViews.setTextColor(R.id.widget_tv, context.getResources().getColor(R.color.appbar_color_closed));
            updateViews.setImageViewResource(R.id.widget_iv, R.drawable.closed);
        }
        appWidgetManager.updateAppWidget(appWidgetId, updateViews);
    }

    public void updateState(boolean isOpen, double time)
    {
        mOpen = isOpen;
        mTime = time;
    }

    private class SpaceApiRunner implements Runnable {

        @Override
        public void run() {
            mSpaceApi.getSpace(mSpaceName, mSpaceApiCallback);
        }
    }

}
