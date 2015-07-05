package de.fau.cs.mad.fablab.android.model;

import android.os.Handler;

import net.spaceapi.HackerSpace;
import net.spaceapi.State;

import de.fau.cs.mad.fablab.android.model.events.SpaceApiStateChangedEvent;
import de.fau.cs.mad.fablab.android.model.events.SpaceApiStatePushedEvent;
import de.fau.cs.mad.fablab.rest.myapi.SpaceApi;
import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SpaceApiModel {
    private final long REFRESH_TIME_MILLIS = 3 * 60 * 1000; // 3 minutes

    private SpaceApi mSpaceApi;
    private String mSpaceName;
    private EventBus mEventBus = EventBus.getDefault();
    private Handler mSpaceApiHandler = new Handler();
    private Runnable mSpaceApiRunner = new SpaceApiRunner();
    private boolean mRefreshRequested;

    private boolean mOpen;
    private long mTime;
    private String mMessage;

    private Callback<HackerSpace> mSpaceApiCallback = new Callback<HackerSpace>() {
        @Override
        public void success(HackerSpace hackerSpace, Response response) {
            updateState(hackerSpace.getState());

            mSpaceApiHandler.postDelayed(mSpaceApiRunner, REFRESH_TIME_MILLIS);
            mRefreshRequested = false;
        }

        @Override
        public void failure(RetrofitError error) {
            mSpaceApiHandler.postDelayed(mSpaceApiRunner, REFRESH_TIME_MILLIS);
            mRefreshRequested = false;
        }
    };

    public SpaceApiModel(SpaceApi spaceApi, String spaceName) {
        mSpaceApi = spaceApi;
        mSpaceName = spaceName;

        mEventBus.register(this);
        mRefreshRequested = false;
        refreshState();
    }

    private void updateState(State state) {
        if (state != null) {
            mOpen = state.getOpen();

            long currentTimeSeconds = System.currentTimeMillis() / 1000L;
            double minutesSinceLastChange = (currentTimeSeconds - state.getLastchange()) / 60;
            mTime = Double.valueOf(minutesSinceLastChange).longValue();

            mMessage = state.getMessage();

            mEventBus.post(new SpaceApiStateChangedEvent(mOpen, mTime, mMessage));
        }
    }

    public void refreshState() {
        if (!mRefreshRequested) {
            mRefreshRequested = true;
            mSpaceApiHandler.removeCallbacks(mSpaceApiRunner);
            mSpaceApi.getSpace(mSpaceName, mSpaceApiCallback);
        }
    }

    public boolean getOpen() {
        return mOpen;
    }

    public long getTime() {
        return mTime;
    }

    public String getMessage() {
        return mMessage;
    }

    private class SpaceApiRunner implements Runnable {

        @Override
        public void run() {
            mSpaceApi.getSpace(mSpaceName, mSpaceApiCallback);
        }
    }

    @SuppressWarnings("unused")
    public void onEvent(SpaceApiStatePushedEvent event) {
        updateState(event.getState());
    }
}
