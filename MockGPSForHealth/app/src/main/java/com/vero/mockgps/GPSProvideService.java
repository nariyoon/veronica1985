package com.vero.mockgps;

import android.Manifest;
import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class GPSProvideService extends IntentService {
    private static final String ACTION_START = "com.vero.mockgps.action.START";
    private static final String ACTION_PUSH = "com.vero.mockgps.action.PUSH";
    private static final String ACTION_END = "com.vero.mockgps.action.END";
    private static final String EXTRA_CVS_FILE_NAME = "com.vero.mockgps.extra.cvs_file_name";
    private static final String TAG = GPSProvideService.class.getSimpleName();

    private MockLocationProvider mockLocationProvider;

    public GPSProvideService() {
        super("GPSProvideService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mockLocationProvider = new MockLocationProvider(this, LocationManager.GPS_PROVIDER);
    }

    public static void startActionStart(Context context) {
        Log.d(TAG, "startActionStart: ");
        Intent intent = new Intent(context, GPSProvideService.class);
        intent.setAction(ACTION_START);
        context.startService(intent);
    }

    public static void startActionEnd(Context context) {
        Log.d(TAG, "startActionEnd: ");
        Intent intent = new Intent(context, GPSProvideService.class);
        intent.setAction(ACTION_END);
        context.startService(intent);
    }

    public static void startActionPush(Context context, String cvsFileName) {
        Log.d(TAG, "startActionPush: ");
        Intent intent = new Intent(context, GPSProvideService.class);
        intent.setAction(ACTION_PUSH);
        intent.putExtra(EXTRA_CVS_FILE_NAME, cvsFileName);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_START.equals(action)) {
                handleActionStart();
            } else if (ACTION_PUSH.equals(action)) {
                String cvsFileName = intent.getStringExtra(EXTRA_CVS_FILE_NAME);
                handleActionPush(cvsFileName);
            } else if (ACTION_END.equals(action)) {
                handleActionEnd();
            }
        }
    }

    private void handleActionStart() {
        Log.d(TAG, "handleActionStart: ");
        try {
            if (!mockLocationProvider.register()) {
                showToast("no provider");
            }
        } catch (SecurityException e) {
            showToast("go to developer settings and add mock gps provider");
        }

    }

    private void handleActionPush(String cvsFileName) {
        showToast("push start");
        Log.d(TAG, "handleActionPush: " + cvsFileName);
        pushMockLocation(cvsFileName);
    }

    private void handleActionEnd() {
        showToast("push end");
        Log.d(TAG, "handleActionEnd: ");
        mockLocationProvider.unregister();
    }

    private void pushMockLocation(String cvsFileName) {

        Log.d(TAG, "pushMockLocation: ");

        try {
            List<String> data = new ArrayList<>();

            InputStream is = getAssets().open(cvsFileName);
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

    private void showToast(final String msg) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
