package com.example.handsome.thenewtest;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.handsome.thenewtest.entity.TheaterTime;
import com.example.handsome.thenewtest.fragment.TheaterTimeFragment;
import com.example.handsome.thenewtest.fragment.TheaterWebFragment;
import com.example.handsome.thenewtest.helper.DatabaseHelper;
import com.example.handsome.thenewtest.helper.JSONHelper;
import com.example.handsome.thenewtest.util.AppController;
import com.example.handsome.thenewtest.util.RegexUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.handsome.thenewtest.R.id.map;

/**
 * Created by handsome on 2015/10/28.
 */
public class TheaterInfoActivity extends AppCompatActivity {


    CoordinatorLayout rootLayout;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    Toolbar toolbar;
    TabLayout tabLayout;
    CollapsingToolbarLayout collapsingToolbarLayout;
    DatabaseHelper dbHelper;
    Context c;
    String lat, lng, thId, title, address;
    ArrayList<String> mvTimeList = new ArrayList<>();
    Handler handler;
    static final String TH_TIME_URL = "https://movingmoviezero.appspot.com/thInfo?id=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theater_info);
        c = this;
        handler = new Handler();
        dbHelper = new DatabaseHelper(this);

        Bundle bundle = this.getIntent().getExtras();
        thId = (String) bundle.get("thId");
        Log.i("hs", "thId = " + thId);

        String[] thInfo = setThInfoFromDB(thId);
        title = thInfo[1];
        address = thInfo[2];
        lat = thInfo[4];
        lng = thInfo[5];

        getTheaterTimeByJson(thId);

