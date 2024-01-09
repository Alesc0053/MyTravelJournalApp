package com.example.mytraveljournal.weatherApi

import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherApi {

    @GET("data/2.5/weather")
    suspend fun listWeatherModel(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") api: String
    ): WeatherModel
}