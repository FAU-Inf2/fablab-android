package de.fau.cs.mad.fablab.android.model;

import java.util.List;

import de.fau.cs.mad.fablab.android.viewmodel.common.ObservableArrayList;
import de.fau.cs.mad.fablab.rest.core.ToolUsage;
import de.fau.cs.mad.fablab.rest.myapi.ToolUsageApi;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ToolUsageModel {

    private ObservableArrayList<ToolUsage> mUsages;
    private ToolUsageApi mToolUsageApi;
    private long mToolId;

    private Callback<List<ToolUsage>> mToolUsagesCallback = new Callback<List<ToolUsage>>() {
        @Override
        public void success(List<ToolUsage> usages, Response response) {
            mUsages.clear();
            mUsages.addAll(usages);
            // TODO Order by creationtime
            /*Collections.sort(mUsages, new Comparator<ToolUsage>() {
                @Override
                public int compare(ToolUsage lhs, ToolUsage rhs) {
                    Collator collator = Collator.getInstance(Locale.GERMAN);
                    collator.setStrength(Collator.SECONDARY);
                    return collator.compare(lhs.getCreationTime(), rhs.getCreationTime());
                }
            });*/
        }

        @Override
        public void failure(RetrofitError error) {
        }
    };

    public ToolUsageModel(ToolUsageApi toolUsageApi)
    {
        mUsages = new ObservableArrayList<>();
        mToolUsageApi = toolUsageApi;
    }

    public void setObservableArrayListListener(ObservableArrayList.Listener<ToolUsage> listener) {
        mUsages.setListener(listener);
    }

    public ObservableArrayList<ToolUsage> getToolUsages(long toolId)
    {
        mToolId = toolId;
        mUsages.clear();
        mToolUsageApi.getUsageForTool(mToolId, mToolUsagesCallback);
        return mUsages;
    }
}
