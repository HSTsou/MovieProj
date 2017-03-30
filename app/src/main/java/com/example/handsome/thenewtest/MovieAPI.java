package com.example.handsome.thenewtest;

import retrofit2.http.Field;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by hs on 2017/1/12.
 */

public interface MovieAPI {

    static final String TH_TIME_URL = "https://movingmoviezero.appspot.com/thInfo?id=";

    @GET("all-mv")
    Observable<String> allMovieStr(

    );

    @GET("thInfo")
    Observable<String> getTheaterInfoStr(
            @Field("id")
                    String id
    );

}
