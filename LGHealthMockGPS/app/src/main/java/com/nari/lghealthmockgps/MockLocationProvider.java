package com.nari.lghealthmockgps;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;

/**
 * Created by nari.yoon on 2017-04-06.
 */

public class MockLocationProvider {

    Context context;
    String providerName;
    LocationManager locationManager;

    public MockLocationProvider(Context context, String provider) {
        this.context = context;
        this.providerName = provider;
        this.locationManager = (LocationManager)context.getSystemService(
                Context.LOCATION_SERVICE);
    }

    public void register() {

        locationManager.addTestProvider(providerName, false, false, false, false, true,
                false, true, 0, 5);
    }

    public void pushLocation() {
        for (Location location : LocationSet.list1) {
            locationManager.setTestProviderEnabled(providerName, true);
            locationManager.setTestProviderStatus(providerName, LocationProvider.AVAILABLE, null,
                    location.getTime());
            locationManager.setTestProviderLocation(providerName, location);
        }
    }

    public void unregister() {
        locationManager.removeTestProvider(providerName);
    }
}
