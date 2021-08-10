package com.example.weatherforecast.ui.details

import androidx.lifecycle.ViewModel
import com.example.weatherforecast.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    fun getCityWeather(id: Long) = repository.getCityWeatherCache(id)
}