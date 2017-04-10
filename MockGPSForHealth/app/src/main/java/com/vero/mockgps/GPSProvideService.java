package com.vero.mockgps;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.location.LocationManager;
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

    public static void startActionPush(Context context) {
        Log.d(TAG, "startActionPush: ");
        Intent intent = new Intent(context, GPSProvideService.class);
        intent.setAction(ACTION_PUSH);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_START.equals(action)) {
                handleActionStart();
            } else if (ACTION_PUSH.equals(action)) {
                handleActionPush();
            } else if (ACTION_END.equals(action)) {
                handleActionEnd();
            }
        }
    }

    private void handleActionStart() {
        Log.d(TAG, "handleActionStart: ");
        if (!mockLocationProvider.register()) {
            Toast.makeText(this, "no provider", Toast.LENGTH_SHORT).show();
            stopSelf();
        }
    }

    private void handleActionPush() {
        Toast.makeText(this, "push start", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "handleActionPush: ");
        pushMockLocation();
    }

    private void handleActionEnd() {
        Toast.makeText(this, "push end", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "handleActionEnd: ");
        mockLocationProvider.unregister();
    }

    private void pushMockLocation() {

        Log.d(TAG, "pushMockLocation: ");


        try {
            List<String> data = new ArrayList<>();

            InputStream is = getAssets().open("normal_straight.csv");
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
