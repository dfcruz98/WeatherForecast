package com.example.weatherforecast.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.data.local.CityWeather
import com.example.weatherforecast.repository.WeatherRepository
import com.example.weatherforecast.util.ActionResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import java.util.*
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _loading = MutableLiveData(true)
    private val _refreshing = MutableLiveData(false)
    private val _error = MutableLiveData<String>()
    private val _citiesWeather = MutableLiveData<List<CityWeather>?>()
    private val _currentLocationWeather = MutableLiveData<CityWeather?>()

    val loading: LiveData<Boolean>
        get() = _loading

    val refreshing: LiveData<Boolean>
        get() = _refreshing

    val error: LiveData<String>
        get() = _error

    val citiesWeather: LiveData<List<CityWeather>?>
        get() = _citiesWeather

    val currentLocationWeather: LiveData<CityWeather?>
        get() = _currentLocationWeather

    init {
        _loading.postValue(true)
        getCitiesWeather()
    }

    fun getCurrentLocationWeather(city: String) {
        viewModelScope.launch {
            val weather = repository.getCityWeather(
                city,
                Locale.getDefault().country,
                "metric"
            )

            when (weather) {
                is ActionResult.Success -> {
                    _currentLocationWeather.postValue(weather.data)
                }
                is ActionResult.Error -> {
                    _error.postValue(weather.message)
                }
            }
        }
    }


    fun getCitiesWeather() {
        val cities = mutableListOf(
            "Lisbon",
            "Madrid",
            "Paris",
            "Berlin",
            "Copenhagen",
            "Roma",
            "London",
            "Dublin",
            "Prague",
            "Vienna"
        )

        viewModelScope.launch {
            supervisorScope {
                val awaits = cities.map {
                    async {
                        repository.getCityWeather(it, Locale.getDefault().country, "metric")
                    }
                }

                val result = awaits.awaitAll()

                val list = mutableListOf<CityWeather>()

                result.forEach {
                    if (it is ActionResult.Success) {
                        list.add(it.data)
                    } else if (it is ActionResult.Error) {
                        _error.postValue(it.message)
                    }
                }

                _loading.postValue(false)
                _refreshing.postValue(false)
                _citiesWeather.postValue(list)
            }
        }
    }

}