package com.example.handsome.thenewtest;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.example.handsome.thenewtest.entity.Movie;
import com.example.handsome.thenewtest.fragment.MovieInfoFragment;
import com.example.handsome.thenewtest.fragment.MovieTimeTabFragment;
import com.example.handsome.thenewtest.helper.DatabaseHelper;
import com.example.handsome.thenewtest.helper.JSONHelper;
import com.example.handsome.thenewtest.util.AppController;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class MovieInfoActivity extends AppCompatActivity {

    static final String MV_INFO_URL = "https://movingmoviezero.appspot.com/mvInfo?id=";
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
    DatabaseHelper helper ;
    SQLiteDatabase db;

    private YouTubePlayerView playerView;
    Context c;

    Movie m;
    String mvId;//accurately,  gatId of movie
    String youtubeThumbnail_Url = "http://img.youtube.com/vi/%s/0.jpg"; //0 : larger img, 1,2,3 different image but small.
    String vId ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_info);
        c = this;
        helper = new DatabaseHelper(this);
        db = helper.getWritableDatabase();

        if (savedInstanceState != null) {
            // Restore value of members from saved state
           mvId = savedInstanceState.getString("mvId");
           Log.i("hs", "savedInstanceState.getString(\"mvId\")" + mvId);

            initToolBar();
            initNavigation();
            m = loadMvInfoFromDB(mvId);
            title = m.getMvName();
            url = m.getImgLink();
            setTrailerPic();
            initInstances();
        } else {
            // Probably initialize members with default values for a new instance
            mvId = getIntent().getStringExtra("mvId");
            initToolBar();
            initNavigation();
            getMovieInfoByJson();
        }

        //YouTubeFragment fragment = (YouTubeFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_youtube);
        //fragment.setVideoId("VxJsGKn2kyA");
    }

    void parseMvInfoJsonAndStore(JSONObject response){
        Log.i("hs", "parseMvInfoJsonAndStore");

            try {
                JSONObject obj = response;

                ContentValues cv = new ContentValues();

                String GAEId = obj.getJSONObject("key").getString("id");
                cv.put(DBConstants.MOVIE.ID, GAEId);

                for(String unit : DatabaseHelper.COL_MOVIE){
                    if(!obj.isNull(unit)){
                        if(unit == "playingDate"){
                            String formatPlayingDate = obj.getString(unit).replaceAll("/", "-");
                            cv.put(unit, formatPlayingDate);
                            continue;
                        }
                        cv.put(unit, obj.getString(unit));
                    }else{
                        cv.putNull(unit);
                    }
                }

                Gson gson = new Gson();
                if(!obj.isNull(DBConstants.MOVIE.YOUTUBE_URL_LIST)){
                    String youtubeListString = gson.toJson(JSONHelper.getStringListFromJsonArray(obj.getJSONArray(DBConstants.MOVIE.YOUTUBE_URL_LIST)));
                    cv.put(DBConstants.MOVIE.YOUTUBE_URL_LIST, youtubeListString);
                }
                if(!obj.isNull(DBConstants.MOVIE.ALL_MV_TH_SHOWTIME_LIST)) {
                    String mvThTimeString = gson.toJson(JSONHelper.getStringListFromJsonArray(obj.getJSONArray(DBConstants.MOVIE.ALL_MV_TH_SHOWTIME_LIST)));
                    cv.put(DBConstants.MOVIE.ALL_MV_TH_SHOWTIME_LIST, mvThTimeString);
                }

                helper.insertMovieInfo(db, cv);

            } catch (JSONException e) {
                e.printStackTrace();
                Log.i("hs", " getAllMvInfo JSONException" + e);

            }


    }


    void getMovieInfoByJson(){

        JsonObjectRequest req = new JsonObjectRequest(MV_INFO_URL + mvId,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.i(TAG, response.toString());
                        parseMvInfoJsonAndStore(response);

                        m = loadMvInfoFromDB(mvId);
                        title = m.getMvName();
                        url = m.getImgLink();
                        setTrailerPic();
                        initInstances();


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

    public Movie loadMvInfoFromDB(String mvId){
        DatabaseHelper helper = new DatabaseHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();

        Movie mv;
        Cursor c = helper.getMovieInfoByAtsMovieId(db, mvId);
        try {
            c.moveToFirst();
            mv = new Movie();
            mv.setPlayingDate(c.getString(1));
            mv.setMvName(c.getString(2));
            mv.setEnName(c.getString(3));
            mv.setGate(c.getString(4));
            mv.setImgLink(c.getString(5));
            mv.setMvlength(c.getString(6));
            mv.setDirector(c.getString(7));
            mv.setActor(c.getString(8));
            mv.setStory(c.getString(9));
            mv.setWriter(c.getString(10));
            mv.setState(c.getString(11));
            mv.setMv_IMDbMoblieUrl(c.getString(12));
            mv.setMv_TomatoesMoblieUrl(c.getString(13));
            mv.setIMDbRating(Double.parseDouble(c.getString(14)));
            mv.setTomatoesRating(Double.parseDouble(c.getString(15)));
            Log.i("hs", "IMDB  = " + Double.parseDouble(c.getString(14)));
            Log.i("hs", "tomato  = " + Double.parseDouble(c.getString(15)));
            Gson gson = new Gson();
            Type type = new TypeToken<List<String>>() {}.getType();
            List<String> mvTimeInfoStr_List = gson.fromJson(c.getString(16), type);
            List<String> youtube_List = gson.fromJson(c.getString(17), type);
            mv.setAllMvThShowtimeList(mvTimeInfoStr_List);
            mv.setYoutubeUrlList(youtube_List);

        } finally {
            c.close();
            db.close();
        }

        return mv;
    }

    private void setTrailerPic(){
        List<String> utubeUrlList = m.getYoutubeUrlList();

        String utubeUrl;
        if(utubeUrlList.size() >0){
            /*for(String url : utubeUrlList){
                JSONObject obj = new JSONObject(url);//in the GAE, if using TEXT type, it will return redundant {"value":"XXXX"}..mabye...
                utubeUrl =  obj.getString("value");
                Log.i("hs", "UtubeUrl = " +  obj.getString("value"));
            }*/

            try{
                JSONObject obj = new JSONObject(utubeUrlList.get(0));
                utubeUrl =  obj.getString("value");
                vId = utubeUrl.substring(utubeUrl.lastIndexOf("/")+1);//ex: "https://www.youtube.com/embed/2S05N0PYYas"
            }catch(JSONException e){
                Log.i("hs", "utubeUrl JSONException" + e);
            }

        }


        mv_pic = (NetworkImageView) findViewById(R.id.mv_pic);
        mv_pic.setImageUrl(String.format(youtubeThumbnail_Url, vId), imageLoader);
        mv_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* final Intent lightboxIntent = new Intent(c, CustomLightboxActivity.class);
                lightboxIntent.putExtra(CustomLightboxActivity.KEY_VIDEO_ID, vId);
                startActivity(lightboxIntent);*/
                Intent web = new Intent(c, WebActivity.class);
                String youtube = "https://www.youtube.com/watch?v=";
                web.putExtra("url", youtube+vId);
                startActivity(web);
            }
        });
    }


    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setLogo(R.drawable.mm_logo);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);//remove the title text
    }

    /**
     * set the tab and tab's content
     */
    private void initInstances() {

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        collapsingToolbarLayout.setTitle(title);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedappbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedappbar);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.info_viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.info_tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        //rootLayout = (CoordinatorLayout) findViewById(R.id.rootLayout);
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
       // getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        Log.i("hs", "onSaveInstanceState" );
        outState.putString("mvId", mvId );
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("hs", "onStop()");
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.i("hs", " onPostResume()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("hs", " onResume()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("hs", "onDestroy()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("hs", "onRestart()");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i("hs", "onRestoreInstanceState");
        Log.i("hs", "savedInstanceState.get(\"mvId\") = " + savedInstanceState.get("mvId"));;
    }
}
