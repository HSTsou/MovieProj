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
            DBConstants.MOVIE.STATE,
            DBConstants.MOVIE.AT_MOVIES_MV_ID,
            DBConstants.MOVIE.PLAYING_DATE,
            DBConstants.MOVIE.MAIN_NAME,
            DBConstants.MOVIE.EN_NAME,
            DBConstants.MOVIE.IMG_LINK,
            DBConstants.MOVIE.IMDb_RATING,
            DBConstants.MOVIE.TOMATOES_RATING,
            Movie.ID};

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static class Movie {
        static final String TABLE_NAME = "Movie";
        static final String ID = "id";
        static final String PLAYING_DATE = "playingDate";
        static final String AT_MOVIES_MV_ID = "atMoviesMvId";
        static final String MAIN_NAME = "mvName";
        static final String EN_NAME = "enName";
        static final String GATE = "gate";
        static final String IMG_LINK = "imgLink";
        static final String LENGTH = "mvlength";//GAE ....mvLength
        static final String DIRECTOR = "director";
        static final String ACTOR = "actor";
        // String LINK_LIST = "linkList";
        static final String STORY = "story";
        static final String UPDATE_DATE = "updateDate";
        static final String MV_TIME_STR = "mvTimeStr";
        static final String WRITER = "writer";
        static final String STATE = "state";
        static final String MV_IMDB_URL = "mv_IMDbMoblieUrl";
        static final String MV_TOMATOES_URL = "mv_tomatoesMoblieUrl";//mobile in GAE was spelled wrong.
        static final String IMDb_RATING = "IMDbRating";
        static final String TOMATOES_RATING = "tomatoesRating";
        static final String YOUTUBE_URL_LIST = "youtubeUrlList";
        static final String ALL_MV_TH_SHOWTIME_LIST = "allMvThShowtimeList";

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

    private static class Theater {
        static final String TABLE_NAME = "Theater";
        static final String ID = "_id";
        static final String NAME = "name";
        static final String ADDRESS = "address";
        static final String PHONE = "phone";
        static final String AREA = "area";
        static final String LAT = "lat";
        static final String LNG = "lng";

        public static final String buildTheaterSQL() {
            StringBuilder s = new StringBuilder();
            s.append(Theater.TABLE_NAME ).append(" (")
                    .append(Theater.ID).append(" text primary key, ").append(Theater.NAME)
                    .append(" text not null, ").append(Theater.ADDRESS).append(" text not null, ")
                    .append(Theater.PHONE).append(" text, ").append(Theater.AREA).append(" text not null, ")
                    .append(Theater.LAT).append(" text, ").append(Theater.LNG).append(" text);");
            return s.toString();
        }

    }

    private static class ThShowtime {
        static final String TABLE_NAME = "Th_Showtime";
        static final String TH_ID = "thId";
        static final String TIME_INFO_STR = "timeInfoStr";
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
        Log.i("hs", "insertTheaterTimeInfo");
        ContentValues values = new ContentValues();
        values.put(ThShowtime.TH_ID, thId);
        values.put(ThShowtime.TIME_INFO_STR, timeInfoStr);
        //values.put(TH_SHOWTIME.UPDATE_TIME, updateTime);
        return db.replace(ThShowtime.TABLE_NAME, null, values);
    }

    //20151108
    public Cursor getTheaterTimeInfoById(SQLiteDatabase db, String thId) {
        String WHERE = ThShowtime.TH_ID + " = ?";
        String[] ARGS = {thId};
        return db.query(ThShowtime.TABLE_NAME, COL_THEATER_SHOWTIMES_INFO, WHERE, ARGS,
                null, null, null);
    }

    //20151117
    public Cursor getTheaterInfoById(SQLiteDatabase db, String thId) {
        String WHERE = Theater.ID + " = ?";
        String[] ARGS = {thId};
        return db.query(Theater.TABLE_NAME, COL_THEATER_INFO, WHERE, ARGS,
                null, null, null);
    }

    //20151117
    public Cursor getGaeIDByAtsMovieMvId(SQLiteDatabase db, String mvId) {
        String[] array = {Movie.ID, Movie.AT_MOVIES_MV_ID};
        String WHERE = Movie.AT_MOVIES_MV_ID + " = ?";
        String[] ARGS = {mvId};
        return db.query(Movie.TABLE_NAME, array, WHERE, ARGS,
                null, null, null);
    }

    //20151110
    public long insertMovieInfo(SQLiteDatabase db, ContentValues values) {
        //   Log.i("hs", " insertMovieInfo" );
        return db.replace(Movie.TABLE_NAME, null, values);
    }

    //20151111
    public Cursor getMovieListData(SQLiteDatabase db, String orderBy, String state) {
        if (orderBy == null)
            orderBy = Movie.PLAYING_DATE;

        String WHERE = Movie.STATE + " = ?";
        String[] ARGS = {state};

        return db.query(Movie.TABLE_NAME, COL_MV_LIST, WHERE, ARGS,
                null, null, orderBy + " DESC");
    }

    //20151111
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
