package de.fau.cs.mad.fablab.android.view.fragments.productmap;

import android.os.Bundle;

import javax.inject.Inject;

public class ProductMapFragmentViewModel {
    static final String KEY_LOCATION = "key_location";

    private ProductLocation mProductLocation;

    @Inject
    public ProductMapFragmentViewModel() {

    }

    public double getMainPositionX() {
        return mProductLocation.getMainPositionX();
    }

    public double getMainPositionY() {
        return mProductLocation.getMainPositionY();
    }

    public String getLocationName() {
        return mProductLocation.getLocationName();
    }

    public String getIdentificationCode() {
        return mProductLocation.getIdentificationCode();
    }

    public void initialize(Bundle args) {
        String location = args.getString(KEY_LOCATION);
        mProductLocation = LocationParser.getLocation(location);
    }
}
