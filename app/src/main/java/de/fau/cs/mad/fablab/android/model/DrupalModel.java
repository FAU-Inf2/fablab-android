package de.fau.cs.mad.fablab.android.model;

import java.util.ArrayList;
import java.util.List;

import de.fau.cs.mad.fablab.rest.core.FabTool;
import de.fau.cs.mad.fablab.rest.myapi.DrupalApi;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class DrupalModel {

    private static List<FabTool> mTools;
    private static DrupalApi mDrupalApi;

    private static Callback<List<FabTool>> mFabToolsCallback = new Callback<List<FabTool>>() {
        @Override
        public void success(List<FabTool> tools, Response response) {
            mTools.addAll(tools);
        }

        @Override
        public void failure(RetrofitError error) {
        }
    };

    public DrupalModel(DrupalApi drupalApi)
    {
        mTools = new ArrayList();
        mDrupalApi = drupalApi;
        mDrupalApi.findAllTools(mFabToolsCallback);
    }

    public List<FabTool> getFabTools()
    {
        return mTools;
    }
}
