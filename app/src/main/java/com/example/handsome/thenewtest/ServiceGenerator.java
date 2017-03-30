package com.example.handsome.thenewtest;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ServiceGenerator {
    private static OkHttpClient httpClient = new OkHttpClient().newBuilder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build();
    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(NetworkConfig.getBaseUrl())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create());

    public static <S> S createService(Class<S> serviceClass) {
        Retrofit retrofit = builder
                .client(httpClient)
                .build();
        return retrofit.create(serviceClass);
    }
}
