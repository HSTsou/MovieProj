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
    public static final String[] REC_MOVIE_COLUMNS = {MOVIE.ID, MOVIE.IMG_LINK,
            MOVIE.MAIN_NAME};
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
        Log.i("hs", " insertMovieInfo" );
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

    public long insertMovie(SQLiteDatabase db, JSONObject movie) {
        return db.insert(MOVIE.TABLE_NAME, null, getContentValues(movie));
    }

    public Cursor getBasicMovies(SQLiteDatabase db, String orderBy) {
        if (orderBy == null)
            orderBy = MOVIE.PLAYING_DATE;

        return db.query(MOVIE.TABLE_NAME, BASIC_MOVIE_COLUMNS, null, null,
                null, null, orderBy + " DESC");
    }

    public Cursor getMovieNameAndPic(SQLiteDatabase db, String movieId) {
        String WHERE = MOVIE.ID + " = ?";
        String[] FROM = {MOVIE.MAIN_NAME, MOVIE.IMG_LINK};
        return db.query(MOVIE.TABLE_NAME, FROM, WHERE,
                new String[]{movieId}, null, null, null);
    }

    public Cursor getMovie(SQLiteDatabase db, String movieId) {
        String WHERE = MOVIE.ID + " = ?";
        return db.query(MOVIE.TABLE_NAME, null, WHERE,
                new String[]{movieId}, null, null, null);
    }


    public Cursor getRecMovieById(SQLiteDatabase db, String movieId) {
        String WHERE = MOVIE.ID + " = ?";
        return db.query(MOVIE.TABLE_NAME, REC_MOVIE_COLUMNS, WHERE,
                new String[]{movieId}, null, null, null);
    }

    public Cursor getRecMovie(SQLiteDatabase db, String movieId) {
        String WHERE = MOVIE.ID + " = ?";
        return db.query(MOVIE.TABLE_NAME, REC_MOVIE_COLUMNS, WHERE,
                new String[]{movieId}, null, null, null);
    }

    public Cursor getMovieIdByName(SQLiteDatabase db, String movieName) {
        String WHERE = MOVIE.MAIN_NAME + " = ?";
        return db.query(MOVIE.TABLE_NAME, REC_MOVIE_COLUMNS, WHERE,
                new String[]{movieName}, null, null, null);
    }



    public Cursor getMovieShowtimes(SQLiteDatabase db, String movieId,
                                    String area) {
        // MOVIE_SHOWTIMES
        String SQL = "select A.name, B.typeList, B.updateDate, B.timeList "
                + "from Theater A, Showtime B "
                + "where A._id = B.thId and B.movie = ? and A.area = ? ";
        String[] ARGS = {movieId, area};
        return db.rawQuery(SQL, ARGS);
    }



    public Cursor getTheaterShowtimes(SQLiteDatabase db, String thId) {
        // THEATER_SHOWTIMES
        String SQL = "select A.mainName, B.typeList, B.updateDate, B.timeList "
                + "from Movie A, Showtime B "
                + "where A.id = B.movie and B.thId = ? ";
        String[] ARGS = {thId};
        Log.e("TEST", "return db.rawQuery!!in db");
        return db.rawQuery(SQL, ARGS);

    }




    public Cursor getTheaters(SQLiteDatabase db, String area) {
        String WHERE = THEATER.AREA + " = ?";
        String[] ARGS = {area};
        return db.query(THEATER.TABLE_NAME, COL_THEATER_LIST, WHERE, ARGS,
                null, null, null);
    }

    public Cursor getThNameById(SQLiteDatabase db, String thId) {
        String WHERE = THEATER.ID + " = ?";
        String[] ARGS = {thId};
        return db.query(THEATER.TABLE_NAME, COL_THEATER_LIST, WHERE, ARGS,
                null, null, null);
    }



    /***
     * cool
     ***/


    public Cursor getMvNameByMvId(SQLiteDatabase db, String mvId) {
        String[] FROM = {MOVIE.MAIN_NAME};
        String WHERE = MOVIE.ID + " = ?";
        String[] ARGS = new String[]{mvId};
        return db.query(MOVIE.TABLE_NAME, FROM, WHERE, ARGS, null, null, null);
    }

    public Cursor getAllMvIds(SQLiteDatabase db) {
        String[] FROM = {MOVIE.ID};
        return db.query(MOVIE.TABLE_NAME, FROM, null, null, null, null, null);
    }

    public Cursor getAllMvNames(SQLiteDatabase db) {
        String[] FROM = {MOVIE.MAIN_NAME};
        return db.query(MOVIE.TABLE_NAME, FROM, null, null, null, null, null);
    }


    public int deleteMovie(SQLiteDatabase db, String mvId) {
        String WHERE = MOVIE.ID + " = ? ";
        String[] ARGS = {mvId};
        return db.delete(MOVIE.TABLE_NAME, WHERE, ARGS);
    }


    @SuppressWarnings("unchecked")
    private ContentValues covertJson(JSONObject json) {
        ContentValues values = new ContentValues();
        for (Iterator<String> key = json.keys(); key.hasNext(); ) {
            String temp = key.next();
            try {
                values.put(temp, json.getString(temp));
            } catch (JSONException e) {
            }
        }
        return values;
    }



    public Cursor getMvUpdateDate(SQLiteDatabase db, String mvId) {
        String[] FROM = {MOVIE.UPDATE_DATE};
        String WHERE = MOVIE.ID + " = ?";
        String[] ARGS = new String[]{mvId};
        return db.query(MOVIE.TABLE_NAME, FROM, WHERE, ARGS, null, null, null);
    }

    public Cursor getAllMvNameId(SQLiteDatabase db) {
        String[] FROM = {MOVIE.MAIN_NAME, MOVIE.ID, MOVIE.EN_NAME};
        return db.query(MOVIE.TABLE_NAME, FROM, null, null, null, null, null);
    }

    public Cursor getPKMvByName(SQLiteDatabase db, String name) {
        String[] FROM = {MOVIE.MAIN_NAME, MOVIE.EN_NAME, MOVIE.IMG_LINK};
        String WHERE = MOVIE.MAIN_NAME + " = ?";
        return db.query(MOVIE.TABLE_NAME, FROM, WHERE, new String[]{name},
                null, null, null);
    }

    public Cursor getPKMvById(SQLiteDatabase db, String mvId) {
        String[] FROM = {MOVIE.MAIN_NAME, MOVIE.EN_NAME, MOVIE.IMG_LINK,
                MOVIE.ID};
        String WHERE = MOVIE.ID + " = ?";
        return db.query(MOVIE.TABLE_NAME, FROM, WHERE, new String[]{mvId},
                null, null, null);
    }


    public Cursor getAllTheaters(SQLiteDatabase db) {
        String[] FROM = {THEATER.NAME, THEATER.ADDRESS};
        return db.query(THEATER.TABLE_NAME, FROM, null, null, null, null, null);
    }

    public Cursor getShowtimeListByMvId(SQLiteDatabase db, String mvId) {

        String SQL = "select _id " + "from Mv_Showtimes " + "where mv_id = ? ";

		/*
         * String SQL = "select B.type_list, B.time_list " +
		 * "from Mv_Showtimes B, Movie C " + "where C.id = ? " +
		 * "and C.id = B.mv_id " ;
		 */
        String[] ARGS = {mvId};
        return db.rawQuery(SQL, ARGS);
    }

    public Cursor getShowtimeListByThName(SQLiteDatabase db, String thName) {
        String SQL = "select main_name, type_list, time_list "
                + "from MOVIE M, Showtimes S, Theaters T "
                + "where T.name = ? " + "and S.th_id = T._id "
                + "and M._id = S.mv_id";
        String[] ARGS = {thName};
        return db.rawQuery(SQL, ARGS);
    }

    public Cursor getAreaNameById(SQLiteDatabase db, String id) {
        String SQL = "select name from Areas where _id = ?";
        String[] ARGS = {id};
        return db.rawQuery(SQL, ARGS);
    }

    // OLD //
    public Cursor getTheaterInfoByName(SQLiteDatabase db, String name) {
        String[] FROM = {THEATER.NAME, THEATER.ADDRESS, THEATER.PHONE,
                THEATER.ID};
        String WHERE = THEATER.NAME + " = ?";
        String[] ARGS = {name};
        return db
                .query(THEATER.TABLE_NAME, FROM, WHERE, ARGS, null, null, null);
    }

    public Cursor getTheatersByArea(SQLiteDatabase db, String area) {
        String[] FROM = {THEATER.NAME, THEATER.ADDRESS, THEATER.ID};
        String WHERE = THEATER.AREA + " = ?";
        String[] ARGS = {area};
        return db
                .query(THEATER.TABLE_NAME, FROM, WHERE, ARGS, null, null, null);
    }


    public Cursor getMovieNameByMvId(SQLiteDatabase db, String mvId) {
        String[] FROM = {MOVIE.MAIN_NAME};
        String WHERE = MOVIE.ID + " = ?";
        String[] ARGS = {mvId};
        Cursor c = db.query(MOVIE.TABLE_NAME, FROM, WHERE, ARGS, null, null,
                null);
        return c;
    }


    /*
    public int insertGenre(SQLiteDatabase db, String id, String name) {
        ContentValues values = new ContentValues();
        values.put(GENRE.ID, id);
        values.put(GENRE.NAME, name);
        return (int) db.insert(GENRE.TABLE_NAME, null, values);
    }

    public Cursor getAllGenreNames(SQLiteDatabase db) {
        String[] FROM = { GENRE.NAME };
        Cursor c = db.query(GENRE.TABLE_NAME, FROM, null, null, null, null,
                null);
        return c;
    }
*/


    public int insertTrackingList(SQLiteDatabase db, String target, String type) {
        ContentValues values = new ContentValues();
        values.put(TRACKINGLIST.TARGET, target);
        values.put(TRACKINGLIST.TYPE, type);
        return (int) db.insert(TRACKINGLIST.TABLE_NAME, null, values);
    }

    public Cursor getTrackingListByType(SQLiteDatabase db, String type) {
        String[] FROM = {TRACKINGLIST.TARGET};
        String WHERE = TRACKINGLIST.TYPE + " = ?";
        String[] ARGS = {type};
        return db.query(TRACKINGLIST.TABLE_NAME, FROM, WHERE, ARGS, null, null,
                null);
    }

    public int deleteTrackingListByType(SQLiteDatabase db, String type) {
        String WHERE = TRACKINGLIST.TYPE + " = ?";
        String[] ARGS = {type};
        return db.delete(TRACKINGLIST.TABLE_NAME, WHERE, ARGS);
    }

    public int deleteTrackingListByTarAndType(SQLiteDatabase db, String target,
                                              String type) {
        String WHERE = TRACKINGLIST.TARGET + " = ? and " + TRACKINGLIST.TYPE
                + " = ?";
        String[] ARGS = {target, type};
        return db.delete(TRACKINGLIST.TABLE_NAME, WHERE, ARGS);
    }

    public Cursor getMvByTarAndType(SQLiteDatabase db, String[] target,
                                    String type) {
        String SQL = "select mainName, subName, id " + "from Movie where ";
        String[] ARGS = new String[target.length];

        if (type.equals("0"))
            type = "directors";
        else if (type.equals("1"))
            type = "actors";
        else if (type.equals("2"))
            type = "genreList";

        for (int i = 0; i < target.length; i++) {
            if (i != 0) {
                SQL += " or ";
            }
            SQL += type + " like ?";
            ARGS[i] = "%" + target[i] + "%";
        }

        return db.rawQuery(SQL, ARGS);
    }

}
