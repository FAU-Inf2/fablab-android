package de.fau.cs.mad.fablab.android.model;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.viewmodel.common.ObservableArrayList;
import de.fau.cs.mad.fablab.rest.core.ICal;
import de.fau.cs.mad.fablab.rest.myapi.ICalApi;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/***
 * Handles the connection to the rest server to receive ICals.
 * Stores the results and fires events on success
 */
public class ICalModel {
    private static final int ELEMENT_COUNT = 10;

    private ObservableArrayList<ICal> mICals;
    private ICalApi mICalApi;
    private boolean mICalsRequested;
    private RuntimeExceptionDao<ICal, Long> mICalDao;
    private long mTimeStampLastUpdate;
    private String mDateLastDisplayedICalsString;
    private boolean mEndReached;

    private Callback<List<ICal>> mICalApiCallback = new Callback<List<ICal>>() {
        @Override
        public void success(List<ICal> iCals, Response response) {
            mICals.addAll(iCals);
            mICalsRequested = false;

            for(ICal i : iCals)
            {
                createIfNotExists(mICalDao, i);
            }
            mDateLastDisplayedICalsString = mICals.get(mICals.size()-1).getStart();

        }

        @Override
        public void failure(RetrofitError error) {
            mICalsRequested = false;
            if(error.getMessage().equals("404 Not Found"))
            {
                mEndReached = true;
            }
        }
    };

    private Callback<Long> mICalApiCallbackUpdate = new Callback<Long>() {
        @Override
        public void success(Long lastUpdate, Response response) {
            if(lastUpdate > mTimeStampLastUpdate)
            {
                mEndReached = false;
                mTimeStampLastUpdate = lastUpdate;
                clearDao(mICalDao);
                mICals.clear();
                fetchNextICals();
            }
        }

        @Override
        public void failure(RetrofitError error) {
            mICalsRequested = false;
        }
    };

    @Inject
    public ICalModel(ICalApi api, RuntimeExceptionDao<ICal, Long> iCalDao){
        mICalApi = api;
        mICalDao = iCalDao;
        mICals = new ObservableArrayList<>();
        mICalsRequested = false;
        mTimeStampLastUpdate = 0;
        mEndReached = false;
        mDateLastDisplayedICalsString = "";
        fetchNextICals();
    }

    public void fetchNextICals() {
        //check whether to get news from database or server
        if (!mICalsRequested && mICals.size() + ELEMENT_COUNT > mICalDao.countOf() && !mEndReached)
        {
            mICalsRequested = true;
            mICalApi.find(mICals.size(), ELEMENT_COUNT, mICalApiCallback);
        }

        else if (!mICalsRequested && !mEndReached)
        {
            mICalsRequested = true;
            List<ICal> fetchedICals = new ArrayList<>();
            //get next Element_count elements from database
            QueryBuilder<ICal, Long> queryBuilder = mICalDao.queryBuilder();
            //sort elements in ascending order according to startdate and only return
            //ELEMENT_COUNT iCals
            queryBuilder.orderBy("start", true).limit(ELEMENT_COUNT);
            try {
                queryBuilder.where().gt("start", mDateLastDisplayedICalsString);
                fetchedICals = mICalDao.query(queryBuilder.prepare());
                mICals.addAll(fetchedICals);
                mDateLastDisplayedICalsString = mICals.get(mICals.size()-1).getStart();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            mICalsRequested = false;
        }

    }


    public ObservableArrayList<ICal> getICalsList() {
        return mICals;
    }

    public void iCalModelUpdate()
    {
        mICalApi.lastUpdate(mICalApiCallbackUpdate);
        deleteOldICals(mICalDao);
    }

    private void createIfNotExists(RuntimeExceptionDao<ICal, Long> iCalDao, ICal iCal)
    {
        List<ICal> retrievedICals = new ArrayList<>();
        QueryBuilder<ICal, Long> queryBuilder = iCalDao.queryBuilder();
        try {
            queryBuilder.where().eq("summery", iCal.getSummery()).and().eq("start", iCal.getStart())
            .and().eq("end", iCal.getEnd());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(retrievedICals.size() == 0)
        {
            iCalDao.create(iCal);
        }
    }

    private void deleteOldICals(RuntimeExceptionDao<ICal, Long> iCalDao)
    {
        DeleteBuilder<ICal, Long> deleteBuilder = iCalDao.deleteBuilder();
        try {
            deleteBuilder.where().lt("end", System.currentTimeMillis());
            iCalDao.delete(deleteBuilder.prepare());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void clearDao(RuntimeExceptionDao<ICal, Long> iCalDao)
    {
        List<ICal> retrievedICals = iCalDao.queryForAll();
        for(ICal i : retrievedICals)
        {
            iCalDao.delete(i);
        }
    }
}
