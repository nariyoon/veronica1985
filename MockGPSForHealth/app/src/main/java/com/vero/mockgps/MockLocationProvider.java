package com.vero.mockgps;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.SystemClock;
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

    public boolean register() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);

        if (locationManager.getProvider(providerName) == null) {
            return false;
        }
        Log.d("LocationTEst", "register: ");

        locationManager.addTestProvider(providerName, false, false, false, false, true,
                false, true, Criteria.POWER_HIGH,
                Criteria.ACCURACY_HIGH);

        return true;
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

    public void pushLocation(String[] locations) {
        new AsyncTask<String, Integer, Void>() {
            @Override
            protected Void doInBackground(String... data) {
                for (String str : data) {

                    double latitude;
                    double longitude;
                    float speed;

                    try {
                        String[] parts = str.split(",");
                        latitude = Double.valueOf(parts[0]);
                        longitude = Double.valueOf(parts[1]);
                        speed = Float.valueOf(parts[2]);

                    } catch (Exception e) {
                        continue;
                    }

                    Location location = new Location(providerName);
                    location.setLatitude(latitude);
                    location.setLongitude(longitude);
                    location.setAltitude(0);
                    location.setSpeed(speed);
                    location.setAccuracy(50);
                    location.setTime(System.currentTimeMillis());
                    location.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());

                    Log.d("LocationTEst", "convertLocation: " + location);
                    //                    https://android.googlesource.com/platform/frameworks/base/+/refs/heads/master/location/java/android/location/Location.java

                    if (locationManager.isProviderEnabled(providerName)) {
                        locationManager.setTestProviderEnabled(providerName, true);
                        locationManager.setTestProviderStatus(providerName,
                                LocationProvider.AVAILABLE, null, System.currentTimeMillis());
                        locationManager.setTestProviderLocation(providerName, location);
                    }
                    try {
                        Thread.sleep(900);
                        if (Thread.currentThread().isInterrupted())
                            throw new InterruptedException("");
                    } catch (InterruptedException e) {
                        break;
                    }

                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                GPSProvideService.startActionEnd(context);
            }
        }.execute(locations);
    }

}
