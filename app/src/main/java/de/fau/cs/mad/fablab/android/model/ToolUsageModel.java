package de.fau.cs.mad.fablab.android.model;

import android.widget.Toast;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import de.fau.cs.mad.fablab.android.model.events.NotificationEvent;
import de.fau.cs.mad.fablab.android.model.events.ReservationChangedEvent;
import de.fau.cs.mad.fablab.android.model.events.ShowToastEvent;
import de.fau.cs.mad.fablab.android.util.Formatter;
import de.fau.cs.mad.fablab.android.viewmodel.common.ObservableArrayList;
import de.fau.cs.mad.fablab.rest.core.FabTool;
import de.fau.cs.mad.fablab.rest.core.ToolUsage;
import de.fau.cs.mad.fablab.rest.core.User;
import de.fau.cs.mad.fablab.rest.myapi.ToolUsageApi;
import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ToolUsageModel {

    private ObservableArrayList<ToolUsage> mUsages;
    private List<ToolUsage> mOwnToolUsages;
    private List<FabTool> mTools;
    private ToolUsageApi mToolUsageApi;
    private long mToolId;
    private String mUuidRandom;
    private String mCurrentTool;
    private String mCurrentProject;

    EventBus mEventBus = EventBus.getDefault();

    private Callback<List<FabTool>> mFabToolsCallback = new Callback<List<FabTool>>() {
        @Override
        public void success(List<FabTool> tools, Response response) {
            mTools.clear();
            mTools.addAll(tools);
            Collections.sort(mTools, new Comparator<FabTool>() {
                @Override
                public int compare(FabTool lhs, FabTool rhs) {
                    Collator collator = Collator.getInstance(Locale.GERMAN);
                    collator.setStrength(Collator.SECONDARY);
                    return collator.compare(lhs.getTitle(), rhs.getTitle());
                }
            });
        }

        @Override
        public void failure(RetrofitError error) {
        }
    };

    private Callback<List<ToolUsage>> mToolUsagesCallback = new Callback<List<ToolUsage>>() {
        @Override
        public void success(List<ToolUsage> usages, Response response) {
            mUsages.clear();
            mUsages.addAll(usages);
        }

        @Override
        public void failure(RetrofitError error) {
            mEventBus.post(new ShowToastEvent("Leider ist beim Abfragen der Liste ein Fehler aufgetreten.", Toast.LENGTH_SHORT));
        }
    };

    private Callback<ToolUsage> mAddToolUsageCallback = new Callback<ToolUsage>() {
        @Override
        public void success(ToolUsage usage, Response response) {
            if(usage != null) {
                mOwnToolUsages.add(usage);
                mEventBus.post(new ReservationChangedEvent());
                Date dateNow = new Date();
                Date date = new Date(usage.getStartTime());

                /* more than 2 minutes in the future */
                if ((date.getTime() - dateNow.getTime()) > 1000 * 60 * 2) {
                    mEventBus.post(new NotificationEvent(Formatter.formatNotificationToolUsage(mCurrentTool, mCurrentProject), "Du bist in ca. einer Minute an der Reihe!", new Date(usage.getStartTime() - 1000 * 60 * 1), (int) usage.getId()));

                    /* more than 12 minutes in the future */
                    if((date.getTime() - dateNow.getTime()) > 1000 * 60 * 12) {
                        mEventBus.post(new NotificationEvent(Formatter.formatNotificationToolUsage(mCurrentTool, mCurrentProject), "Noch ca. 10 Minuten", new Date(usage.getStartTime() - 1000 * 60 * 10), (int) usage.getId()));
                    }
                }

                mEventBus.post(new NotificationEvent(Formatter.formatNotificationToolUsage(mCurrentTool, mCurrentProject), "Los geht's! Du bist an der Reihe.", new Date(usage.getStartTime()), (int) usage.getId()));

                NotificationEvent removeEvent = new NotificationEvent("", "", new Date(usage.getStartTime() + usage.getDuration() * 60 * 1000), (int) usage.getId());
                removeEvent.setRemove();

                mEventBus.post(removeEvent);
                mEventBus.post(new ShowToastEvent(Formatter.formatToastToolUsageOffset((int) (new Date(date.getTime() - dateNow.getTime()).getTime() / 1000 / 60)), Toast.LENGTH_SHORT));
            }
        }

        @Override
        public void failure(RetrofitError error) {
            mEventBus.post(new ShowToastEvent("Leider ist beim Hinzuf\u00FCgen ein Fehler aufgetreten.", Toast.LENGTH_SHORT));
        }
    };

    private Callback<Response> mRemoveToolUsageCallback = new Callback<Response>() {
        @Override
        public void success(Response usage, Response response) {
            if(usage != null) {
                mEventBus.post(new ReservationChangedEvent());
            }
        }

        @Override
        public void failure(RetrofitError error) {
            mEventBus.post(new ShowToastEvent("Leider ist beim L\u00F6schen ein Fehler aufgetreten. Es ist nur m\u00F6glich, eigene Reservierungen zu l\u00F6schen.", Toast.LENGTH_SHORT));
        }
    };

    public ToolUsageModel(ToolUsageApi toolUsageApi)
    {
        mUsages = new ObservableArrayList<>();
        mTools = new ArrayList<>();
        mOwnToolUsages = new ArrayList<>();
        mToolUsageApi = toolUsageApi;
        mUuidRandom = UUID.randomUUID().toString();
        mToolUsageApi.getEnabledTools(mFabToolsCallback);
    }

    public void setObservableArrayListListener(ObservableArrayList.Listener<ToolUsage> listener) {
        mUsages.setListener(listener);
    }


    public String getUuidRandom() {
        return mUuidRandom;
    }

    public List<FabTool> getEnabledFabTools()
    {
        mToolUsageApi.getEnabledTools(mFabToolsCallback);
        return mTools;
    }

    public ObservableArrayList<ToolUsage> getToolUsages(long toolId)
    {
        mToolId = toolId;
        mUsages.clear();
        mToolUsageApi.getUsageForTool(mToolId, mToolUsagesCallback);
        return mUsages;
    }

    public void addToolUsage(User user, FabTool tool, String project, long duration) {
        ToolUsage t = new ToolUsage();
        t.setTool(tool);
        if(user != null) {
            t.setUser(user.getUsername());
        }
        t.setProject(project);
        t.setDuration(duration);
        t.setToken(getUuidRandom());

        mCurrentTool = tool.getTitle();
        mCurrentProject = project;

        mToolUsageApi.addUsage(getUuidRandom(), tool.getId(), t, mAddToolUsageCallback);
    }

    public boolean isOwnUsage(ToolUsage usage) {
        for(ToolUsage t : mOwnToolUsages) {
            if(t.getId() == usage.getId() && t.toString().equals(usage.toString()))
                return true;
        }
        return false;
    }

    public void removeEntry(ToolUsage entry) {
        mToolUsageApi.removeUsage(getUuidRandom(), entry.getToolId(), entry.getId(), mRemoveToolUsageCallback);
        mUsages.remove(entry);
    }
}
