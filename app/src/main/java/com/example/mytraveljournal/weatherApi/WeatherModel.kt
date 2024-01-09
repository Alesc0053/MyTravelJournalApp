package com.example.mytraveljournal.weatherApi

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WeatherModel(
    val coord: Coord?,
    val weather: List<Weather>?,
    val main: Main?,
    val wind: Wind?,
    val rain: Rain?,
    val clouds: Clouds?,
    val sys: Sys?,
    val timezone: Int?,
    val id: Int?,
    val name: String?,
    val cod: Int?
) {
    @JsonClass(generateAdapter = true)
    data class Coord(
        val lon: Double?,
        val lat: Double?
    )

    @JsonClass(generateAdapter = true)
    data class Weather(
        val id: Int?,
        val main: String?,
        val description: String?,
        val icon: String?
    )

    @JsonClass(generateAdapter = true)
    data class Main(
        val temp: Double,
        val feels_like: Double?,
        val temp_min: Double?,
        val temp_max: Double?,
        val pressure: Int?,
        val humidity: Int?,
        @Json(name = "sea_level") val seaLevel: Int?,
        val grnd_level: Int?
    )

    @JsonClass(generateAdapter = true)
    data class Wind(
        val speed: Double?,
        val deg: Int?,
        val gust: Double?
    )

    @JsonClass(generateAdapter = true)
    data class Rain(
        @Json(name = "1h") val oneHour: Double?
    )

    @JsonClass(generateAdapter = true)
    data class Clouds(
        val all: Int?
    )

    @JsonClass(generateAdapter = true)
    data class Sys(
        val type: Int?,
        val id: Int?,
        val country: String?,
        val sunrise: Long?,
        val sunset: Long?
    )
}