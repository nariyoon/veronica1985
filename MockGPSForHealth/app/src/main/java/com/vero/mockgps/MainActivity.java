package com.vero.mockgps;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("MainActivity", "no permission: ");
            Toast.makeText(this, "no permission", Toast.LENGTH_SHORT).show();
            finish();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView listView = (ListView) findViewById(R.id.location_listview);
        final LocationAdapter adapter = new LocationAdapter();
        adapter.add("slowly_walk.csv", "13min, 1km", R.drawable.slowly_walk);
        adapter.add("HanRiverRun.csv", "1h, 10km", R.drawable.slowly_walk);
        adapter.add("YongsanToYeoido.csv", "1h, 15km", R.drawable.slowly_walk);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GPSProvideService.startActionPush(MainActivity.this, adapter.getFileName(position));
            }
        });

        GPSProvideService.startActionStart(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GPSProvideService.startActionEnd(this);

    }

    class LocationAdapter extends BaseAdapter {
        ArrayList<String> cvs_title_list;
        ArrayList<String> cvs_content_list;
        ArrayList<Integer> map_img_list;

        LocationAdapter() {
            cvs_title_list = new ArrayList<>();
            cvs_content_list = new ArrayList<>();
            map_img_list = new ArrayList<>();
        }

        public void add(String cvsTitle, String cvsContent, int imgRes) {
            cvs_title_list.add(cvsTitle);
            cvs_content_list.add(cvsContent);
            map_img_list.add(imgRes);
        }

        @Override
        public int getCount() {
            return cvs_title_list.size();
        }

        @Override
        public String getItem(int position) {
            return getFileName(position);
        }

        public String getFileName(int position) {
            return cvs_title_list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View currentView = convertView;
            Context context = parent.getContext();
            if (currentView == null) {
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                currentView = inflater.inflate(R.layout.list_item, parent, false);
            }
            ((TextView) currentView.findViewById(R.id.cvs_title))
                    .setText(cvs_title_list.get(position));
            ((TextView) currentView.findViewById(R.id.cvs_content))
                    .setText(cvs_content_list.get(position));
            ((ImageView) currentView.findViewById(R.id.map_img))
                    .setImageResource(map_img_list.get(position));

            return currentView;
        }
    }
}
