package com.nari.mockgps;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
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
        this.locationManager = (LocationManager)context.getSystemService(
                Context.LOCATION_SERVICE);
    }

    public void register() {
        Log.d("LocationTEst", "register: ");
        locationManager.addTestProvider(providerName, false, false, false, false, true,
                false, true, 0, android.location.Criteria.ACCURACY_FINE);

        if(locationManager.getProvider(providerName) != null){
            locationManager.removeTestProvider(providerName);
        }
    }

    public void pushLocation() {
        Location[] locations = {LocationSet.list1[0], LocationSet.list1[1]};
        for (Location location : locations) {

            Log.d("LocationTEst", "pushLocation: " + location);

            locationManager.setTestProviderLocation(providerName, location);
            locationManager.setTestProviderEnabled(providerName, true);
            locationManager.setTestProviderStatus(providerName, LocationProvider.AVAILABLE, null,
                    location.getTime());
        }
    }

    public void unregister() {

        Log.d("LocationTEst", "register: ");
        locationManager.removeTestProvider(providerName);
    }
}
