package com.example.handsome.thenewtest;

public interface DBConstants {

    String DATABASE_NAME = "MMDB";
    int DATABASE_VERSION = 1;

    interface MOVIE {
        String TABLE_NAME = "Movie";
        String ID = "id";
        String PLAYING_DATE = "playingDate";
        String AT_MOVIES_MV_ID = "atMoviesMvId";
        String MAIN_NAME = "mvName";
        String EN_NAME = "enName";
        String GATE = "gate";
        String IMG_LINK = "imgLink";
        String LENGTH = "mvlength";//GAE ....mvLength
        String DIRECTOR = "director";
        String ACTOR = "actor";
       // String LINK_LIST = "linkList";
        String STORY = "story";
        String UPDATE_DATE = "updateDate";
        String MV_TIME_STR = "mvTimeStr";
        String WRITER = "writer";
        String STATE = "state";
        String MV_IMDB_URL = "mv_IMDbMoblieUrl";
        String MV_TOMATOES_URL = "mv_tomatoesMoblieUrl";//mobile in GAE was spelled wrong.
        String IMDb_RATING = "IMDbRating";
        String TOMATOES_RATING = "tomatoesRating";
        String YOUTUBE_URL_LIST = "youtubeUrlList";
        String ALL_MV_TH_SHOWTIME_LIST = "allMvThShowtimeList";

    }

    String CREATE_MOVIE = MOVIE.TABLE_NAME + " ("
            + MOVIE.ID + " text , " + MOVIE.MAIN_NAME
            + " text not null, " + MOVIE.EN_NAME + " text, " + MOVIE.AT_MOVIES_MV_ID
            + " text, " + MOVIE.GATE + " text, " + MOVIE.IMG_LINK + " text, "
            + MOVIE.WRITER + " text, "
            + MOVIE.LENGTH + " text, " + MOVIE.DIRECTOR + " text, "
            + MOVIE.ACTOR + " text, " + MOVIE.MV_TIME_STR  + " text, "
            + MOVIE.STORY + " text, "
            + MOVIE.PLAYING_DATE  + " text, " + MOVIE.STATE + " text, "
            + MOVIE.MV_IMDB_URL+ " text, " + MOVIE.MV_TOMATOES_URL + " text, "
            + MOVIE.IMDb_RATING+ " real, "
            + MOVIE.TOMATOES_RATING+ " real, "
            + MOVIE.YOUTUBE_URL_LIST+ " text, "
            + MOVIE.ALL_MV_TH_SHOWTIME_LIST + " text, "+ MOVIE.UPDATE_DATE + " text, PRIMARY KEY ("+ MOVIE.ID +") );";

    /*interface GENRE {
        String TABLE_NAME = "Genre";
        String ID = "_id";
        String NAME = "name";
    }

    String CREATE_GENRE = GENRE.TABLE_NAME + " ("
            + GENRE.ID + " text primary key, " + GENRE.NAME
            + " text not null);";
*/
    interface THEATER {
        String TABLE_NAME = "Theater";
        String ID = "_id";
        String NAME = "name";
        String ADDRESS = "address";
        String PHONE = "phone";
        String AREA = "area";
        String LAT = "lat";
        String LNG = "lng";

    }

    String CREATE_THEATER = THEATER.TABLE_NAME + " ("
            + THEATER.ID + " text primary key, " + THEATER.NAME
            + " text not null, " + THEATER.ADDRESS + " text not null, "
            + THEATER.PHONE + " text, " + THEATER.AREA + " text not null, " + THEATER.LAT + " text, " + THEATER.LNG + " text);";


    interface TH_SHOWTIME {
        String TABLE_NAME = "Th_Showtime";
	   // String ID = "_id";
        String TH_ID = "thId";
        String TIME_INFO_STR = "timeInfoStr";
        //String UPDATE_TIME= "updateTime";

	}


    String CREATE_TH_SHOWTIME = TH_SHOWTIME.TABLE_NAME
            + " ("
            + TH_SHOWTIME.TH_ID + " text primary key, " + TH_SHOWTIME.TIME_INFO_STR
            + " text);";

    interface TRACKINGLIST {
        String TABLE_NAME = "TrackingList";
        String ID = "_id";
        String TARGET = "target";
        String TYPE = "type";
    }

    // TRACKINGLIST
    String CREATE_TRACKINGLIST = TRACKINGLIST.TABLE_NAME
            + " (" + TRACKINGLIST.ID + " integer primary key autoincrement, "
            + TRACKINGLIST.TARGET + " text not null, " + TRACKINGLIST.TYPE
            + " text not null);";


}
