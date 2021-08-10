package com.example.weatherforecast.data.remote

import com.example.weatherforecast.data.remote.objects.CityWeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("weather")
    suspend fun cityWeather(
        @Query("q") city: String,
        @Query("lang") lang: String,
        @Query("units") units: String,
    ): Response<CityWeatherResponse>

    @GET("weather")
    suspend fun cityWeatherCoordinates(
        @Query("lat") latitude: String,
        @Query("log") longitude: String,
        @Query("lang") lang: String,
        @Query("units") units: String,
    ): Response<CityWeatherResponse>
}