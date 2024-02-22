package com.example.weather_api

import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Call

interface ApiInterface {

    @GET("weather")
    fun getWeatherData(
        @Query("q") city:String,
        @Query("appid") appid:String,
        @Query("units") units:String
    ) :Call<weatherapp>
}