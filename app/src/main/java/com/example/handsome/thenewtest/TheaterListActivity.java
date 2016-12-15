package com.example.handsome.thenewtest;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.handsome.thenewtest.adapter.TheaterListAdapter;
import com.example.handsome.thenewtest.helper.DatabaseHelper;
import com.example.handsome.thenewtest.helper.GPSHelper;
import com.example.handsome.thenewtest.helper.ItemClickSupport;
import com.example.handsome.thenewtest.helper.SharedPreferencesHelper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.MapsInitializer;
import com.google.api.services.youtube.model.GeoPoint;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TheaterListActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private String area;
    private Location location;
    private GeoPoint geoLocation;
    private DatabaseHelper dbHelper;
    private Handler handler;
    private Context context;
    private final static String TAG = "TEST";
    private ProgressDialog progressDialog;
    private List<Map<String, String>> data;
    private SharedPreferencesHelper prefHelper;


    String areaId, areaName;

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;

    private Location mLastLocation;
    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;
    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates = false;
    private LocationRequest mLocationRequest;
    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters
    public static RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //inst = getResources().getString(R.string.msg_inst_theater_list);
        //AppHelper.showInstDialog(this, inst);
        dbHelper = new DatabaseHelper(this);
        handler = new Handler();
        context = this;
        setContentView(R.layout.activity_th_list);


        MapsInitializer.initialize(this);


        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        initNavigation();
        initToolbar();

        Bundle bundle = this.getIntent().getExtras();
        areaId = (String) bundle.get("areaId");
        areaName = (String) bundle.get("areaName");


        if (checkPlayServices()) {
            buildGoogleApiClient();
            createLocationRequest();
            //displayLocation();
        }
        //togglePeriodicLocationUpdates();

    }


    private void initNavigation() {
        drawerLayout = (DrawerLayout) findViewById(R.id.thlist_drawerLayout);
        drawerToggle = new ActionBarDrawerToggle(TheaterListActivity.this, drawerLayout, R.string.app_name, R.string.app_name);
        drawerLayout.setDrawerListener(drawerToggle);
        drawerLayout.setFitsSystemWindows(true);


        NavigationView navigation;
        navigation = (NavigationView) findViewById(R.id.navigation);
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                Intent i = new Intent();
                switch (id) {
                    case R.id.navItem1:
                        i.setClass(context, MainActivity.class);
                        finish();
                        startActivity(i);
                        break;
                    case R.id.movie:
                        i.setClass(context, MovieListActivity.class);
                        finish();
                        startActivity(i);
                        break;
                    case R.id.theater:
                        i.setClass(context, TheaterAreaActivity.class);
                        finish();
                        startActivity(i);
                        break;
                }
                return false;
            }
        });

    }

    private void initToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //setTitle(getString(R.string.app_name));
        //mToolbar.setTitleTextColor(ContextCompat.getColor(this, R.));
    }


    /**
     * Method to verify google play services on the device
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        R.string.beautiful_error, Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Creating google api client object
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }


    /**
     * Creating location request object
     */
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    /**
     * Method to display the location on UI
     */
    private void displayLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            mLastLocation = LocationServices.FusedLocationApi
                    .getLastLocation(mGoogleApiClient);
        }


        if (mLastLocation != null) {
            double latitude = mLastLocation.getLatitude();
            double longitude = mLastLocation.getLongitude();

            Log.i("hs", latitude + ", " + longitude);

            refresh();
        } else {
            Log.i("hs", "(Couldn't get the location. Make sure location is enabled on the device)");
            refresh();

        }
    }

    /**
     * Method to toggle periodic location updates
     */
    private void togglePeriodicLocationUpdates() {
        if (!mRequestingLocationUpdates) {
            // Changing the button text

            Log.i("hs", "btn_stop_location_updates");
            mRequestingLocationUpdates = true;

            // Starting the location updates
            startLocationUpdates();

            Log.d(TAG, "Periodic location updates started!");

        } else {
            // Changing the button text

            Log.i("hs", "btn_start_location_updates");
            mRequestingLocationUpdates = false;

            // Stopping the location updates
            stopLocationUpdates();

            Log.d(TAG, "Periodic location updates stopped!");
        }
    }

    /**
     * Starting the location updates
     */
    protected void startLocationUpdates() {

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);

    }

    /**
     * Stopping location updates
     */
    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    /**
     * Google api callback methods
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
    }

    @Override
    public void onConnected(Bundle arg0) {

        // Once connected with google api, get the location
        displayLocation();

        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        // Assign the new location
        mLastLocation = location;

        Toast.makeText(getApplicationContext(), "Location changed!",
                Toast.LENGTH_SHORT).show();

        // Displaying the new location on UI
        displayLocation();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        checkPlayServices();

        // Resuming the periodic location updates
        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // stopLocationUpdates();
    }

    private void refresh() {
        if (mLastLocation != null) {

            try {
                String add = GPSHelper.getAddress(mLastLocation);
                Log.i("hs", "adress" + add);
                if (add != null)
                    Toast.makeText(
                            context,
                            add, Toast.LENGTH_LONG).show();

            } catch (Exception e) {
                e.printStackTrace();
            }
            setBestCase();

        } else {
            Log.i(TAG, "Cannot get location...");
            Toast.makeText(getApplicationContext(),
                    getResources().getString(R.string.open_google_gps),
                    Toast.LENGTH_LONG).show();
            load();
            showContent();
        }

    }


    private void setBestCase() {
        final Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    load();
                    getDistances();

                } finally {
                    progressDialog.dismiss();
                }
            }
        };

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(
                R.string.cal_distance_ing));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        /*
		 * progressDialog.setButton("隱藏", new DialogInterface.OnClickListener()
		 * {
		 *
		 * @Override public void onClick(DialogInterface dialog, int which) {
		 * thread.interrupt(); progressDialog.dismiss(); }
		 *
		 * });
		 */
        progressDialog.setCancelable(false);
        progressDialog.show();
        thread.start();
    }

    private void load() {


        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            data = loadTheaters(db);
        } finally {
            db.close();
        }
    }

    private void showContent() {

        // load();
        handler.post(new Runnable() {

            @Override
            public void run() {
                TheaterListAdapter adapter = new TheaterListAdapter(context, data);
                mRecyclerView.setAdapter(adapter);
                //list.setAdapter(adapter);
                //setListener(list);
                //setContentView(list);
            }

        });
        ItemClickSupport.addTo(TheaterListActivity.mRecyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Log.i("hs", "position = " + position);
                // Log.i("hs", "item = " + allData.get(position));

                String thId = data.get(position).get(DBConstants.THEATER.ID);
                Intent i = new Intent();
                i.setClass(context, TheaterInfoActivity.class);

                i.putExtra("thId", thId);

                startActivity(i);
            }
        });
    }


    private List<Map<String, String>> loadTheaters(SQLiteDatabase db) {
        List<Map<String, String>> data = new ArrayList<>();
        Cursor c = dbHelper.getTheaters(db, areaId);
        try {
            while (c.moveToNext()) {
                Map<String, String> item = new HashMap<>();
                String[] col = DatabaseHelper.COL_THEATER_LIST;
                for (int i = 0; i < col.length; i++) {
                    String key = c.getColumnName(i);
                    String value = c.getString(i);
                    item.put(key, value);
                }
                data.add(item);
            }
        } finally {
            c.close();
        }
        return data;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void getDistances() {
        boolean flag = false;
        int count = 0;
        int dataSize = data.size();

        for (Map<String, String> theater : data) {

            double endLat = Double.parseDouble(theater
                    .get(DBConstants.THEATER.LAT));
            double endLng = Double.parseDouble(theater
                    .get(DBConstants.THEATER.LNG));

            float[] distanceResults = new float[2];
            try {

                if (mLastLocation != null) {
                    double startLatitude = mLastLocation.getLatitude();
                    double startLongitude = mLastLocation.getLongitude();

                    Location.distanceBetween(startLatitude, startLongitude, endLat, endLng, distanceResults);
                    // Log.i("hs", "distanceResults = " + distanceResults[0]);

                } else {
                    Log.i("hs", "no location!!");
                }

                NumberFormat nf = NumberFormat.getInstance();
                nf.setMaximumFractionDigits(1);
                theater.put("distance", nf.format(distanceResults[0] / 1000) + "");
            } catch (Exception e) {
                Log.e("hs", e.toString());

                count++;
                if (count == (dataSize - 1)) {
                    flag = true;
                    break;
                }

            }
            //   data.add(theater);
        }

        if (flag) {
            showContent();
            handler.post(new Runnable() {

                @Override
                public void run() {
                    Toast.makeText(
                            getApplicationContext(),
                            getResources()
                                    .getString(R.string.plz_open_net_gps),
                            Toast.LENGTH_LONG).show();
                }
            });
            return;
        }

        // sort
        Collections.sort(data, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                Map<String, String> obj1 = (Map<String, String>) o1;
                Map<String, String> obj2 = (Map<String, String>) o2;

                int result = 0;
                double A = Double.parseDouble(obj1.get("distance")) * 100;//why it need to be multiplied by 100 for working?
                double B = Double.parseDouble(obj2.get("distance")) * 100;
                // small to big
                result = (int) (A - B);

                return result;

            }
        });
        showContent();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        try {
            location = GPSHelper.getLocation(this);
            //geoLocation = GPSHelper.getGeoByLocation(location);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        Log.i(TAG, "Cannot get location...");
        showContent();

    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //navigation home button.
        if (drawerToggle.onOptionsItemSelected(item))
            return true;

        int id = item.getItemId();

        if (id == R.id.menu_refresh) {
            refresh();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
