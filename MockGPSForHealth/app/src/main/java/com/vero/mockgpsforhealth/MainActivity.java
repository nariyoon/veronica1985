package com.vero.mockgpsforhealth;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    MockLocationProvider mockLocationProvider;
    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startMockLocation();
            }
        });

        mockLocationProvider = new MockLocationProvider(this, LocationSet.PROVIDER_NAME);
        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
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
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mockLocationProvider.register()) {
            Toast.makeText(this, "no provider", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mockLocationProvider.unregister();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationManager.removeUpdates(locationListener);
    }

    private void startMockLocation() {

        Toast.makeText(this, "start", Toast.LENGTH_SHORT).show();

        //        mockLocationProvider.pushLocation();
        new AsyncTask<String, Integer, String>() {

            @Override
            protected String doInBackground(String... params) {
                mockLocationProvider.pushLocation();
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                Toast.makeText(MainActivity.this, "completed", Toast.LENGTH_SHORT).show();
                super.onPostExecute(s);
            }
        }.execute("");

    }
}