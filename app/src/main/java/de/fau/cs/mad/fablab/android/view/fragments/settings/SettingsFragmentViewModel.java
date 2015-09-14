package de.fau.cs.mad.fablab.android.view.fragments.settings;

import android.app.Application;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.provider.CalendarContract;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.BuildConfig;
import de.fau.cs.mad.fablab.android.model.AutoCompleteModel;
import de.fau.cs.mad.fablab.android.model.ICalModel;
import de.fau.cs.mad.fablab.android.model.PushModel;
import de.fau.cs.mad.fablab.android.model.SpaceApiModel;
import de.fau.cs.mad.fablab.android.model.VersionCheckModel;
import de.fau.cs.mad.fablab.android.model.events.AddCalendarEvent;
import de.fau.cs.mad.fablab.android.model.events.ExistingCalendarEvent;
import de.fau.cs.mad.fablab.android.model.events.NoUpdateAvailableEvent;
import de.fau.cs.mad.fablab.android.view.activities.MainActivity;
import de.fau.cs.mad.fablab.android.viewmodel.common.ObservableArrayList;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.fau.cs.mad.fablab.rest.core.ICal;
import de.greenrobot.event.EventBus;

public class SettingsFragmentViewModel implements SharedPreferences.OnSharedPreferenceChangeListener,
        Preference.OnPreferenceClickListener {
    private static final String KEY_PREF_ENABLE_PUSH = "enable_push";
    private static final String KEY_PREF_POLLING_FREQ = "spaceapi_polling_freq";
    private static final String KEY_PREF_FORCE_RELOAD_AUTOCOMPLETION = "force_reload_autocompletion";
    private static final String KEY_PREF_CHECK_FOR_UPDATES = "check_for_updates";
    private static final String KEY_PREF_ADD_CALENDAR = "add_calendar";

    @Inject
    SpaceApiModel mSpaceApiModel;
    @Inject
    PushModel mPushModel;
    @Inject
    AutoCompleteModel mAutoCompleteModel;
    @Inject
    VersionCheckModel mVersionCheckModel;
    @Inject
    ICalModel mIcalModel;

    private Context context;


    @Override
    public boolean onPreferenceClick(Preference preference) {
        switch (preference.getKey()) {
            case KEY_PREF_FORCE_RELOAD_AUTOCOMPLETION:
                mAutoCompleteModel.forceReloadProductNames();
                break;
            case KEY_PREF_CHECK_FOR_UPDATES:
                mVersionCheckModel.checkVersion();
                break;
            case KEY_PREF_ADD_CALENDAR:
                doCalendar(context, "FabLab", "FabLab");
                break;
        }
        return true;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key) {
            case KEY_PREF_ENABLE_PUSH:
                if (sharedPreferences.getBoolean(key, false)) {
                    mPushModel.registerDeviceToGcm();
                } else {
                    mPushModel.unregisterDeviceFromGcm();
                }
                break;
            case KEY_PREF_POLLING_FREQ:
                long pollingFrequency = Integer.parseInt(sharedPreferences.getString(
                        key, "15")) * 60 * 1000;
                mSpaceApiModel.setPollingFrequency(pollingFrequency);
                break;
        }
    }

    public void initialize(PreferenceScreen preferenceScreen, SharedPreferences sharedPreferences) {
        if (BuildConfig.FLAVOR.equals("fdroid")) {
            Preference preference = preferenceScreen.findPreference(KEY_PREF_ENABLE_PUSH);
            if (preference != null) {
                preferenceScreen.removePreference(preference);
            }
        }

        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        preferenceScreen.findPreference(KEY_PREF_FORCE_RELOAD_AUTOCOMPLETION)
                .setOnPreferenceClickListener(this);
        preferenceScreen.findPreference(KEY_PREF_CHECK_FOR_UPDATES)
                .setOnPreferenceClickListener(this);
        preferenceScreen.findPreference(KEY_PREF_ADD_CALENDAR)
                .setOnPreferenceClickListener(this);

        context = preferenceScreen.getContext();

    }
    public static final String[] EVENT_PROJECTION = new String[] {
            CalendarContract.Calendars._ID,                           // 0
            CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,         // 2
            CalendarContract.Calendars.OWNER_ACCOUNT                  // 3
    };

    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
    private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
    private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;

    private void doCalendar(Context context, String name, String accountName)
    {
        Uri uri;
        EventBus eventBus = EventBus.getDefault();
        if(!existsFablabCalendar(context, name, accountName))
        {
            uri = createCalendarWithName(context, name, accountName);
            eventBus.post(new AddCalendarEvent());
        }
        else
            eventBus.post(new ExistingCalendarEvent());

    }

    public long getCalID(Context context, String name, String accountName)
    {
        long calendarId = 0;
        Cursor cur = null;
        ContentResolver cr = context.getContentResolver();
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        String selection = "((" + CalendarContract.Calendars.ACCOUNT_NAME + " = ?) AND ("
                + CalendarContract.Calendars.ACCOUNT_TYPE + " = ?) AND ("
                + CalendarContract.Calendars.NAME + " = ?))";
        String[] selectionArgs = new String[] {accountName, CalendarContract.ACCOUNT_TYPE_LOCAL, name};

        cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);

        while(cur.moveToNext())
        {
            calendarId = cur.getLong(PROJECTION_ID_INDEX);
        }
        return calendarId;
    }


    public boolean existsFablabCalendar(Context context, String name, String accountName)
    {
        Cursor cur = null;
        ContentResolver cr = context.getContentResolver();
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        String selection = "((" + CalendarContract.Calendars.ACCOUNT_NAME + " = ?) AND ("
                                + CalendarContract.Calendars.ACCOUNT_TYPE + " = ?) AND ("
                                + CalendarContract.Calendars.NAME + " = ?))";
        String[] selectionArgs = new String[] {accountName, CalendarContract.ACCOUNT_TYPE_LOCAL, name};

        cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);

        if(cur.moveToNext())
            return true;
        else
            return false;
    }

    public Uri createCalendarWithName(Context ctx, String name,String accountName) {

        Uri target = Uri.parse(CalendarContract.Calendars.CONTENT_URI.toString());
        target = target.buildUpon().appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, accountName)
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, "com.google").build();

        ContentValues values = new ContentValues();
        values.put(CalendarContract.Calendars.ACCOUNT_NAME, accountName);
        values.put(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL);
        values.put(CalendarContract.Calendars.NAME, name);
        values.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, name);
        values.put(CalendarContract.Calendars.CALENDAR_COLOR, 0x00FF00);
        values.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_ROOT);
        values.put(CalendarContract.Calendars.OWNER_ACCOUNT, accountName);
        values.put(CalendarContract.Calendars.VISIBLE, 1);
        values.put(CalendarContract.Calendars.SYNC_EVENTS, 1);
        values.put(CalendarContract.Calendars.CALENDAR_TIME_ZONE, "Europe/Berlin");

        Uri newCalendar = ctx.getContentResolver().insert(target, values);

        createCalendarEvents();

        return newCalendar;
    }

    public void createCalendarEvents()
    {
        long calendarId = getCalID(context, "FabLab", "FabLab");
        ObservableArrayList<ICal> iCals =  mIcalModel.getICalsList();

        for(ICal iCal: iCals)
        {
            createEventWithName(context, iCal, calendarId);
        }
    }

    public static void createEventWithName(Context ctx, ICal iCal, long calendarId ) {
        long startMillis = 0;
        long endMillis = 0;
        String name = "";
        String description = "";

        TimeZone timeZone = TimeZone.getDefault();
        Date now = new Date();
        int offsetFromUTC = timeZone.getOffset(now.getTime());

        Calendar calStart = Calendar.getInstance();
        calStart.setTime(iCal.getDtstartAsDate());
        calStart.add(Calendar.MILLISECOND, offsetFromUTC);

        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(iCal.getEndAsDate());
        calEnd.add(Calendar.MILLISECOND, offsetFromUTC);

        startMillis = calStart.getTimeInMillis();
        endMillis = calEnd.getTimeInMillis();
        name = iCal.getSummery();
        description = iCal.getDescription();

        ContentValues cv = new ContentValues();
        cv.put(CalendarContract.Events.TITLE, name);
        cv.put(CalendarContract.Events.DESCRIPTION, description);
        cv.put(CalendarContract.Events.DTSTART, startMillis);
        cv.put(CalendarContract.Events.DTEND, endMillis);
        cv.put(CalendarContract.Events.CALENDAR_ID, calendarId);
        cv.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().toString());

        Uri newEvent = ctx.getContentResolver().insert(CalendarContract.Events.CONTENT_URI, cv);
    }

}
