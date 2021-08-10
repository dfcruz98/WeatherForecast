package com.example.weatherforecast.repository

import androidx.lifecycle.LiveData
import com.example.weatherforecast.data.local.CityWeather
import com.example.weatherforecast.util.ActionResult

interface WeatherRepository {

    suspend fun getCityWeather(
        latitude: String,
        longitude: String,
        countryCode: String,
        units: String
    ): ActionResult<CityWeather>

    suspend fun getCityWeather(
        city: String,
        countryCode: String,
        units: String
    ): ActionResult<CityWeather>

    fun getCityWeatherCache(id: Long): LiveData<CityWeather?>
}