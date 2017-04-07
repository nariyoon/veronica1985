package com.vero.mockgps;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    MockLocationProvider mockLocationProvider;
    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startMockLocation();
            }
        });

        mockLocationProvider = new MockLocationProvider(this, LocationManager.GPS_PROVIDER);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("LocationTEst", "onLocationChanged: " + location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        if (!mockLocationProvider.register()) {
            Toast.makeText(this, "no provider", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mockLocationProvider.unregister();
        locationManager.removeUpdates(locationListener);
    }

    private void startMockLocation() {

        Toast.makeText(this, "start", Toast.LENGTH_SHORT).show();

        try {

            List<String> data = new ArrayList<>();

            InputStream is = getAssets().open("mock_gps_data_01.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            String line;
            while ((line = reader.readLine()) != null) {
                data.add(line);
            }

            // convert to a simple array so we can pass it to the AsyncTask
            String[] locations = new String[data.size()];
            data.toArray(locations);

            mockLocationProvider.pushLocation(locations);

        } catch (IOException e) {
            Log.e("LocationTest", e.getMessage(), e);
            e.printStackTrace();
        }
    }
}
