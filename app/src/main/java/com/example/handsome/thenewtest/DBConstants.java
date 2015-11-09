package com.example.handsome.thenewtest;

public interface DBConstants {

    String DATABASE_NAME = "MMDB";
    int DATABASE_VERSION = 1;

    interface MOVIE {
        String TABLE_NAME = "Movie";
        String ID = "_id";
        String PLAYING_DATE = "playingDate";
        String ATS_ID = "atsId";
        String MAIN_NAME = "mainName";
        String EN_NAME = "enName";
        String GATE = "gate";
        String IMG_LINK = "imgLink";
        String DATE = "date";
        String LENGTH = "mvLength";
        String DIRECTOR = "director";
        String ACTOR = "actor";
        String LINK_LIST = "linkList";
        String STORY = "story";
        String UPDATE_DATE = "updateDate";
        String MV_TIME_STR = "mvTimeStr";
        String WRITER = "writer";
        String STATE = "state";
        String MV_IMDB_URL = "mv_IMDbMoblieUrl";
        String MV_TOMATOES_URL = "mv_tomatoesMoblieUrl";
        String IMDb_RATING = "IMDbRating";
        String TOMATOES_RATING = "tomatoesRating";
        String YOUTUBE_URL_LIST = "youtubeUrlList";
        String ALL_MV_TH_SHOWTIME_LIST = "allMvThShowtimeList";

    }

    String CREATE_MOVIE = MOVIE.TABLE_NAME + " ("
            + MOVIE.ID + " text primary key, " + MOVIE.MAIN_NAME
            + " text not null, " + MOVIE.EN_NAME + " text, " + MOVIE.ATS_ID
            + " text, " + MOVIE.GATE + " text, " + MOVIE.IMG_LINK + " text, "
            + MOVIE.DATE + " text, " + MOVIE.WRITER + " text, "
            + MOVIE.LENGTH + " text, " + MOVIE.DIRECTOR + " text, "
            + MOVIE.ACTOR + " text, " + MOVIE.MV_TIME_STR  + " text, "
            + MOVIE.LINK_LIST + " text, " + MOVIE.STORY + " text, "
            + MOVIE.PLAYING_DATE  + " text, " + MOVIE.STATE + " text, "
            + MOVIE.MV_IMDB_URL+ " text, " + MOVIE.MV_TOMATOES_URL + " text, "
            + MOVIE.IMDb_RATING+ " real, "
            + MOVIE.TOMATOES_RATING+ " real, "
            + MOVIE.YOUTUBE_URL_LIST+ " text, "
            + MOVIE.ALL_MV_TH_SHOWTIME_LIST + " text, "+ MOVIE.UPDATE_DATE + " text);";

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

    interface AREA {
        String TABLE_NAME = "Area";
        String ID = "_id";
        String NAME = "name";
    }

    String CREATE_AREA = AREA.TABLE_NAME + " (" + AREA.ID
            + " text primary key, " + AREA.NAME + " text not null);";

    interface SHOWTIME {
        String TABLE_NAME = "Showtime";
        String ID = "id";
        String MV_ID = "movie";
        String TH_ID = "thId";
        String TYPE_LIST = "typeList";
        String TIME_LIST = "timeList";
        String UPDATE_DATE = "updateDate";
    }

    String CREATE_SHOWTIME = SHOWTIME.TABLE_NAME + " ("
            + SHOWTIME.ID + " integer primary key autoincrement, "
            + SHOWTIME.MV_ID + " text not null, "
            + SHOWTIME.TH_ID + " text not null, "
            + SHOWTIME.TIME_LIST + " text not null, "
            + SHOWTIME.TYPE_LIST + " text, "
            + SHOWTIME.UPDATE_DATE + " text);";


    interface TH_SHOWTIME {
        String TABLE_NAME = "Th_Showtime";
	    String ID = "_id";
        String TH_ID = "thId";
        String TIME_INFO_STR = "timeInfoStr";
        //String UPDATE_TIME= "updateTime";

	}
	
	 String CREATE_TH_SHOWTIME = TH_SHOWTIME.TABLE_NAME
	+ " (" + TH_SHOWTIME.ID + " integer primary key autoincrement, "
	+ TH_SHOWTIME.TH_ID + " text not null, " + TH_SHOWTIME.TIME_INFO_STR
	+ " text);";
	


    interface MOVIETYPE {
        String TABLE_NAME = "MovieType";
        String ID = "_id";
        String ENNAME = "enName";
        String CHNAME = "chName";
    }

    String CREATE_MOVIETYPE = MOVIETYPE.TABLE_NAME + " ("
            + MOVIETYPE.ID + " text primary key, " + MOVIETYPE.ENNAME
            + " text not null, " + MOVIETYPE.CHNAME + " text not null);";

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
