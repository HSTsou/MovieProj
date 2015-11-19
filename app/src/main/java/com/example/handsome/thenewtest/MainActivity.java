package com.example.handsome.thenewtest;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Response;
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
    DatabaseHelper helper ;
    SQLiteDatabase db;
    List<String> playingMvId;
    ImageView splashImg;

   private NewtonCradleLoading newtonCradleLoading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("hs", "on creating");
        setContentView(R.layout.activity_main);

        prefHelper = new SharedPreferencesHelper(this);
        context = this;

        //splashImg = (ImageView) findViewById(R.id.splash_img);
        newtonCradleLoading = (NewtonCradleLoading) findViewById(R.id.newton_cradle_loading);
        newtonCradleLoading.start();

        helper = new DatabaseHelper(this);
        db = helper.getWritableDatabase();


        if (prefHelper.isFirstTime()) {//initiate the theater data.
            initThread.start();
        }else{
            Log.i("hs", "notFirstTime");
            clearTable();
        }

    }

    public void clearTable(){
        db.delete(DBConstants.MOVIE.TABLE_NAME, null,null);
        Log.i("hs", "delete MOVIE.TABLE ");
    }

    private boolean initiateApp() {
        Log.i("hs", "Initiating App");
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        AssetsHelper asHelper = new AssetsHelper(this, dbHelper);

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            try {
                asHelper.loadTheaterData(db);
                return true;
            } catch (IOException e) {
                Log.e("hs", e.toString());
            }
        } finally {
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
        Log.i("hs", "getAllMvInfo");
        final String allmV = "all-mv";
        String url = MM_URL +allmV;
        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Log.i(TAG, response.toString());
                        if(parseMvJsonAndStore(response)){
                            Log.i("hs", "movie Done ");

                        }else{
                            Log.i("hs", "what's wrong? ");
                        }
                        newtonCradleLoading.stop();

                        startActivity(new Intent(MainActivity.this,
                                MovieListActivity.class));
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

        AppController.getInstance().addToRequestQueue(req, AppController.TAG);
    }


    @Override
    public void onStart(){
        super.onStart();
        try{
            getAllMvInfoByJSON();
            //getAllThTimeByJSON();
        }finally {
           // db.close();
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        AppController.getInstance().cancelPendingRequests(AppController.TAG);
    }

    boolean parseMvJsonAndStore(JSONArray response){

        db.beginTransaction();
        playingMvId = new ArrayList<>();//check movie is playing or not.

        try{
            for (int i = 0; i < response.length(); i++) {
                try {
                    final JSONObject obj = response.getJSONObject(i);
                    final ContentValues cv = new ContentValues();

                            String GAEId = null;
                            try {
                                GAEId = obj.getJSONObject("key").getString("id");
                                Log.i("hs", " GAEId" + GAEId);
                                playingMvId.add(GAEId);

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

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            cv.put(DBConstants.MOVIE.ID, GAEId);
                            helper.insertMovieInfo(db, cv);


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i("hs", " getAllMvInfo JSONException" + e);

                }
            }
            db.setTransactionSuccessful();

        }finally{
            //Log.i("hs", "  db.endTransaction(); ");
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
