package com.example.weatherforecast.data.local

import androidx.recyclerview.widget.DiffUtil
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "city_weather")
data class CityWeather(
    @PrimaryKey(autoGenerate = false)
    val cityId: Long,
    val city: String,
    val country: String,
    val icon: String,
    val temperature: Double,
    val weatherDescription: String,
    val pressure: Int,
    val humidity: Int,
    val windSpeed: String
)

object CityWeatherDiff : DiffUtil.ItemCallback<CityWeather>() {
    override fun areItemsTheSame(oldItem: CityWeather, newItem: CityWeather) =
        oldItem.city == newItem.city

    override fun areContentsTheSame(oldItem: CityWeather, newItem: CityWeather) = oldItem == newItem
}
