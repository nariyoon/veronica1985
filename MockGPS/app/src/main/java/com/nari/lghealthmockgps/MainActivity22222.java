package com.nari.lghealthmockgps;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity22222 extends AppCompatActivity {

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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mockLocationProvider = new MockLocationProvider(this, LocationManager.GPS_PROVIDER);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("LocationTEst", "onLocationChanged: " + location);
                Toast.makeText(MainActivity22222.this, location.getLatitude() + ", " + location.getLongitude(), Toast.LENGTH_SHORT).show();
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
        mockLocationProvider.register();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_start) {
            startMockLocation();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void startMockLocation() {

        mockLocationProvider.pushLocation();
//        new AsyncTask<String, Integer, String>() {
//
//            @Override
//            protected String doInBackground(String... params) {
//                mockLocationProvider.pushLocation();
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(String s) {
//                Toast.makeText(MainActivity22222.this, "Completed", Toast.LENGTH_SHORT).show();
//                super.onPostExecute(s);
//            }
//        }.execute("");

    }
}