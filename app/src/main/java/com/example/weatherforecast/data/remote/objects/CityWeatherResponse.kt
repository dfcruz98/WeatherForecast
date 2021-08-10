package com.example.weatherforecast.data.remote.objects

import com.google.gson.annotations.SerializedName

data class CityWeatherResponse(
    @SerializedName("cod")
    val code: Int,
    val message: String?,
    val name: String?,
    val id: Long?,
    @SerializedName("sys")
    val country: Country?,
    val wind: Wind?,
    @SerializedName("main")
    val weatherParameters: WeatherParameters?,
    val weather: List<Weather>?
)