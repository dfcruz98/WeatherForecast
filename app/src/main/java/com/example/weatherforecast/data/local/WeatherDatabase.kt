package com.example.weatherforecast.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [CityWeather::class],
    version = 1,
    exportSchema = false
)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}