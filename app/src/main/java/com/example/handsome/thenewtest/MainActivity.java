package com.example.handsome.thenewtest;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.handsome.thenewtest.helper.AssetsHelper;
import com.example.handsome.thenewtest.helper.DatabaseHelper;
import com.example.handsome.thenewtest.helper.JSONHelper;
import com.example.handsome.thenewtest.helper.SharedPreferencesHelper;
import com.example.handsome.thenewtest.third.NewtonCradleLoading;
import com.example.handsome.thenewtest.util.AppController;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    static final String MM_URL = "https://movingmoviezero.appspot.com/";
    final static String TAG = "hs";
    Context context;
    private SharedPreferencesHelper prefHelper;

    private List<String> playingMvId;//無入用
    private ImageView splashImg;
    private TextView info_text;
    private NewtonCradleLoading newtonCradleLoading;
    private SwipeRefreshLayout swipeView;
    DatabaseHelper helper ;

    SQLiteDatabase db ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "on creating");
        setContentView(R.layout.activity_main);

        helper = new DatabaseHelper(this);
        db = helper.getWritableDatabase();
        prefHelper = new SharedPreferencesHelper(this);
        context = this;
        info_text = (TextView) findViewById(R.id.info_text);
        splashImg = (ImageView) findViewById(R.id.splash_img);
        newtonCradleLoading = (NewtonCradleLoading) findViewById(R.id.newton_cradle_loading);
        newtonCradleLoading.start();

        swipeView = (SwipeRefreshLayout) findViewById(R.id.swipe);

        swipeView.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(context, R.color.black));
        swipeView.setColorSchemeResources(
                R.color.white,R.color.lake_green
                );

        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeView.setRefreshing(true); //starts refreshing
                getAllMvInfoByJSON();
            }
        });



        if (prefHelper.isFirstTime()) {//initiate the theater data.
            //initThread.start();
            if (initiateApp()) {
                Log.i(TAG, "Initiate successfully");
                prefHelper.setFirstTime(false);
            } else {
                Log.e(TAG, "Initiation failed");
            }
        } else {
            Log.i(TAG, "notFirstTime");
            clearTable();
        }
    }

    /**
     * 目前是一進入清空資料庫，
     */
    public void clearTable() {
       // SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(DatabaseHelper.Movie.TABLE_NAME, null, null);
        Log.i(TAG, "delete MOVIE.TABLE ");
    }


    public void clearMovieWithoutFav(){
        //取出sqlite all mvId
        //比對sharedperference or sqlite , 刪除沒有加入最愛的資料
        //直接用sharedperference存ID，再去GAE要電影資料，省去更新電影新資訊
    }

    private boolean initiateApp() {
        Log.i(TAG, "Initiating App");
        newtonCradleLoading.start();
        info_text.setText(R.string.init_data_ing);
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        AssetsHelper asHelper = new AssetsHelper(this, dbHelper);

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            try {
                asHelper.loadTheaterData(db);
                db.setTransactionSuccessful();
                return true;
            } catch (IOException e) {
                Log.e(TAG, e.toString());
            }
        } finally {

            db.endTransaction();
            db.close();
        }
        return false;
    }

    private Thread initThread = new Thread() {
        @Override
        public void run() {
            Log.i(TAG, "Strat App for the first time");
            if (initiateApp()) {
                Log.i(TAG, "Initiate successfully");
                prefHelper.setFirstTime(false);

            } else {
                Log.e(TAG, "Initiate unsuccessfully");
            }
        }

    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_refresh) {
            //  Toast.makeText(this, "refresh", Toast.LENGTH_SHORT).show();
            //  makeJsonArrayRequest();
        }

        return super.onOptionsItemSelected(item);
    }

    private void getAllMvInfoByJSON() {
        Log.i(TAG, "getAllMvInfo");
        info_text.setText(R.string.update_data_ing);
        final String allmV = "all-mv";
        String url = MM_URL + allmV;

        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Log.i(TAG, response.toString());
                        if (parseMvJsonAndStore(response)) {
                            Log.i(TAG, "movie Done ");

                        } else {
                            Log.i(TAG, "what's wrong? ");
                        }
                        newtonCradleLoading.stop();
                        swipeView.setRefreshing(false);
                        info_text.setText(R.string.update_data_finish);
                        //check firebase messaging including mvId, if turn into this movie page.
                        Intent intent = getIntent();
                        String mvId = intent.getStringExtra("mvId");
                        if (mvId!=null){
                            Log.d("FCM", "mvId:"+mvId);
                            Intent i = new Intent();
                            i.setClass(context, MovieInfoActivity.class);
                            i.putExtra("mvId", mvId);
                            startActivity(i);
                        }else{
                            startActivity(new Intent(MainActivity.this,
                                    MovieListActivity.class));
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i(TAG, "Error = " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        R.string.beautiful_error, Toast.LENGTH_SHORT).show();
                info_text.setText(R.string.update_data_error);
                swipeView.setRefreshing(false);

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Log.i(TAG, "no connection|timeout Error = " + error.getMessage());
                } else if (error instanceof AuthFailureError) {
                    Log.i(TAG, "AuthFailureError Error = " + error.getMessage());
                } else if (error instanceof ServerError) {
                    Log.i(TAG, "ServerError Error = " + error.getMessage());
                } else if (error instanceof NetworkError) {
                    Log.i(TAG, "NetworkError Error = " + error.getMessage());
                } else if (error instanceof ParseError) {
                    Log.i(TAG, "ParseError Error = " + error.getMessage());
                }
            }

        });

        AppController.getInstance().addToRequestQueue(req, AppController.TAG);
    }


    @Override
    public void onStart() {
        super.onStart();
        try {
            getAllMvInfoByJSON();
            //getAllThTimeByJSON();
        } finally {
            // db.close();
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        AppController.getInstance().cancelPendingRequests(AppController.TAG);
    }

    boolean parseMvJsonAndStore(JSONArray response) {
        db.beginTransaction();
        playingMvId = new ArrayList<>();//check movie is playing or not.
        Log.i(TAG, "playing Mv number :" + response.length());
        try {
            for (int i = 0; i < response.length(); i++) {
                try {
                    final JSONObject obj = response.getJSONObject(i);
                    final ContentValues cv = new ContentValues();

                    String GAEId = null;
                    try {
                        GAEId = obj.getJSONObject("key").getString("id");
                        //Log.i(TAG, " GAEId" + GAEId);
                        playingMvId.add(GAEId);

                        for (String unit : DatabaseHelper.COL_MOVIE) {
                            if (!obj.isNull(unit)) {
                                if (unit.equals("playingDate")) {
                                    String formatPlayingDate = obj.getString(unit).replaceAll("/", "-");
                                    cv.put(unit, formatPlayingDate);
                                    continue;
                                }
                                cv.put(unit, obj.getString(unit));
                            } else {
                                cv.putNull(unit);
                            }
                        }

                        Gson gson = new Gson();
                        if (!obj.isNull(DatabaseHelper.Movie.YOUTUBE_URL_LIST)) {
                            String youtubeListString = gson.toJson(JSONHelper.getStringListFromJsonArray(obj.getJSONArray(DatabaseHelper.Movie.YOUTUBE_URL_LIST)));
                            cv.put(DatabaseHelper.Movie.YOUTUBE_URL_LIST, youtubeListString);
                        }
                        if (!obj.isNull(DatabaseHelper.Movie.ALL_MV_TH_SHOWTIME_LIST)) {
                            String mvThTimeString = gson.toJson(JSONHelper.getStringListFromJsonArray(obj.getJSONArray(DatabaseHelper.Movie.ALL_MV_TH_SHOWTIME_LIST)));
                            cv.put(DatabaseHelper.Movie.ALL_MV_TH_SHOWTIME_LIST, mvThTimeString);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.i(TAG, "JSONException@parsing" + e);
                    }
                    cv.put(DatabaseHelper.Movie.ID, GAEId);
                    helper.insertMovieInfo(db, cv);


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i(TAG, " getAllMvInfo JSONException" + e);

                }
            }
            db.setTransactionSuccessful();

        } finally {
            //Log.i(TAG, "  db.endTransaction(); ");
            db.endTransaction();
            db.close();
        }

        return true;
    }


   /* private class MyAsyncTask extends AsyncTask<JSONArray, Integer, String> {
        //private ProgressDialog dialog;


        @Override
        protected void onPreExecute() {
            pDialog = ProgressDialog.show(MainActivity.this, null, null);
            pDialog.show();
        }

        @Override
        protected String doInBackground(JSONArray... response) {

            return "ok";
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            pDialog.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            if(result.equalsIgnoreCase("ok")){
                hidepDialog();
                startActivity(new Intent(MainActivity.this,
                        MovieListActivity.class));
            }

        }

    }*/


}
