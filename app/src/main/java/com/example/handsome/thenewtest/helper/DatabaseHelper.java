package com.example.handsome.thenewtest.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import static com.android.volley.VolleyLog.TAG;


public class DatabaseHelper extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "MMDB";
    static final int DATABASE_VERSION = 1;
    public static final String[] COL_MOVIE = {
            Movie.AT_MOVIES_MV_ID,
            Movie.PLAYING_DATE,
            Movie.MAIN_NAME,
            Movie.EN_NAME,
            Movie.GATE,
            Movie.IMG_LINK,
            Movie.LENGTH,
            Movie.DIRECTOR,
            Movie.ACTOR,
            Movie.STORY,
            Movie.WRITER,
            Movie.STATE,
            Movie.MV_IMDB_URL,
            Movie.MV_TOMATOES_URL,
            Movie.IMDb_RATING,
            Movie.TOMATOES_RATING
    };
    public static final String[] COL_MOVIE_INFO = {
            Movie.AT_MOVIES_MV_ID,
            Movie.PLAYING_DATE,
            Movie.MAIN_NAME,
            Movie.EN_NAME,
            Movie.GATE,
            Movie.IMG_LINK,
            Movie.LENGTH,
            Movie.DIRECTOR,
            Movie.ACTOR,
            Movie.STORY,
            Movie.WRITER,
            Movie.STATE,
            Movie.MV_IMDB_URL,
            Movie.MV_TOMATOES_URL,
            Movie.IMDb_RATING,
            Movie.TOMATOES_RATING,
            Movie.ALL_MV_TH_SHOWTIME_LIST,
            Movie.YOUTUBE_URL_LIST
    };
    public static final String[] COL_MV_LIST = {
            Movie.STATE,
            Movie.AT_MOVIES_MV_ID,
            Movie.PLAYING_DATE,
            Movie.MAIN_NAME,
            Movie.EN_NAME,
            Movie.IMG_LINK,
            Movie.IMDb_RATING,
            Movie.TOMATOES_RATING,
            Movie.ID};

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static class Movie {
        public static final String TABLE_NAME = "Movie";
        public static final String ID = "id";
        public static final String PLAYING_DATE = "playingDate";
        public static final String AT_MOVIES_MV_ID = "atMoviesMvId";
        public static final String MAIN_NAME = "mvName";
        public static final String EN_NAME = "enName";
        public static final String GATE = "gate";
        public static final String IMG_LINK = "imgLink";
        public static final String LENGTH = "mvlength";//GAE ....mvLength
        public static final String DIRECTOR = "director";
        public static final String ACTOR = "actor";
        // String LINK_LIST = "linkList";
        public static final String STORY = "story";
        public static final String UPDATE_DATE = "updateDate";
        public static final String MV_TIME_STR = "mvTimeStr";
        public static final String WRITER = "writer";
        public static final String STATE = "state";
        public static final String MV_IMDB_URL = "mv_IMDbMoblieUrl";
        public static final String MV_TOMATOES_URL = "mv_tomatoesMoblieUrl";//mobile in GAE was spelled wrong.
        public static final String IMDb_RATING = "IMDbRating";
        public static final String TOMATOES_RATING = "tomatoesRating";
        public static final String YOUTUBE_URL_LIST = "youtubeUrlList";
        public static final String ALL_MV_TH_SHOWTIME_LIST = "allMvThShowtimeList";

        public static final String buildMovieSQL() {
            StringBuilder s = new StringBuilder();
            s.append(Movie.TABLE_NAME).append(" (")
                    .append(Movie.ID).append(" text , ").append(Movie.MAIN_NAME)
                    .append(" text not null, ").append(Movie.EN_NAME).append(" text, ").append(Movie.AT_MOVIES_MV_ID)
                    .append(" text, ").append(Movie.GATE).append(" text, ").append(Movie.IMG_LINK).append(" text, ")
                    .append(Movie.WRITER).append(" text, ")
                    .append(Movie.LENGTH).append(" text, ").append(Movie.DIRECTOR).append(" text, ")
                    .append(Movie.ACTOR).append(" text, ").append(Movie.MV_TIME_STR).append(" text, ")
                    .append(Movie.STORY).append(" text, ")
                    .append(Movie.PLAYING_DATE).append(" text, ").append(Movie.STATE).append(" text, ")
                    .append(Movie.MV_IMDB_URL).append(" text, ").append(Movie.MV_TOMATOES_URL).append(" text, ")
                    .append(Movie.IMDb_RATING).append(" real, ")
                    .append(Movie.TOMATOES_RATING).append(" real, ")
                    .append(Movie.YOUTUBE_URL_LIST).append(" text, ")
                    .append(Movie.ALL_MV_TH_SHOWTIME_LIST).append(" text, ").append(Movie.UPDATE_DATE).append(" text, PRIMARY KEY (").append(Movie.ID).append(") );");
            return s.toString();

        }

    }

    public static class Theater {
        public static final String TABLE_NAME = "Theater";
        public static final String ID = "_id";
        public static final String NAME = "name";
        public static final String ADDRESS = "address";
        public static final String PHONE = "phone";
        public static final String AREA = "area";
        public static final String LAT = "lat";
        public static final String LNG = "lng";

        static String buildTheaterSQL() {
            StringBuilder s = new StringBuilder();
            s.append(Theater.TABLE_NAME).append(" (")
                    .append(Theater.ID).append(" text primary key, ").append(Theater.NAME)
                    .append(" text not null, ").append(Theater.ADDRESS).append(" text not null, ")
                    .append(Theater.PHONE).append(" text, ").append(Theater.AREA).append(" text not null, ")
                    .append(Theater.LAT).append(" text, ").append(Theater.LNG).append(" text);");
            return s.toString();
        }

    }

    public static class ThShowtime {
        public static final String TABLE_NAME = "Th_Showtime";
        public static final String TH_ID = "thId";
        public static final String TIME_INFO_STR = "timeInfoStr";
        //String UPDATE_TIME= "updateTime";

        public static final String buildThShowtimeSQL() {
            StringBuilder s = new StringBuilder();
            s.append(ThShowtime.TABLE_NAME).append(" (")
                    .append(ThShowtime.TH_ID).append(" text primary key, ").append(ThShowtime.TIME_INFO_STR)
                    .append(" text);");
            return s.toString();
        }

    }

    public static final String[] BASIC_MOVIE_COLUMNS = {Movie.IMG_LINK,
            Movie.MAIN_NAME, Movie.EN_NAME, Movie.ID};
    public static final String[] COL_THEATER_LIST = {Theater.NAME,
            Theater.ADDRESS, Theater.ID, Theater.LAT, Theater.LNG};
    public static final String[] COL_THEATER_SHOWTIMES_INFO = {ThShowtime.TH_ID,
            ThShowtime.TIME_INFO_STR};
    public static final String[] COL_THEATER_INFO = {Theater.ID, Theater.NAME,
            Theater.ADDRESS, Theater.PHONE, Theater.LAT, Theater.LNG,};


    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "DatabaseHelper creates tables");
        db.execSQL("create table " + Movie.buildMovieSQL());
        db.execSQL("create table " + Theater.buildTheaterSQL());
        db.execSQL("create table " + ThShowtime.buildThShowtimeSQL());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    @SuppressWarnings("unchecked")
    private ContentValues getContentValues(JSONObject json) {
        ContentValues values = new ContentValues();
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

    public long insertTheaterTimeInfo(SQLiteDatabase db, String thId,
                                      String timeInfoStr) {
        Log.i("hs", "insertTheaterTimeInfo");
        ContentValues values = new ContentValues();
        values.put(ThShowtime.TH_ID, thId);
        values.put(ThShowtime.TIME_INFO_STR, timeInfoStr);
        //values.put(TH_SHOWTIME.UPDATE_TIME, updateTime);
        return db.replace(ThShowtime.TABLE_NAME, null, values);
    }

    public Cursor getTheaterTimeInfoById(SQLiteDatabase db, String thId) {
        String WHERE = ThShowtime.TH_ID + " = ?";
        String[] ARGS = {thId};
        return db.query(ThShowtime.TABLE_NAME, COL_THEATER_SHOWTIMES_INFO, WHERE, ARGS,
                null, null, null);
    }

    public Cursor getTheaterInfoById(SQLiteDatabase db, String thId) {
        String WHERE = Theater.ID + " = ?";
        String[] ARGS = {thId};
        return db.query(Theater.TABLE_NAME, COL_THEATER_INFO, WHERE, ARGS,
                null, null, null);
    }

    public Cursor getGaeIDByAtsMovieMvId(SQLiteDatabase db, String mvId) {
        String[] array = {Movie.ID, Movie.AT_MOVIES_MV_ID};
        String WHERE = Movie.AT_MOVIES_MV_ID + " = ?";
        String[] ARGS = {mvId};
        return db.query(Movie.TABLE_NAME, array, WHERE, ARGS,
                null, null, null);
    }

    public long insertMovieInfo(SQLiteDatabase db, ContentValues values) {
        return db.replace(Movie.TABLE_NAME, null, values);
    }

    public Cursor getMovieListData(SQLiteDatabase db, String orderBy, String state) {
        if (orderBy == null)
            orderBy = Movie.PLAYING_DATE;

        String WHERE = Movie.STATE + " = ?";
        String[] ARGS = {state};

        return db.query(Movie.TABLE_NAME, COL_MV_LIST, WHERE, ARGS,
                null, null, orderBy + " DESC");
    }

    public Cursor getMovieInfoByAtsMovieId(SQLiteDatabase db, String mvId) {

        String WHERE = Movie.ID + " = ?";
        String[] ARGS = {mvId};

        return db.query(Movie.TABLE_NAME, COL_MOVIE_INFO, WHERE, ARGS,
                null, null, null);
    }

    public long insertTheater(SQLiteDatabase db, String[] theater, String area) {
        ContentValues values = new ContentValues();
        values.put(Theater.ID, theater[0]);
        values.put(Theater.NAME, theater[1]);
        values.put(Theater.ADDRESS, theater[2]);
        values.put(Theater.PHONE, theater[3]);
        values.put(Theater.LAT, theater[4]);
        values.put(Theater.LNG, theater[5]);
        values.put(Theater.AREA, area);
        return db.insert(Theater.TABLE_NAME, null, values);
    }

    public Cursor getTheaters(SQLiteDatabase db, String area) {
        String WHERE = Theater.AREA + " = ?";
        String[] ARGS = {area};
        return db.query(Theater.TABLE_NAME, COL_THEATER_LIST, WHERE, ARGS,
                null, null, null);
    }


}
