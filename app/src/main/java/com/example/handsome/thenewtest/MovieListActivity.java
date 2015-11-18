package com.example.handsome.thenewtest;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
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

import com.example.handsome.thenewtest.fragment.MovieListFragment;

import java.util.ArrayList;
import java.util.List;

public class MovieListActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
       // setTheme(R.style.AppThemeBlue);
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_movie_list);

        initNavigation();
        initToolbar();
        initViewPagerAndTabs();
    }

    private void initNavigation(){
        drawerLayout = (DrawerLayout) findViewById(R.id.movielist_drawerLayout);
        drawerToggle = new ActionBarDrawerToggle(MovieListActivity.this, drawerLayout, R.string.app_name, R.string.app_name);
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
        //setTitle(getString(R.string.app_name));
      //  mToolbar.setTitleTextColor(ContextCompat.getColor(this, R.));
    }

    private void initViewPagerAndTabs() {

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());

        pagerAdapter.addFragment(MovieListFragment.createInstance("thisWeek"),  getString(R.string.this_week));
        pagerAdapter.addFragment(MovieListFragment.createInstance("firstRun"), getString(R.string.first_run));
        pagerAdapter.addFragment(MovieListFragment.createInstance("secondRun"), getString(R.string.second_run));
        pagerAdapter.addFragment(MovieListFragment.createInstance("notYet"), getString(R.string.not_yet));

        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }



    static class PagerAdapter extends FragmentPagerAdapter {// FragmentStatePagerAdapter

        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        public PagerAdapter(FragmentManager fragmentManager ) {
            super(fragmentManager);

        }

        public void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            Log.i("hs", "MovieListFragment.getInstance(position);");
            return fragmentList.get(position);

           //return MovieListFragment.getInstance(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
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
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item))
            return true;
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_refresh) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}