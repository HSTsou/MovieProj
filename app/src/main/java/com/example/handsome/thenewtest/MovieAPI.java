package com.example.handsome.thenewtest;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by hs on 2017/1/12.
 */

public interface MovieAPI {

    @GET("all-mv")
    Observable<String> allMovieStr(

    );

    @GET("thInfo")
    Observable<String> getTheaterInfoStr(
            @Query("id")
                    String id
    );


    @GET("mvInfo")
    Observable<String> getMvInfoStr(
            @Query("id")
                    String id
    );

}
