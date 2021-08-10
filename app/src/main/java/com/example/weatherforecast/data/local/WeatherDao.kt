package com.example.weatherforecast.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCityWeather(cityWeather: CityWeather)

    @Query("SELECT * FROM city_weather WHERE cityId = :cityId ")
    fun getCityWeather(cityId: Long): LiveData<CityWeather?>

    @Query("SELECT * FROM city_weather")
    fun getCitiesWeather(): LiveData<List<CityWeather>>
}