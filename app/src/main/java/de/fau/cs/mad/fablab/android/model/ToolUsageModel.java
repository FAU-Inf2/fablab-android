package de.fau.cs.mad.fablab.android.model;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.fau.cs.mad.fablab.android.model.events.ReservationChangedEvent;
import de.fau.cs.mad.fablab.android.model.events.ShowToastEvent;
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
    private List<ToolUsage> ownToolUsages;
    private ToolUsageApi mToolUsageApi;
    private long mToolId;
    private String uuidRandom;

    EventBus mEventBus = EventBus.getDefault();

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
                ownToolUsages.add(usage);
                mEventBus.post(new ReservationChangedEvent());
            }
        }

        @Override
        public void failure(RetrofitError error) {
            mEventBus.post(new ShowToastEvent("Leider ist beim Hinzufuegen ein Fehler aufgetreten.", Toast.LENGTH_SHORT));
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
            mEventBus.post(new ShowToastEvent("Leider ist beim Loeschen ein Fehler aufgetreten. Es ist nur moeglich, eigene Reservierungen zu loeschen.", Toast.LENGTH_SHORT));
        }
    };

    public ToolUsageModel(ToolUsageApi toolUsageApi)
    {
        mUsages = new ObservableArrayList<>();
        ownToolUsages = new ArrayList<>();
        mToolUsageApi = toolUsageApi;
        uuidRandom = UUID.randomUUID().toString();
    }

    public void setObservableArrayListListener(ObservableArrayList.Listener<ToolUsage> listener) {
        mUsages.setListener(listener);
    }

    public String getUuidRandom() {
        return uuidRandom;
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

        mToolUsageApi.addUsage(getUuidRandom(), tool.getId(), t, mAddToolUsageCallback);
    }

    public boolean isOwnUsage(ToolUsage usage) {
        for(ToolUsage t : ownToolUsages) {
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
