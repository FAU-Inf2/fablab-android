package de.fau.cs.mad.fablab.android.view.fragments.productmap;

import android.os.Bundle;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.model.ProductMapModel;
import de.fau.cs.mad.fablab.android.model.events.ProductMapStatusChangedEvent;
import de.greenrobot.event.EventBus;

public class ProductMapFragmentViewModel {
    static final String KEY_LOCATION = "key_location";

    @Inject
    ProductMapModel mModel;

    private String mLocation;

    private Listener mListener;
    private EventBus mEventBus = EventBus.getDefault();

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public void initialize(Bundle args) {
        //something like "fau fablab / werkstadt / ..."
        mLocation = args.getString(KEY_LOCATION);

        String url = getProductMapUrl();
        if (mListener != null && url != null) {
            mListener.onLoadMap(url);
        }
    }

    private String getProductMapUrl() {
        String productMapUrl = mModel.getProductMapUrl();
        if (productMapUrl != null) {
            productMapUrl += "?id=" + mLocation;
        }
        return productMapUrl;
    }

    public void pause() {
        mEventBus.unregister(this);
    }

    public void resume() {
        mEventBus.register(this);
    }

    public void onEvent(ProductMapStatusChangedEvent event) {
        if (mListener != null) {
            if (event.isAvailable()) {
                mListener.onLoadMap(getProductMapUrl());
            } else {
                mListener.onShowErrorMessage();
            }
        }
    }

    public interface Listener {
        void onLoadMap(String url);

        void onShowErrorMessage();
    }
}
