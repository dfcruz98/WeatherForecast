package com.example.weatherforecast.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weatherforecast.data.local.CityWeather
import com.example.weatherforecast.repository.WeatherRepository
import com.example.weatherforecast.util.ActionResult


class FakeWeatherRepository : WeatherRepository {

    private val cityWeather = MutableLiveData(
        CityWeather(
            1, "Lisbon", "PT", "01",
            25.2, "", 2, 2, "999"
        )
    )

    override suspend fun getCityWeather(
        city: String,
        countryCode: String,
        units: String
    ): ActionResult<CityWeather> {
        return ActionResult.Success(
            CityWeather(
                1, "Lisbon", "PT", "01",
                25.2, "", 2, 2, "999"
            )
        )
    }

    override fun getCityWeatherCache(id: Long): LiveData<CityWeather?> {
        return cityWeather
    }

}