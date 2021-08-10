package com.example.weatherforecast.repository

import androidx.lifecycle.LiveData
import com.example.weatherforecast.data.local.CityWeather
import com.example.weatherforecast.data.local.WeatherDao
import com.example.weatherforecast.data.mapper.CityWeatherMapping
import com.example.weatherforecast.data.remote.WeatherService
import com.example.weatherforecast.util.ActionResult
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherService,
    private val db: WeatherDao
) : WeatherRepository {

    override suspend fun getCityWeather(
        latitude: String,
        longitude: String,
        countryCode: String,
        units: String
    ): ActionResult<CityWeather> {
        return try {
            val response = api.cityWeatherCoordinates(latitude, longitude, countryCode, units)
            val responseBody = response.body()

            if (!response.isSuccessful || responseBody == null) {
                return ActionResult.Error("Error getting weather")
            }

            val cityWeather = CityWeatherMapping().mapTo(responseBody)
            db.insertCityWeather(cityWeather)
            ActionResult.Success(cityWeather)

        } catch (ex: Exception) {
            ActionResult.Error("Error getting weather")
        }
    }

    override suspend fun getCityWeather(
        city: String,
        countryCode: String,
        units: String
    ): ActionResult<CityWeather> {
        return try {
            val response = api.cityWeather(city, countryCode, units)
            val responseBody = response.body()

            if (!response.isSuccessful || responseBody == null) {
                return ActionResult.Error("Error getting $city weather")
            }

            val cityWeather = CityWeatherMapping().mapTo(responseBody)
            db.insertCityWeather(cityWeather)
            ActionResult.Success(cityWeather)

        } catch (ex: Exception) {
            ActionResult.Error("Error getting $city weather")
        }
    }

    override fun getCityWeatherCache(id: Long): LiveData<CityWeather?> {
        return db.getCityWeather(id)
    }


}