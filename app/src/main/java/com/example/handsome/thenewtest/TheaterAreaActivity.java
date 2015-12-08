package com.example.handsome.thenewtest;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.handsome.thenewtest.adapter.TheaterAreaGridAdapter;
import com.example.handsome.thenewtest.entity.AreaObject;
import com.example.handsome.thenewtest.util.InnerGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by handsome on 2015/10/24.
 */
public class TheaterAreaActivity extends AppCompatActivity{

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    Context context;
    NestedScrollView nestedScrollview;
    InnerGridView gridview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theater_area);
        context = this;

        nestedScrollview = (NestedScrollView) findViewById(R.id.nested_view);
        gridview = (InnerGridView) findViewById(R.id.theater_gridview);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            gridview.setNestedScrollingEnabled(true);
        }

        initNavigation();
        initToolbar();

        List<AreaObject> allItems = getAllAreaObject();
        TheaterAreaGridAdapter customAdapter = new TheaterAreaGridAdapter(context, allItems);
        gridview.setAdapter(customAdapter);

    }

    private void initNavigation(){
        drawerLayout = (DrawerLayout) findViewById(R.id.area_drawerLayout);
        drawerToggle = new ActionBarDrawerToggle(TheaterAreaActivity.this, drawerLayout, R.string.app_name, R.string.app_name);
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
        //  mToolbar.setTitleTextColor(ContextCompat.getColor(this, R.));
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
       //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //navigation home button.
        if (drawerToggle.onOptionsItemSelected(item))
            return true;

        int id = item.getItemId();

       // if (id == R.id.action_settings) {
           // return true;
       // }
        return super.onOptionsItemSelected(item);
    }

    private List<AreaObject> getAllAreaObject(){
       // AreaObject areaObject = null;
        List<AreaObject> items = new ArrayList<>();
        items.add(new AreaObject("基隆", "a01"));
        items.add(new AreaObject("台北東區", "a02z1"));
        items.add(new AreaObject("台北西區", "a02z2"));
        items.add(new AreaObject("台北南區", "a02z3"));
        items.add(new AreaObject("台北北區", "a02z4"));
        items.add(new AreaObject("新北市", "a02z5"));
        items.add(new AreaObject("台北二輪", "a02z6"));
        items.add(new AreaObject("桃園", "a03"));
        items.add(new AreaObject("台中", "a04"));
        items.add(new AreaObject("嘉義", "a05"));
        items.add(new AreaObject("台南", "a06"));
        items.add(new AreaObject("高雄", "a07"));
        items.add(new AreaObject("新竹", "a35"));
        items.add(new AreaObject("苗栗", "a37"));
        items.add(new AreaObject("花蓮", "a38"));
        items.add(new AreaObject("宜蘭", "a39"));
        items.add(new AreaObject("雲林", "a45"));
        items.add(new AreaObject("彰化", "a47"));
        items.add(new AreaObject("南投", "a49"));
        items.add(new AreaObject("金門", "a68"));
        items.add(new AreaObject("澎湖", "a69"));
        items.add(new AreaObject("屏東", "a87"));
        items.add(new AreaObject("台東", "a89"));

        return items;
    }
}
