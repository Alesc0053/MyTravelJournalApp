package com.example.mytraveljournal.weatherApi

import com.squareup.moshi.Moshi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object MoshiApi {
    private val moshi = Moshi.Builder()
        .add(com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory())
        .build()

    val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl("https://api.openweathermap.org")
        .build()

    object API{
        val retrofitService : OpenWeatherApi by lazy {
            retrofit.create(OpenWeatherApi::class.java)
        }
    }
}