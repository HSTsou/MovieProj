package com.example.handsome.thenewtest.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.handsome.thenewtest.DBConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class DatabaseHelper extends SQLiteOpenHelper implements DBConstants {

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static final String[] BASIC_MOVIE_COLUMNS = {MOVIE.IMG_LINK,
            MOVIE.MAIN_NAME, MOVIE.EN_NAME, MOVIE.ID};

    public static final String[] COL_THEATER_LIST = {THEATER.NAME,
            THEATER.ADDRESS, THEATER.ID, THEATER.LAT, THEATER.LNG};

    public static final String[] COL_THEATER_SHOWTIMES_INFO = {TH_SHOWTIME.TH_ID,
            TH_SHOWTIME.TIME_INFO_STR};
    public static final String[] COL_THEATER_INFO = {THEATER.ID,THEATER.NAME,
            THEATER.ADDRESS,THEATER.PHONE,THEATER.LAT,THEATER.LNG,};
    public static final String[] COL_MOVIE = {
            MOVIE.AT_MOVIES_MV_ID,
            MOVIE.PLAYING_DATE,
            MOVIE.MAIN_NAME,
            MOVIE.EN_NAME,
            MOVIE.GATE,
            MOVIE.IMG_LINK,
            MOVIE.LENGTH,
            MOVIE.DIRECTOR,
            MOVIE.ACTOR,
            MOVIE.STORY,
            MOVIE.WRITER,
            MOVIE.STATE,
            MOVIE.MV_IMDB_URL,
            MOVIE.MV_TOMATOES_URL,
            MOVIE.IMDb_RATING,
            MOVIE.TOMATOES_RATING
    };

    public static final String[] COL_MOVIE_INFO = {
            MOVIE.AT_MOVIES_MV_ID,
            MOVIE.PLAYING_DATE,
            MOVIE.MAIN_NAME,
            MOVIE.EN_NAME,
            MOVIE.GATE,
            MOVIE.IMG_LINK,
            MOVIE.LENGTH,
            MOVIE.DIRECTOR,
            MOVIE.ACTOR,
            MOVIE.STORY,
            MOVIE.WRITER,
            MOVIE.STATE,
            MOVIE.MV_IMDB_URL,
            MOVIE.MV_TOMATOES_URL,
            MOVIE.IMDb_RATING,
            MOVIE.TOMATOES_RATING,
            MOVIE.ALL_MV_TH_SHOWTIME_LIST,
            MOVIE.YOUTUBE_URL_LIST
    };

    public static final String[] COL_MV_LIST = {
            DBConstants.MOVIE.STATE,
            DBConstants.MOVIE.AT_MOVIES_MV_ID,
            DBConstants.MOVIE.PLAYING_DATE,
            DBConstants.MOVIE.MAIN_NAME,
            DBConstants.MOVIE.EN_NAME,
            DBConstants.MOVIE.IMG_LINK,
            DBConstants.MOVIE.IMDb_RATING,
            DBConstants.MOVIE.TOMATOES_RATING,
            MOVIE.ID};

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("TEST", "DatabaseHelper creates tables");
        db.execSQL("create table " + CREATE_MOVIE);
        db.execSQL("create table " + CREATE_THEATER);
        db.execSQL("create table " + CREATE_TH_SHOWTIME);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    @SuppressWarnings("unchecked")
    private ContentValues getContentValues(JSONObject json) {
        ContentValues values = new ContentValues();
        Log.i("TEST", "ContentValues values = new ContentValues();");
        for (Iterator<String> i = json.keys(); i.hasNext(); ) {
            String key = i.next();
            try {
                values.put(key, json.getString(key));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return values;
    }

    //20151108
    public long insertTheaterTimeInfo(SQLiteDatabase db, String thId,
                                      String timeInfoStr) {
        Log.i("hs", "insertTheaterTimeInfo" );
        ContentValues values = new ContentValues();
        values.put(TH_SHOWTIME.TH_ID, thId);
        values.put(TH_SHOWTIME.TIME_INFO_STR, timeInfoStr);
        //values.put(TH_SHOWTIME.UPDATE_TIME, updateTime);
        return db.replace(TH_SHOWTIME.TABLE_NAME, null, values);
    }

    //20151108
    public Cursor getTheaterTimeInfoById(SQLiteDatabase db, String thId) {
        String WHERE = TH_SHOWTIME.TH_ID + " = ?";
        String[] ARGS = {thId};
        return db.query(TH_SHOWTIME.TABLE_NAME, COL_THEATER_SHOWTIMES_INFO, WHERE, ARGS,
                null, null, null);
    }

    //20151117
    public Cursor getTheaterInfoById(SQLiteDatabase db, String thId) {
        String WHERE = THEATER.ID + " = ?";
        String[] ARGS = {thId};
        return db.query(THEATER.TABLE_NAME, COL_THEATER_INFO , WHERE, ARGS,
                null, null, null);
    }

    //20151117
    public Cursor getGaeIDByAtsMovieMvId(SQLiteDatabase db, String mvId) {
        String[] array = {MOVIE.ID, MOVIE.AT_MOVIES_MV_ID};
        String WHERE = MOVIE.AT_MOVIES_MV_ID + " = ?";
        String[] ARGS = {mvId};
        return db.query(MOVIE.TABLE_NAME, array , WHERE, ARGS,
                null, null, null);
    }

    //20151110
    public long insertMovieInfo(SQLiteDatabase db, ContentValues values) {
     //   Log.i("hs", " insertMovieInfo" );
        return db.replace(MOVIE.TABLE_NAME, null, values);
    }

    //20151111
    public Cursor getMovieListData(SQLiteDatabase db, String orderBy, String state) {
        if (orderBy == null)
            orderBy = MOVIE.PLAYING_DATE;

        String WHERE = MOVIE.STATE + " = ?";
        String[] ARGS = {state};

        return db.query(MOVIE.TABLE_NAME, COL_MV_LIST, WHERE, ARGS,
                null, null, orderBy + " DESC");
    }

    //20151111
    public Cursor getMovieInfoByAtsMovieId(SQLiteDatabase db, String mvId) {

        String WHERE = MOVIE.ID + " = ?";
        String[] ARGS = {mvId};

        return db.query(MOVIE.TABLE_NAME, COL_MOVIE_INFO, WHERE, ARGS,
                null, null, null);
    }


    public long insertTheater(SQLiteDatabase db, String[] theater, String area) {
        ContentValues values = new ContentValues();
        values.put(THEATER.ID, theater[0]);
        values.put(THEATER.NAME, theater[1]);
        values.put(THEATER.ADDRESS, theater[2]);
        values.put(THEATER.PHONE, theater[3]);
        values.put(THEATER.LAT, theater[4]);
        values.put(THEATER.LNG, theater[5]);
        values.put(THEATER.AREA, area);
        return db.insert(THEATER.TABLE_NAME, null, values);
    }

      public Cursor getTheaters(SQLiteDatabase db, String area) {
        String WHERE = THEATER.AREA + " = ?";
        String[] ARGS = {area};
        return db.query(THEATER.TABLE_NAME, COL_THEATER_LIST, WHERE, ARGS,
                null, null, null);
    }



}
