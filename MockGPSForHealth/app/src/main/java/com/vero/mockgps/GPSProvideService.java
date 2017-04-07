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

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class GPSProvideService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_START = "com.vero.mockgps.action.START";
    private static final String ACTION_PUSH = "com.vero.mockgps.action.PUSH";
    private static final String ACTION_END = "com.vero.mockgps.action.END";

    private MockLocationProvider mockLocationProvider;

    public GPSProvideService() {
        super("GPSProvideService");
        mockLocationProvider = new MockLocationProvider(this, LocationManager.GPS_PROVIDER);
    }

    public static void startActionStart(Context context) {
        Intent intent = new Intent(context, GPSProvideService.class);
        intent.setAction(ACTION_START);
        context.startService(intent);
    }

    public static void startActionEnd(Context context) {
        Intent intent = new Intent(context, GPSProvideService.class);
        intent.setAction(ACTION_END);
        context.startService(intent);
    }

    public static void startActionPush(Context context) {
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
        if (!mockLocationProvider.register()) {
            Toast.makeText(this, "no provider", Toast.LENGTH_SHORT).show();
            stopSelf();
        }
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void handleActionPush() {
        pushMockLocation();
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void handleActionEnd() {
        mockLocationProvider.unregister();
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void pushMockLocation() {

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
