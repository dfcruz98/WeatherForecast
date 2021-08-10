package com.example.weatherforecast.data.remote.objects

import com.google.gson.annotations.SerializedName

data class WeatherParameters(
    @SerializedName("temp")
    val temperature: Double?,
    val pressure: Int?,
    val humidity: Int?
)