        setMap(title, address);
        initInstances(title);
        initNavigation();
        setAppBarDragging(false);//FALSE : not interact with coordinator layout

    }

    void parseThTimeJsonAndStore(JSONArray response) {
        Log.i("hs", "parseMvInfoJsonAndStore");
        DatabaseHelper helper = new DatabaseHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();

        for (int i = 0; i < response.length(); i++) {
            try {
                JSONObject obj = response.getJSONObject(i);

                String thId = obj.getString("thId");

                Gson gson = new Gson();
                String inputString = gson.toJson(JSONHelper.getStringListFromJsonArray(obj.getJSONArray("thMvShowtimeList")));
                // Log.i("hs", "thId" + thId );
                helper.insertTheaterTimeInfo(db, thId.substring(10), inputString);


            } catch (JSONException e) {
                e.printStackTrace();
                Log.i("hs", " getAllMvInfo JSONException" + e);

            } finally {
                db.close();
            }

        }
    }

    void getTheaterTimeByJson(String thId) {
        Log.i("hs", "getTheaterTimeByJson");
        JsonArrayRequest req = new JsonArrayRequest(TH_TIME_URL + thId,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Log.i(TAG, response.toString());
                        parseThTimeJsonAndStore(response);
                        setViewPager();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // VolleyLog.d(TAG, "Error: " + error.getMessage());
                Log.i("hs", "Error = " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        R.string.beautiful_error, Toast.LENGTH_SHORT).show();

            }

        });

        AppController.getInstance().addToRequestQueue(req);
    }
  /*  @Override
    public void onMapReady(GoogleMap googleMap) {

        LatLng coordinate = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 16));
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.addMarker(new MarkerOptions()
                .title(title)
                .snippet(address)
                .position(coordinate))
                .showInfoWindow();
    }*/

    void setMap(final String title, final String address) {
        ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map)).getMapAsync(new OnMapReadyCallback() {
            //如果使用則XML需改成 com.google.android.gms.maps.SupportMapFragment ，而不是.MapFragment 20161216
            @Override
            public void onMapReady(GoogleMap googleMap) {
                if ("".equals(lat) || "".equals(lng)||lat==null||lng==null) {
                    lat = "0";
                    lng = "0";
                }
                LatLng coordinate = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 15));
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.addMarker(new MarkerOptions()
                        .title(title)
                        .snippet(address)
                        .position(coordinate))
                        .showInfoWindow();


            }
        });
    }


    private void initInstances(String title) {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setLogo(R.drawable.mm_logo);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        collapsingToolbarLayout.setTitle(title);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedappbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedappbar);

        // rootLayout = (CoordinatorLayout) findViewById(R.id.rootLayout);

    }


    private void setAppBarDragging(final boolean newValue) {
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        CoordinatorLayout.LayoutParams params =
                (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
        AppBarLayout.Behavior behavior = new AppBarLayout.Behavior();
        behavior.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
            @Override
            public boolean canDrag(AppBarLayout appBarLayout) {
                return newValue;
            }
        });
        params.setBehavior(behavior);
    }

    private void initNavigation() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerToggle = new ActionBarDrawerToggle(TheaterInfoActivity.this, drawerLayout, R.string.app_name, R.string.app_name);

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
                        i.setClass(c, MainActivity.class);
                        finish();
                        startActivity(i);
                        break;
                    case R.id.movie:
                        i.setClass(c, MovieListActivity.class);
                        finish();
                        startActivity(i);
                        break;
                    case R.id.theater:
                        i.setClass(c, TheaterAreaActivity.class);
                        finish();
                        startActivity(i);
                        break;
                }
                return false;
            }
        });

    }

    String[] setThInfoFromDB(String thId) {
        Log.i("hs", "setThInfoFromDB");
        DatabaseHelper helper = new DatabaseHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();

        String[] thInfo = new String[6];

        Cursor c = null;
        try {
            c = helper.getTheaterInfoById(db, thId);

            if (c.moveToFirst()) {
                thInfo[0] = c.getString(0);//thId
                thInfo[1] = c.getString(1);//thName
                thInfo[2] = c.getString(2);//thAddress
                thInfo[3] = c.getString(3);//phone
                thInfo[4] = c.getString(4);//lat
                thInfo[5] = c.getString(5);//lng
                //Log.i("hs", " lat, lng = "+ c.getString(4) +", " +c.getString(5));
            }

        } finally {
            c.close();
            db.close();
        }

        return thInfo;
    }

    List<TheaterTime> setThTimeByDateFromDB() {
        List<TheaterTime> thTimeList = new ArrayList<TheaterTime>();
        DatabaseHelper helper = new DatabaseHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();

        String thTimeInfoStr_outArray = "";

        Cursor c = null;
        try {
            c = helper.getTheaterTimeInfoById(db, thId);
            if (c.moveToFirst()) {
                thTimeInfoStr_outArray = c.getString(1);

            }

        } finally {
            c.close();
            db.close();
        }

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        ArrayList<String> thTimeInfoStr_List;
        if (thTimeInfoStr_outArray.length() != 0 || !thTimeInfoStr_outArray.equalsIgnoreCase("")) {
            thTimeInfoStr_List = gson.fromJson(thTimeInfoStr_outArray, type);
            Log.i("hs", " thTimeInfoStr_outArray = " + thTimeInfoStr_outArray);


            String date;
            String mvAndTimeStr;
            try {
                for (String thTime : thTimeInfoStr_List) {
                    JSONObject obj = new JSONObject(thTime);
                    thTime = obj.getString("value");
                    TheaterTime th = new TheaterTime();
                    String[] thTimeStr = thTime.split("!");
                    date = thTimeStr[0];

                    if (thTimeStr.length > 1) {//if no exceed 1 means : ["{\"value\":\"\\/showtime\\/t02d20\\/a02\\/!\"}"] no timeStr
                        mvAndTimeStr = thTimeStr[1];
                        th.setAllThTimeStr(mvAndTimeStr.substring(1));//remove the first "@"
                        th.setThDate(date);
                        thTimeList.add(th);
                    } else {//no timeStr, so directly return null, and then ....
                        return null;
                    }

                }

            } catch (JSONException e) {

            }

            return thTimeList;
        }


        return null;

    }

    private void setViewPager() {
        final ViewPager viewPager = (ViewPager) findViewById(R.id.info_viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.info_tabLayout);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {

        List<TheaterTime> thTimeList = setThTimeByDateFromDB();
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        RegexUtil rex;
        if (thTimeList != null) {
            for (TheaterTime thObj : thTimeList) {

                rex = new RegexUtil("[0-9]{8}");// find time "00000000" pattern

                if (rex.getIndexOfStr(thObj.getThDate()) != -1) {//if found it, like "/showtime/t07703/a07/20151119/"
                    String s = thObj.getThDate();
                    s = s.substring(s.length() - 9, s.length() - 1);
                    adapter.addFrag(TheaterTimeFragment.createInstance(thObj.getAllThTimeStr()), s + " " + getDayOfWeek(s));//20151119 (四)


                } else {////"/showtime/t07703/a07/"
                    adapter.addFrag(TheaterTimeFragment.createInstance(thObj.getAllThTimeStr()), "今天");
                }

            }
            viewPager.setAdapter(adapter);
        } else {
            adapter.addFrag(TheaterWebFragment.createInstance(thId), "網站");
            viewPager.setAdapter(adapter);
        }


    }

    String getDayOfWeek(String dateNum) {

        String format = "yyyyMMdd";

        SimpleDateFormat df = new SimpleDateFormat(format);
        Date date = null;

        try {
            date = df.parse(dateNum);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        String[] weeks = {"日", "一", "二", "三", "四", "五", "六"};

        cal.setTime(date);
        int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (week_index < 0) {
            week_index = 0;
        }

        return weeks[week_index];
    }


    static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
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
        getMenuInflater().inflate(R.menu.theater_info_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item))
            return true;

        int id = item.getItemId();

        if (id == R.id.navigation) {
            Intent navigation = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + lat + "," + lng));
            startActivity(navigation);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
