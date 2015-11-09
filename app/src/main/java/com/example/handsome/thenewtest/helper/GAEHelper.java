package com.example.handsome.thenewtest.helper;

/**
 * Created by handsome on 2015/9/10.
 */

import android.graphics.drawable.Drawable;
import android.util.Log;

import com.example.handsome.thenewtest.exception.NoNetworkConnectedException;
import com.example.handsome.thenewtest.exception.StatusNotOkException;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

//  import org.json.JSONObject;
//import com.mm.exception.NoNetworkConnectedException;
// import com.mm.exception.StatusNotOkException;

public class GAEHelper {

    private static final String playingMvIds = "http://fju-bim1.appspot.com/service/playing-mv-ids";
    private static final String mvInfo = "http://fju-bim1.appspot.com/service/mv-info?id=";
    private static final String mvShowtime = "http://fju-bim1.appspot.com/service/mv-showtimes?id=";
    private static final String thShowtime = "http://fju-bim1.appspot.com/service/th-showtimes?id=";
    private static final String mvFanHaterNumber = "http://fju-bim1.appspot.com/service/mv-fan-hater-number?id=";

    private static final String HOST = "http://fju-bim1.appspot.com/";
    private static final String SERVCIE_RECOMMEND = HOST
            + "service/token/mv-recommend?token=%s";
    private static final String SERVICE_COMMENT = HOST
            + "service/token/comment?id=%s&token=%s&comment=%s";
    private static final String SERVICE_BATTLE = HOST
            + "service/token/battle?token=%s&mv1=%s&mv2=%s";
    private static final String SERVICE_POSTER = HOST + "service/greating";

    public static final String ERR_NULL_RESULT = "未知錯誤!請洽詢鄒董工程師!";

    public static Drawable getPoster() throws NoNetworkConnectedException, Exception {

        String src = URLHelper.getRequest(SERVICE_POSTER);
        JSONObject obj = JSONHelper.checkStatus(src);

        String url = obj.getString("pic_url");
        url = url.replace("mpost2", "mpost");

        return URLHelper.getDrawable(url);
    }

    public static String getPosterUrl() throws NoNetworkConnectedException, Exception {

        String src = URLHelper.getRequest(SERVICE_POSTER);
        JSONObject obj = JSONHelper.checkStatus(src);

        String url = obj.getString("pic_url");
        url = url.replace("mpost2", "mpost");

        return url;
    }

    public static JSONArray getRecommend(String token)
            throws NoNetworkConnectedException, Exception {
        Object[] parm = {token};
        String url = String.format(SERVCIE_RECOMMEND, parm);

        String source = URLHelper.getRequest(url);

        JSONObject json = new JSONObject(source);
        String status = json.getString("status");
        if (status.equalsIgnoreCase("OK")) {
            JSONArray array = (JSONArray) json.get("results");
            return array;
        } else {
            throw new StatusNotOkException();
        }
    }

    public static List<String> getPlayingMovieIds()
            throws NoNetworkConnectedException, Exception {
        String source = URLHelper.getRequest(playingMvIds);
        Log.i("hs", "?"+source);
        // turn to list
        List<String> result = new ArrayList<String>();
        JSONArray array = new JSONArray(source);
        for (int i = 0; i < array.length(); i++) {
            result.add(array.getString(i));
        }
        Log.i("hs", "result.toString() = " +result.toString());
        return result;
    }

    public static JSONObject getMovie(String movieId)
            throws NoNetworkConnectedException, Exception {
        String source = URLHelper.getRequest(mvInfo + movieId);
        JSONObject json = new JSONObject(source);
        return json;
    }

    public static JSONArray getMovieShowtimes(String movieId)
            throws NoNetworkConnectedException, Exception {
        String source = URLHelper.getRequest(mvShowtime + movieId);
        JSONArray array = new JSONArray(source.toString());
        return array;

    }

    // HS
    public static JSONArray getTheaterShowtimes(String thId)
            throws NoNetworkConnectedException, Exception {
        Log.v("TEST", "START getTheaterShowtimes ");
        String source = URLHelper.getRequest(thShowtime + thId);
        JSONArray array = new JSONArray(source);
        Log.v("TEST", "END! getTheaterShowtimes " + array);
        return array;

    }

    public static String[] getMovieCommentNumber(String movieId)
            throws NoNetworkConnectedException, Exception {
        String source = URLHelper.getRequest(mvFanHaterNumber + movieId);

        JSONObject json = new JSONObject(source);
        String fanNum = json.getString("fanNumber");
        String haterNum = json.getString("haterNumber");
        String[] result = {fanNum, haterNum};
        return result;
    }

    public static boolean commentMovie(String movieId, String token, int option)
            throws  Exception {
        String comment = "like";
        switch (option) {
            case 0:
                comment = "like";
                break;
            case 1:
                comment = "dislike";
                break;
        }
        Object[] parm = {movieId, token, comment};

        String url = String.format(SERVICE_COMMENT, parm);
        String src = URLHelper.getRequest(url);
        JSONHelper.checkStatus(src);
        return true;
    }

    public static String battleMovie(String token, String mv1, String mv2)
            throws  Exception {
        Object[] parm = {token, mv1, mv2};
        String service = SERVICE_BATTLE;

        String url = String.format(service, parm);
        String source = URLHelper.getRequest(url);
        JSONObject json = new JSONObject(source);
        String status = json.getString("status");
        Log.v("TEST", "status = " + status);
        if (status.equalsIgnoreCase("OK")) {
            // String results = json.getString("results");
            JSONArray array = (JSONArray) json.get("results");
            JSONObject first = array.getJSONObject(0);
            String winner = first.getString("winner");
            Log.v("TEST", "winner = " + winner);
            return winner;
        }
        return null;
    }

}

