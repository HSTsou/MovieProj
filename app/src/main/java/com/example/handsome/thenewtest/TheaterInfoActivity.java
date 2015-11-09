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

import com.example.handsome.thenewtest.entity.TheaterTime;
import com.example.handsome.thenewtest.fragment.TheaterTimeFragment;
import com.example.handsome.thenewtest.helper.DatabaseHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

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

    Context c;
    String lat,lng, thId;
    ArrayList<String> mvTimeList = new ArrayList<>();
    Handler  handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theater_info);
        c = this;
        handler = new Handler();
        Bundle bundle = this.getIntent().getExtras();
        final String title = (String) bundle.get("thName");
        final String address = (String) bundle.get("address");
        lat = (String) bundle.get("lat");
        lng = (String) bundle.get("lng");
        thId = (String) bundle.get("thId");
        setMap(title, address);
        initInstances(title);
        initNavigation();
        setAppBarDragging(false);//FALSE : not interact with coordinator layout



    }

    void setMap(final  String title, final  String address ){

        new Thread(new Runnable() {

            @Override
            public void run() {

                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        GoogleMap map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
                        LatLng coordinate = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
                        //map.setMyLocationEnabled(true);
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 16));
                        map.getUiSettings().setZoomControlsEnabled(true);
                        map.addMarker(new MarkerOptions()
                                .title(title)
                                .snippet(address)
                                .position(coordinate))

                                .showInfoWindow();

                    }

                });

            }

        }).start();
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

        final ViewPager viewPager = (ViewPager) findViewById(R.id.info_viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.info_tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        rootLayout = (CoordinatorLayout) findViewById(R.id.rootLayout);

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

    private void initNavigation(){
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

    List<TheaterTime> setThTimeByDate(){
        List<TheaterTime> thTimeList = new  ArrayList<TheaterTime>();
        mvTimeList =  MainActivity.TH_TIME_LIST;
        String date;
        String  mvAndTimeStr;
        for(String thTime : mvTimeList){
            TheaterTime th = new TheaterTime();
            String[] thTimeStr = thTime.split("!");
            date = thTimeStr[0];
            mvAndTimeStr = thTimeStr[1];
            th.setThDate(date);
            th.setAllThTimeStr(mvAndTimeStr);
            thTimeList.add(th);
        }
        return thTimeList;
    }

    List<TheaterTime> setThTimeByDateFromDB(){
        List<TheaterTime> thTimeList = new  ArrayList<TheaterTime>();
        DatabaseHelper helper = new DatabaseHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();

        String thTimeInfoStr_outArray ="";
        String updateTime = "";
        Cursor c = null;
        try {
            c = helper.getTheaterTimeInfoById(db, thId);
            if (c.moveToFirst()){

                thTimeInfoStr_outArray = c.getString(1);
            }

        } finally {
            c.close();
            db.close();
        }

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        ArrayList<String> thTimeInfoStr_List = gson.fromJson(thTimeInfoStr_outArray, type);
        Log.i("hs", " thTimeInfoStr_outArray = "+thTimeInfoStr_outArray);
        Log.i("hs", " updateTime = "+updateTime);

        String date;
        String  mvAndTimeStr;
        for(String thTime : thTimeInfoStr_List){
            TheaterTime th = new TheaterTime();
            String[] thTimeStr = thTime.split("!");
            date = thTimeStr[0];
            mvAndTimeStr = thTimeStr[1];
            th.setThDate(date);
            th.setAllThTimeStr(mvAndTimeStr.substring(1));//remove the first "@"
            thTimeList.add(th);
        }
        return thTimeList;
    }

    private void setupViewPager(ViewPager viewPager) {
        //List<TheaterTime> thTimeList = setThTimeByDate();
        List<TheaterTime> thTimeList = setThTimeByDateFromDB();
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        for(TheaterTime thObj : thTimeList){
            adapter.addFrag(TheaterTimeFragment.createInstance(thObj.getAllThTimeStr()), thObj.getThDate());
        }
        viewPager.setAdapter(adapter);
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
