package com.example.handsome.thenewtest;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
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
import android.view.View;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.handsome.thenewtest.entity.Movie;
import com.example.handsome.thenewtest.fragment.MovieInfoFragment;
import com.example.handsome.thenewtest.fragment.MovieTimeTabFragment;
import com.example.handsome.thenewtest.util.AppController;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.ArrayList;
import java.util.List;


public class MovieInfoActivity extends AppCompatActivity {

    CoordinatorLayout rootLayout;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    FloatingActionButton fabBtn;
    Toolbar toolbar;
    TabLayout tabLayout;
    CollapsingToolbarLayout collapsingToolbarLayout;
    NetworkImageView mv_pic;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    String title, url;
    private YouTubePlayerView playerView;
    Context c;
    public static final String VIDEO_ID = "o7VVHhK9zf0";
    Movie m;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_info);
        c = this;
        m = getIntent().getParcelableExtra("movie");
        Log.i("hs", "name = " + m.getMvName() + " url" + m.getImgLink());
        title = m.getMvName();
        url = m.getImgLink();
        initInstances();

        //YouTubeFragment fragment = (YouTubeFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_youtube);
        //fragment.setVideoId("VxJsGKn2kyA");
    }

    private void initInstances() {
        initNavigation();
        mv_pic = (NetworkImageView) findViewById(R.id.mv_pic);
        mv_pic.setImageUrl(url, imageLoader);
        mv_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Intent lightboxIntent = new Intent(c, CustomLightboxActivity.class);
                lightboxIntent.putExtra(CustomLightboxActivity.KEY_VIDEO_ID, VIDEO_ID);
                startActivity(lightboxIntent);

            }
        });

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setLogo(R.drawable.mm_logo);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        collapsingToolbarLayout.setTitle(title);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.info_viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.info_tabLayout);
        tabLayout.setupWithViewPager(viewPager);



        rootLayout = (CoordinatorLayout) findViewById(R.id.rootLayout);

     /*   fabBtn = (FloatingActionButton) findViewById(R.id.fabBtn);
        fabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(rootLayout, "Hello. I am Snackbar!", Snackbar.LENGTH_SHORT)
                        .setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        })
                        .show();
            }
        });
*/


    }

    private void initNavigation(){
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerToggle = new ActionBarDrawerToggle(MovieInfoActivity.this, drawerLayout, R.string.app_name, R.string.app_name);
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

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(MovieInfoFragment.createInstance(m), getString(R.string.movie_info));
        adapter.addFrag(MovieTimeTabFragment.createInstance((ArrayList)m.getAllMvThShowtimeList()), getString(R.string.movie_time));
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
