package com.example.lab7;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface JsonPlaceholderAPI {
    @GET("data/2.5/weather?")
    Call<PostWeather> getCurrentWeatherData(@Query("q") String name, @Query("units") String units, @Query("APPID") String app_id);

}
