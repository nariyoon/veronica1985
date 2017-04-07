package com.vero.mockgpsforhealth;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.provider.Settings;
import android.util.Log;

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
        this.locationManager = (LocationManager) context.getSystemService(
                Context.LOCATION_SERVICE);
    }

    public boolean register() {
        Log.d("LocationTEst", "register: ");

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);

        if (locationManager.getProvider(providerName) == null) {
            return false;
        }

        locationManager.addTestProvider(providerName, false, false, false, false, true,
                false, true, Criteria.POWER_HIGH,
                Criteria.ACCURACY_HIGH);

        return true;
    }

    public void pushLocation() {
        Location[] locations = LocationSet.list1;

        for (Location location : locations) {
            locationManager.setTestProviderEnabled(providerName, true);
            locationManager.setTestProviderStatus(providerName, LocationProvider.AVAILABLE, null,
                    location.getTime());
            locationManager.setTestProviderLocation(providerName, location);
        }
    }

    public void unregister() {

        Log.d("LocationTEst", "unregister: ");
        if (locationManager.getProvider(providerName) != null) {
            clearLocation();
            locationManager.removeTestProvider(providerName);
        }
    }

    public void clearLocation() {
        locationManager.clearTestProviderLocation(providerName);
    }

}
