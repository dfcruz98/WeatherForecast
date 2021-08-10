package com.example.weatherforecast.data.mapper

import com.example.weatherforecast.data.local.CityWeather
import com.example.weatherforecast.data.remote.objects.CityWeatherResponse
import com.example.weatherforecast.util.EntityMapping

class CityWeatherMapping : EntityMapping<CityWeatherResponse, CityWeather> {
    override fun mapTo(entity: CityWeatherResponse): CityWeather {
        return CityWeather(
            cityId = entity.id!!,
            city = entity.name!!,
            country = entity.country!!.initials,
            icon = entity.weather!!.first().icon,
            weatherDescription = entity.weather.first().description,
            temperature = entity.weatherParameters!!.temperature!!,
            pressure = entity.weatherParameters.pressure!!,
            humidity = entity.weatherParameters.humidity!!,
            windSpeed = entity.wind!!.speed,
        )
    }
}